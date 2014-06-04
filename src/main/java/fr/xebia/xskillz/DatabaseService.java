package fr.xebia.xskillz;

import com.google.common.util.concurrent.AbstractIdleService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class DatabaseService extends AbstractIdleService implements Provider<GraphDatabaseService> {

    private GraphDatabaseService graphDb;

    @Override
    protected void startUp() throws Exception {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("target/data");
    }

    @Override
    protected void shutDown() throws Exception {
        graphDb.shutdown();
    }

    @Override
    public GraphDatabaseService get() {
        return graphDb;
    }
}
