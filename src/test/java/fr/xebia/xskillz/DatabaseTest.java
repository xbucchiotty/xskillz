package fr.xebia.xskillz;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.*;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.google.inject.util.Providers.of;
import static fr.xebia.xskillz.Database.*;
import static fr.xebia.xskillz.Relations.KNOWS;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.neo4j.graphdb.Direction.OUTGOING;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {

    private GraphDatabaseService graphDb = new MockedGraphDb();

    @Mock
    private Transaction tx;

    @Test
    public void extractNode_should_extract_column_from_map_and_cast_it_to_node() {
        String aColumn = "n";
        Node aNode = graphDb.createNode();
        Map<String, Object> aMap = ImmutableMap.<String, Object>of(aColumn, aNode);

        Node result = extractNode(aColumn).apply(aMap);

        assertThat(result).isEqualTo(aNode);
    }

}