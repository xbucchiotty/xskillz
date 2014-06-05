package fr.xebia.xskillz;

import com.google.common.base.Function;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.FluentIterable.from;
import static javax.ws.rs.core.Response.Status.NOT_MODIFIED;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.UriBuilder.fromResource;

@Path("/xebian")
@Singleton
public class XebianRepository {

    private final Provider<GraphDatabaseService> databaseProvider;

    @Inject
    public XebianRepository(Provider<GraphDatabaseService> databaseProvider) {
        this.databaseProvider = databaseProvider;
        put("john@xebia.fr");
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Collection<Xebian> xebians(@QueryParam("q") final String query) {
        final Predicate<Xebian> predicate;

        if (Strings.isNullOrEmpty(query)) {
            predicate = Predicates.alwaysTrue();
        } else {
            predicate = Xebian.withSkill(query);
        }

        return from(xebians).filter(predicate).toList();
    }

    public Collection<Xebian> allXebians() {
        return xebians("");
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Xebian> xebians() {
        return
                xebians;
    }

    public Collection<Xebian> allXebians() {
        return xebians("");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response find(@PathParam("id") final XebianId id) {
        return from(xebians)
                .firstMatch(Xebian.byId(id))
                .transform(new Function<Xebian, Response>() {
                    @Override
                    public Response apply(Xebian xebian) {
                        return Response.ok(xebian).build();
                    }
                }).or(status(NOT_MODIFIED).build());

        return Response.ok().build();

    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response add(@PathParam("id") final XebianId id, final String skill) {
        return from(xebians)
                .firstMatch(Xebian.byId(id))
                .transform(new Function<Xebian, Response>() {
                    @Override
                    public Response apply(Xebian xebian) {
                        xebian.addSkill(new Skill(skill));
                        return Response.ok(xebian).build();
                    }
                }).or(status(NOT_MODIFIED).build());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Xebian xebian) {
        return xebians.add(xebian) ? status(Response.Status.CREATED).build() : status(NOT_MODIFIED).build();

                return new Xebian(node.getId(), email);
            }
        });

        return created(fromResource(XebianRepository.class).build(xebian.getId())).build();

    }

    private Optional<Xebian> findXebian(XebianId id) {
        return withDatabase(queryNodeById(id.getValue()))
                .transform(toXebian());
    }


    private static Function<GraphDatabaseService, Optional<Node>> queryNodeById(final Long id) {
        return new Function<GraphDatabaseService, Optional<Node>>() {
            @Override
            public Optional<Node> apply(GraphDatabaseService graphDb) {
                return fromNullable(graphDb.getNodeById(id));
            }
        };
    }

    private <T> T withDatabase(Function<GraphDatabaseService, T> f) {
        return f.apply(databaseProvider.get());
    }

    private <T> T withinTransaction(final Function<GraphDatabaseService, T> f) {
        return withDatabase(new Function<GraphDatabaseService, T>() {

            @Override
            public T apply(GraphDatabaseService graphDb) {
                Transaction tx = graphDb.beginTx();

                T result = f.apply(graphDb);

                tx.success();
                tx.close();
                return result;
            }
        });
    }

    private static Function<Node, Xebian> toXebian() {
        return new Function<Node, Xebian>() {
            @Override
            public Xebian apply(Node node) {
                return new Xebian(node.getId(), node.getProperty(Xebian.Properties.EMAIL).toString());
            }
        };
    }

}
