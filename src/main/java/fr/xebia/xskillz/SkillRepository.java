package fr.xebia.xskillz;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashSet;

@Path("/skill")
public class SkillRepository {

    private static final Collection<Skill> skills = new HashSet<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Skill> skills() {
        return skills;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Skill skill) {
        return skills.add(skill) ? Response.status(Response.Status.CREATED).build() : Response.status(Response.Status.NOT_MODIFIED).build();

    }
}
