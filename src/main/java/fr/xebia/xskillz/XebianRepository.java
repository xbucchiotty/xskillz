package fr.xebia.xskillz;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashSet;

@Path("/xebian")
public class XebianRepository {

    private final static Collection<Xebian> xebians = new HashSet<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Xebian> xebians() {
        return xebians;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Xebian xebian) {
        return xebians.add(xebian) ? Response.status(Response.Status.CREATED).build() : Response.status(Response.Status.NOT_MODIFIED).build();

    }
}
