package fr.xebia.xskillz;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;

import static com.google.common.base.Functions.compose;
import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.FluentIterable.from;
import static fr.xebia.xskillz.Database.*;
import static fr.xebia.xskillz.Functions2.chain;
import static fr.xebia.xskillz.Functions2.liftForOptions;
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
    public Collection<Xebian> searchForXebiansBySkill(@QueryParam("q") final String query) {
        final Predicate<Xebian> predicate;

        if (isNullOrEmpty(query)) {
            predicate = alwaysTrue();
        } else {
            predicate = Xebian.withSkill(query);
        }

        return from(xebians()).filter(predicate).toList();
    }

    @POST
    @Produces(APPLICATION_JSON)
    public Response createAXebian(final String email) {
        Xebian xebian = withinTransaction(
                chain(Xebians.create(email), Xebians.fromNode),
                databaseProvider);

        return ok().apply(xebian);

    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{id}")
    public Response findById(@PathParam("id") final long id) {
        return withinTransaction(chain(Xebians.findById(id), liftForOptions(Xebians.fromNode)), databaseProvider)
                .transform(Responses.ok())
                .or(Response.status(NOT_FOUND).build());

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
                                chain(Skills.addKnown(skill), Xebians.fromNode);

                        Function<Optional<Node>, Optional<Xebian>> addSkillAndGetXebianIfPossible =
                                liftForOptions(addSkillAndGetXebian);

                        Function<GraphDatabaseService, Optional<Xebian>> optionalXebianWithSkill =
                                chain(Xebians.findById(id), addSkillAndGetXebianIfPossible);

                        return optionalXebianWithSkill.apply(graphDb);
                    }
                }, databaseProvider
        ).transform(Responses.ok())
                .or(Response.notModified().build());
    }

    public Collection<Xebian> xebians() {
        return withinTransaction(
                queryAll("MATCH (n:" + Labels.XEBIAN + ") return n", compose(Xebians.fromNode, extractNodeFromColumn("n")))
                , databaseProvider);
    }


}
