package fr.xebia.xskillz;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashSet;

import static com.google.common.collect.FluentIterable.from;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_MODIFIED;
import static javax.ws.rs.core.Response.status;

@Path("/xebian")
@Singleton
public class XebianRepository {

    private final Collection<Xebian> xebians = new HashSet<>();

    public XebianRepository() {
        xebians.add(new Xebian("john@xebia.fr"));
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
    @Produces(APPLICATION_JSON)
    @Path("/{id}")
    public Response find(@PathParam("id") final XebianId id) {
        return from(xebians)
                .firstMatch(Xebian.byId(id))
                .transform(new Function<Xebian, Response>() {
                    @Override
                    public Response apply(Xebian xebian) {
                        return Response.ok(xebian).build();
                    }
                })
                .or(status(NOT_MODIFIED).build());

    }

    @PUT
    @Produces(APPLICATION_JSON)
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
                })
                .or(status(NOT_MODIFIED).build());
    }

    @POST
    @Produces(APPLICATION_JSON)
    public Response put(Xebian xebian) {
        return xebians.add(xebian) ? status(Response.Status.CREATED).build() : status(NOT_MODIFIED).build();

    }

}
