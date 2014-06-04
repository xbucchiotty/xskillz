package fr.xebia.xskillz;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import static com.google.common.collect.FluentIterable.from;

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

    @POST
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Skill> search(String query) {

        final List<Predicate<Skill>> predicates = new ArrayList<>();

        Iterator<String> iterator = Splitter.on(' ').trimResults().split(query).iterator();

        while (iterator.hasNext()) {
            final String searchItem = iterator.next();
            predicates.add(new Predicate<Skill>() {
                @Override
                public boolean apply(Skill o) {
                    return o.getName().toLowerCase().contains(searchItem.toLowerCase());
                }
            });

        }
        Predicate<Skill> skillPredicate = Predicates.or(predicates);

        return from(skills)
                .filter(skillPredicate)
                .toList();
    }
}
