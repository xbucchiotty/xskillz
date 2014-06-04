package fr.xebia.xskillz;

import com.google.common.base.Function;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

import static com.google.common.collect.FluentIterable.from;
import static javax.ws.rs.core.Response.Status.NOT_MODIFIED;
import static javax.ws.rs.core.Response.status;

@Path("/xebian")
@Singleton
public class XebianRepository {

    private final Provider<GraphDatabaseService> databaseProvider;

    @Inject
    public XebianRepository(Provider<GraphDatabaseService> databaseProvider) {
        this.databaseProvider = databaseProvider;
        put(new Xebian("john@xebia.fr"));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Xebian> xebians() {
        return databaseProvider.get();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response find(@PathParam("id") final XebianId id) {
        return from(xebians())
                .firstMatch(Xebian.byId(id))
                .transform(new Function<Xebian, Response>() {
                    @Override
                    public Response apply(Xebian xebian) {
                        return Response.ok(xebian).build();
                    }
                }).or(status(NOT_MODIFIED).build());

    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response add(@PathParam("id") final XebianId id, final String skill) {
        return from(xebians())
                .firstMatch(Xebian.byId(id))
                .transform(new Function<Xebian, Response>() {
                    @Override
                    public Response apply(Xebian xebian) {
                        xebian.addSkill(new Skill(skill));
                        return Response.ok().build();
                    }
                }).or(status(NOT_MODIFIED).build());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Xebian xebian) {
        GraphDatabaseService graphDb = databaseProvider.get();

        Node firstNode = graphDb.createNode();
        firstNode.setProperty("message", "Hello, ");
        Node secondNode = graphDb.createNode();
        secondNode.setProperty("message", "World!");

        Relationship relationship = firstNode.createRelationshipTo(secondNode, Relation.KNOWS);
        relationship.setProperty("message", "brave Neo4j ");

        return databaseProvider.get().add(xebian) ? status(Response.Status.CREATED).build() : status(NOT_MODIFIED).build();

    }

}
