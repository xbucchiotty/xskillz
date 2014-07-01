package fr.xebia.xskillz;

import org.neo4j.graphdb.GraphDatabaseService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/auth")
@Singleton
public class AuthRepository {

    private final XebianRepository xebiansRepository;

    private final Provider<GraphDatabaseService> databaseProvider;

    @Inject
    public AuthRepository(XebianRepository xebians, Provider<GraphDatabaseService> databaseProvider) {
        this.xebiansRepository = xebians;
        this.databaseProvider = databaseProvider;
    }

    @GET
    public String randomAccount() {
        return xebiansRepository.xebians().stream()
                .findFirst()
                .orElseGet(() -> Xebians.create("john@xebia.fr").andThen(Xebians.fromNode).apply(databaseProvider.get()))
                .getEmail();
    }
}
