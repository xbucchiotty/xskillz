package fr.xebia.xskillz;

import org.neo4j.graphdb.GraphDatabaseService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.stream.Stream;

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
                .map(optionalXebian -> optionalXebian.orElseGet(() -> Xebians.create("john@xebia.fr").andThen(Xebians.fromNode).apply(databaseProvider.get())))
                .run(databaseProvider)
                .getEmail();
    }
}
