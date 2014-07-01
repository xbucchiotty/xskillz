package fr.xebia.xskillz;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.*;

import javax.inject.Provider;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static fr.xebia.xskillz.Functions.stream;
import static fr.xebia.xskillz.Relations.toEndNode;
import static java.util.stream.Collectors.toList;

public abstract class Database {

    private Database() {

    }

    public static Function<GraphDatabaseService, Optional<Node>> queryNodeById(final Long id) {
        return graphDb -> Optional.ofNullable(graphDb.getNodeById(id));
    }

    public static <T> T withinTransaction(final Function<GraphDatabaseService, T> f, Provider<GraphDatabaseService> databaseProvider) {
        GraphDatabaseService graphDb = databaseProvider.get();
        Transaction tx = graphDb.beginTx();

        T result = f.apply(graphDb);

        tx.success();
        tx.close();
        return result;
    }

    public static Function<Map<String, Object>, Node> extractNodeFromColumn(final String column) {
        return map -> (Node) map.get(column);
    }

    public static <T> Function<GraphDatabaseService, Collection<T>> queryAll(final String query, final Function<Map<String, Object>, T> dataExtractor) {
        return graphDb -> {
            ExecutionEngine engine = new ExecutionEngine(graphDb);

            return stream(engine.execute(query))
                    .map(dataExtractor)
                    .collect(toList());
        };
    }

    public static Function<GraphDatabaseService, Node> findOr(final Label label, final String propertyKey, final String value, final Function<GraphDatabaseService, Node> or) {
        return new Function<GraphDatabaseService, Node>() {
            @Override
            public Node apply(GraphDatabaseService graphDb) {
                return stream(graphDb.findNodesByLabelAndProperty(label, propertyKey, value))
                        .findFirst()
                        .orElse(or.apply(graphDb));
            }
        };
    }

    public static Function<Node, Node> addRelation(final Node endNode, final Direction direction, final Relations relations) {
        return new Function<Node, Node>() {
            @Override
            public Node apply(Node xebianNode) {
                return stream(xebianNode.getRelationships(direction, relations))
                        .map(toEndNode)
                        .filter(node -> node.equals(endNode))
                        .findFirst()
                        .orElseGet(() -> {
                            xebianNode.createRelationshipTo(endNode, relations);
                            return xebianNode;
                        });
            }
        };
    }

}