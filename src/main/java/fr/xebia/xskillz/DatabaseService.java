package fr.xebia.xskillz;

import com.google.common.util.concurrent.AbstractIdleService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.logging.Logger;

@Singleton
public class DatabaseService extends AbstractIdleService implements Provider<GraphDatabaseService> {

    public static final Logger logger = Logger.getAnonymousLogger();

    private Optional<GraphDatabaseService> graphDb = Optional.empty();

    @Override
    protected void startUp() throws Exception {
        logger.info("Database starting");

        graphDb = Optional.of(new GraphDatabaseFactory().newEmbeddedDatabase("target/data"));

        logger.info("Database started");
    }

    @Override
    protected void shutDown() throws Exception {
        logger.info("Database terminating");

        if (graphDb.isPresent()) {
            graphDb.get().shutdown();
        }

        graphDb = Optional.empty();

        logger.info("Database terminated");
    }

    @Override
    public GraphDatabaseService get() {
        return graphDb.orElse(null);
    }
}
