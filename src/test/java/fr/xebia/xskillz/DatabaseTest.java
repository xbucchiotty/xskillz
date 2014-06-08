package fr.xebia.xskillz;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.*;

import java.util.Map;

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

    private Label aLabel = DynamicLabel.label("aLabel");
    private String aProperty = "aProperty";
    private String aValue = "aValue";


    @Test
    public void queryNodeById_should_returns_node_from_id() {
        Node aNode = graphDb.createNode();
        Optional<Node> result = queryNodeById(aNode.getId()).apply(graphDb);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(aNode);
    }

    @Test
    public void queryNodeById_should_returns_absent_when_node_not_found() {
        Optional<Node> result = queryNodeById(0L).apply(graphDb);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void extractNodeFromColumn_should_extract_column_from_map_and_cast_it_to_node() {
        String aColumn = "n";
        Node aNode = graphDb.createNode();
        Map<String, Object> aMap = ImmutableMap.<String, Object>of(aColumn, aNode);

        Node result = extractNodeFromColumn(aColumn).apply(aMap);

        assertThat(result).isEqualTo(aNode);
    }

    @Test
    public void findOr_should_find_node_by_label_and_property_and_take_first_one() {
        Node firstNode = graphDb.createNode(aLabel);
        firstNode.setProperty(aProperty, aValue);
        final Node aNode = graphDb.createNode();

        Node result = findOr(aLabel, aProperty, aValue, new Function<GraphDatabaseService, Node>() {
            @Override
            public Node apply(GraphDatabaseService input) {
                return aNode;
            }
        }).apply(graphDb);

        assertThat(result).isEqualTo(firstNode);
    }

    @Test
    public void findOr_should_returns_default_value_when_node_not_found() {
        final Node aNode = graphDb.createNode();

        Node result = findOr(aLabel, aProperty, aValue, new Function<GraphDatabaseService, Node>() {
            @Override
            public Node apply(GraphDatabaseService input) {
                return aNode;
            }
        }).apply(graphDb);

        assertThat(result).isEqualTo(aNode);
    }

    @Test
    public void addRelation_should_add_relation_to_end_node_when_relation_does_not_exist_yet() {
        Node firstNode = graphDb.createNode();
        Node secondNode = graphDb.createNode();

        addRelation(secondNode, OUTGOING, KNOWS).apply(firstNode);

        assertThat(firstNode.getSingleRelationship(KNOWS, OUTGOING).getEndNode()).isEqualTo(secondNode);
    }

    @Test
    public void addRelation_should_not_add_relation_to_end_node_when_relation_already_exist() {
        Node firstNode = graphDb.createNode();
        Node secondNode = graphDb.createNode();

        addRelation(secondNode, OUTGOING, KNOWS).apply(firstNode);
        addRelation(secondNode, OUTGOING, KNOWS).apply(firstNode);

        assertThat(firstNode.getRelationships()).hasSize(1);
    }

    @Test
    public void withinTransaction_should_execution_inner_function_inside_transaction() {
        Function<GraphDatabaseService, String> innerFunction = new Function<GraphDatabaseService, String>() {
            @Override
            public String apply(GraphDatabaseService input) {
                return "result";
            }
        };
        GraphDatabaseService spy = spy(graphDb);
        when(spy.beginTx()).thenReturn(tx);

        String result = withinTransaction(innerFunction, of(spy));

        assertThat(result).isEqualTo("result");

        verify(tx).success();
        verify(tx).close();
    }


}
