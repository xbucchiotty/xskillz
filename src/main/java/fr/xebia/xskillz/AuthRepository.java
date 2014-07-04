package fr.xebia.xskillz;

import org.neo4j.graphdb.GraphDatabaseService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.stream.Stream;

import static fr.xebia.xskillz.Functions.getOptionalOr;

@Path("/auth")
@Singleton
public class AuthRepository {

    private final Provider<GraphDatabaseService> databaseProvider;

    @Inject
    public AuthRepository(Provider<GraphDatabaseService> databaseProvider) {
        this.databaseProvider = databaseProvider;
    }

    @GET
    public String randomAccount() {
        return Transaction.start(Xebians.queryAll())
                .map(Stream::findFirst)
                .flatMap(db -> getOptionalOr(() -> Xebians.create("john@xebia.fr").andThen(Xebians.fromNode).apply(db)))
                .run(databaseProvider)
                .getEmail();
    }
}
