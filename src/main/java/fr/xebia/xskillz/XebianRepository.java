package fr.xebia.xskillz;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.isNullOrEmpty;
import static fr.xebia.xskillz.Database.*;
import static fr.xebia.xskillz.Functions.liftForOptions;
import static fr.xebia.xskillz.Responses.ok;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

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
    public Collection<Xebian> searchForXebiansBySkill(@QueryParam("q") final String query, @QueryParam("email") String email) {
        Predicate<Xebian> predicate;

        if (isNullOrEmpty(email)) {
            predicate = xebian -> true;
        } else {
            predicate = xebia -> xebia.getEmail().equals(email);
        }

        if (!isNullOrEmpty(query)) {
            predicate = predicate.and(Xebian.withSkill(query));
        }

        return xebians().stream().filter(predicate).collect(Collectors.toList());
    }

    @POST
    @Produces(APPLICATION_JSON)
    public Response createAXebian(final String email) {
        Xebian xebian = withinTransaction(
                Xebians.create(email).andThen(Xebians.fromNode),
                databaseProvider);

        return ok().apply(xebian);

    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{id}")
    public Response findById(@PathParam("id") final long id) {
        return withinTransaction(Xebians.findById(id).andThen(liftForOptions(Xebians.fromNode)), databaseProvider)
                .map(Responses.ok())
                .orElse(Response.status(NOT_FOUND).build());

    }

    @PUT
    @Produces(APPLICATION_JSON)
    @Path("{id}")
    public Response addSkill(@PathParam("id") final long id, final String skillName) {
        return withinTransaction(
                new Function<GraphDatabaseService, Optional<Xebian>>() {
                    @Override
                    public Optional<Xebian> apply(GraphDatabaseService graphDb) {
                        Node skill = Skills.findOrCreate(skillName).apply(graphDb);

                        Function<Node, Xebian> addSkillAndGetXebian =
                                Skills.addKnown(skill).andThen(Xebians.fromNode);

                        Function<Optional<Node>, Optional<Xebian>> addSkillAndGetXebianIfPossible =
                                liftForOptions(addSkillAndGetXebian);

                        Function<GraphDatabaseService, Optional<Xebian>> optionalXebianWithSkill =
                                Xebians.findById(id).andThen(addSkillAndGetXebianIfPossible);

                        return optionalXebianWithSkill.apply(graphDb);
                    }
                }, databaseProvider
        ).map(Responses.ok())
                .orElse(Response.notModified().build());
    }

    public Collection<Xebian> xebians() {
        return withinTransaction(
                queryAll("MATCH (n:" + Labels.XEBIAN + ") return n", Xebians.fromNode.compose(extractNodeFromColumn("n")))
                , databaseProvider);
    }


}
