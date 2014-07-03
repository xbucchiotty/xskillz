package fr.xebia.xskillz;

import com.google.common.base.Strings;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

import static fr.xebia.xskillz.Database.createRelationFrom;
import static fr.xebia.xskillz.Functions.getOptionalOr;
import static fr.xebia.xskillz.Functions.liftO;
import static fr.xebia.xskillz.Functions.toList;
import static fr.xebia.xskillz.Relations.KNOWS;
import static fr.xebia.xskillz.Responses.redirectToNode;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.UriBuilder.fromResource;

@Path("/xebian")
@Singleton
public class XebianRepository {

    private final Provider<GraphDatabaseService> databaseProvider;

    @Inject
    public XebianRepository(Provider<GraphDatabaseService> databaseProvider) {
        this.databaseProvider = databaseProvider;
    }


    @GET
    @Produces(APPLICATION_JSON)
    @Path("{id}")
    public Response findById(@PathParam("id") final long id) {
        return Transaction.start(Xebians.findById(id))
                .map(liftO(Xebians.fromNode))
                .map(Responses.ok())
                .run(databaseProvider);
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Collection<Xebian> searchForXebians(@QueryParam("q") final String query, @QueryParam("email") String email) {

        Predicate<Xebian> emailPredicate = xebian -> Strings.isNullOrEmpty(email) || xebian.getEmail().equals(email);
        Predicate<Xebian> skillPredicate = xebian -> Strings.isNullOrEmpty(query) || xebian.matches(query);

        return Transaction.start(Xebians.queryAll())
                .map(stream -> stream.filter(emailPredicate.and(skillPredicate)))
                .map(toList())
                .run(databaseProvider);
    }

    @POST
    @Produces(APPLICATION_JSON)
    public Response createAXebian(final String email) {

        Function<GraphDatabaseService, Response> createAndDisplay = Xebians.create(email).andThen(node ->
                created(fromResource(XebianRepository.class).path(Long.toString(node.getId())).build()).build());

        return Transaction.start(Xebians.findByEmail(email))
                .map(liftO(redirectToNode()))
                .flatMap(db -> getOptionalOr(() -> createAndDisplay.apply(db)))
                .run(databaseProvider);

    }

    @PUT
    @Produces(APPLICATION_JSON)
    @Path("{id}")
    public Response addSkill(@PathParam("id") final long id, final String skillName) {
        return Transaction.start(Xebians.findById(id))
                .flatMap(db -> liftO(createKnownRelation(skillName).apply(db)))
                .map(liftO(Xebians.fromNode))
                .map(Responses.ok())
                .run(databaseProvider);

    }

    public static Function<GraphDatabaseService, Function<Node, Node>> createKnownRelation(String skillName) {
        return db -> xebianNode ->
                Skills.findOrCreate(skillName)
                        .andThen(createRelationFrom(xebianNode, KNOWS))
                        .andThen(optional -> xebianNode).apply(db);
    }

}