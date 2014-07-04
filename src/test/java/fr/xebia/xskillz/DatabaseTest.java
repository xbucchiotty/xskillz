package fr.xebia.xskillz;

import com.google.common.collect.ImmutableMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import static fr.xebia.xskillz.Database.*;
import static fr.xebia.xskillz.Relations.KNOWS;
import static java.nio.file.Files.createTempDirectory;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.neo4j.graphdb.Direction.OUTGOING;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {

    private GraphDatabaseService mockedDb = new MockedGraphDb();

    private static GraphDatabaseService db;

    @Test
    public void extractNode_should_extract_column_from_map_and_cast_it_to_node() {
        String aColumn = "n";
        Node aNode = mockedDb.createNode();
        Map<String, Object> aMap = ImmutableMap.<String, Object>of(aColumn, aNode);

        Node result = extractNode(aColumn).apply(aMap);

        assertThat(result).isEqualTo(aNode);
    }

    @Test
    public void createRelationFrom_should_create_named_relation_from_node_to_applied_end_node() {
        MockedNode startNode = new MockedNode();
        MockedNode endNode = new MockedNode();

        Relationship result = addRelationshipTo(startNode, KNOWS).apply(endNode);

        assertThat(result.getStartNode()).isEqualTo(startNode);
        assertThat(result.getEndNode()).isEqualTo(endNode);
        assertThat(result.getType()).isEqualTo(KNOWS);

        assertThat(startNode.hasRelationship(OUTGOING, KNOWS));
    }

    @Test
    public void execute_should_execute_cyper_query() throws IOException {
        String query = "START n=node(*) RETURN n";

        Stream<Map<String, Object>> result = execute(query).apply(db);

        assertThat(result).isNotNull();
    }

    @BeforeClass
    public static void setUp() throws IOException {
        db = new GraphDatabaseFactory().newEmbeddedDatabase(createTempDirectory("xsillz-db").toString());
    }

    @AfterClass
    public static void tearDown() {
        if (db != null) {
            db.shutdown();
        }
    }

}