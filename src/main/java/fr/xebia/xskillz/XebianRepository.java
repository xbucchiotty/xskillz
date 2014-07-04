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

import static fr.xebia.xskillz.Database.addRelationshipTo;
import static fr.xebia.xskillz.Functions.*;
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
    @Path("{email}")
    public Response findById(@PathParam("email") final String email) {
        return Transaction.start(Xebians.findByEmail(email))
                .map(liftO(Xebians.fromNode))
                .map(Responses.ok())
                .run(databaseProvider);
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Collection<Xebian> searchForXebians(@QueryParam("q") final String skillQuery) {
        Predicate<Xebian> skillPredicate = xebian -> Strings.isNullOrEmpty(skillQuery) || xebian.matches(skillQuery);

        return Transaction.start(Xebians.queryAll())
                .map(stream -> stream.filter(skillPredicate))
                .map(toList())
                .run(databaseProvider);
    }

    @POST
    @Produces(APPLICATION_JSON)
    public Response createAXebian(final String email) {

        Function<GraphDatabaseService, Response> createAndDisplay = Xebians.create(email).andThen(node ->
                created(fromResource(XebianRepository.class).path(Long.toString(node.getId())).build()).build());

        return Transaction.start(Xebians.findByEmail(email))
                .map(liftO(redirectToNode(XebianRepository.class)))
                .flatMap(db -> getOptionalOr(() -> createAndDisplay.apply(db)))
                .run(databaseProvider);

    }

    @PUT
    @Produces(APPLICATION_JSON)
    @Path("{email}")
    public Response addSkill(@PathParam("email") final String email, final String skillName) {
        return Transaction.start(Xebians.findByEmail(email))
                .flatMap(db -> liftO(createKnownRelation(skillName).apply(db)))
                .map(liftO(Xebians.fromNode))
                .map(Responses.ok())
                .run(databaseProvider);

    }

    public static Function<GraphDatabaseService, Function<Node, Node>> createKnownRelation(String skillName) {
        return db -> xebianNode ->
                Skills.findOrCreate(skillName)
                        .andThen(addRelationshipTo(xebianNode, KNOWS))
                        .andThen(relation -> xebianNode).apply(db);
    }

}