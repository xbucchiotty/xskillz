package fr.xebia.xskillz;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.event.KernelEventHandler;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.graphdb.traversal.BidirectionalTraversalDescription;
import org.neo4j.graphdb.traversal.TraversalDescription;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.FluentIterable.from;
import static fr.xebia.xskillz.TestHelpers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockedGraphDb implements GraphDatabaseService {

    private Set<Node> nodes = new HashSet<>();

    @Override
    public Node createNode() {
        MockedNode mockedNode = new MockedNode();
        nodes.add(mockedNode);
        return mockedNode;
    }

    @Override
    public Node createNode(Label... labels) {
        MockedNode mockedNode = new MockedNode(labels);
        nodes.add(mockedNode);
        return mockedNode;
    }

    @Override
    public Node getNodeById(final long l) {
        return from(nodes).firstMatch(nodeId(l)).orNull();
    }

    @Override
    public Relationship getRelationshipById(final long l) {
        return from(nodes)
                .transformAndConcat(toRelations())
                .firstMatch(relationshipId(l))
                .orNull();
    }

    @Override
    public Iterable<Node> getAllNodes() {
        return nodes;
    }

    @Override
    public ResourceIterable<Node> findNodesByLabelAndProperty(final Label label, final String s, final Object o) {
        return resourceIterableFrom(
                from(nodes).filter(
                        and(nodeHasLabel(label), hasProperty(s, o)))
        );
    }

    @Override
    public Iterable<RelationshipType> getRelationshipTypes() {
        return from(nodes)
                .transformAndConcat(toRelations())
                .transform(toType());
    }

    @Override
    public boolean isAvailable(long l) {
        return from(nodes).anyMatch(nodeId(l));
    }

    @Override
    public void shutdown() {

    }

    @Override
    public Transaction beginTx() {
        return mock(Transaction.class);

    }

    @Override
    public <T> TransactionEventHandler<T> registerTransactionEventHandler(TransactionEventHandler<T> tTransactionEventHandler) {
        return null;
    }

    @Override
    public <T> TransactionEventHandler<T> unregisterTransactionEventHandler(TransactionEventHandler<T> tTransactionEventHandler) {
        return null;
    }

    @Override
    public KernelEventHandler registerKernelEventHandler(KernelEventHandler kernelEventHandler) {
        return null;
    }

    @Override
    public KernelEventHandler unregisterKernelEventHandler(KernelEventHandler kernelEventHandler) {
        return null;
    }

    @Override
    public Schema schema() {
        return null;
    }

    @Override
    public IndexManager index() {
        return null;
    }

    @Override
    public TraversalDescription traversalDescription() {
        return null;
    }

    @Override
    public BidirectionalTraversalDescription bidirectionalTraversalDescription() {
        return null;
    }
}
