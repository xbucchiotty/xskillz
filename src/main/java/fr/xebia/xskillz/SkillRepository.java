package fr.xebia.xskillz;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;

@Path("/skill")
@Singleton
public class SkillRepository {

    private final XebianRepository xebiansRepository;

    @Inject
    public SkillRepository(XebianRepository xebians) {
        this.xebiansRepository = xebians;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Skill> skills() {
        return from(xebiansRepository.xebians()).transformAndConcat(new Function<Xebian, Collection<Skill>>() {
            @Override
            public Collection<Skill> apply(Xebian xebian) {
                return xebian.getSkills();
            }
        }).toSet();
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

        return from(skills())
                .filter(skillPredicate)
                .toList();
    }
}
