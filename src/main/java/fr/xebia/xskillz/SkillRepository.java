package fr.xebia.xskillz;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.inject.Inject;
import javax.inject.Provider;
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
import static fr.xebia.xskillz.Database.withinTransaction;
import static fr.xebia.xskillz.Labels.SKILL;
import static fr.xebia.xskillz.Skills.fromNode;

@Path("/skill")
@Singleton
public class SkillRepository {

    private final Provider<GraphDatabaseService> databaseProvider;

    @Inject
    public SkillRepository(Provider<GraphDatabaseService> databaseProvider) {
        this.databaseProvider = databaseProvider;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Skill> skills() {
        return withinTransaction(new Function<GraphDatabaseService, Collection<Skill>>() {
            @Override
            public Collection<Skill> apply(GraphDatabaseService graphDb) {
                return from(graphDb.findNodesByLabelAndProperty(SKILL, null, null))
                        .transform(fromNode)
                        .toList();
            }
        }, databaseProvider);
    }

    @POST
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Skill> search(String query) {

        final List<Predicate<Skill>> predicates = new ArrayList<>();

        Iterator<String> iterator = Splitter.on(' ').trimResults().split(query).iterator();

        while (iterator.hasNext()) {
            final String searchItem = iterator.next();
            predicates.add(Skill.searchForItem(searchItem));
        }
        Predicate<Skill> skillPredicate = Predicates.or(predicates);

        return from(skills())
                .filter(skillPredicate)
                .toList();
    }

}
