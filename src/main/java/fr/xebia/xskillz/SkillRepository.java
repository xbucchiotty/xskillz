package fr.xebia.xskillz;

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
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static fr.xebia.xskillz.Database.*;

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
        return withinTransaction(
                queryAll("MATCH (n:" + Labels.SKILL + ") return n", Skills.fromNode.compose(extractNodeFromColumn("n")))
                , databaseProvider);
    }

    @POST
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Skill> search(String query) {

        Iterator<String> iterator = Splitter.on(' ').trimResults().split(query).iterator();

        Predicate<Skill> predicate = (skill) -> false;

        while (iterator.hasNext()) {
            final String searchItem = iterator.next();
            predicate = predicate.or(Skill.searchForItem(searchItem));
        }

        return skills().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

}