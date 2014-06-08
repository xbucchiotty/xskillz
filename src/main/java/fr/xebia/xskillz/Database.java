package fr.xebia.xskillz;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.*;

import javax.inject.Provider;
import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.FluentIterable.from;
import static fr.xebia.xskillz.Relations.toEndNode;

public abstract class Database {

    private Database() {

    }

    public static Function<GraphDatabaseService, Optional<Node>> queryNodeById(final Long id) {
        return new Function<GraphDatabaseService, Optional<Node>>() {
            @Override
            public Optional<Node> apply(GraphDatabaseService graphDb) {
                return fromNullable(graphDb.getNodeById(id));
            }
        };
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
        return new Function<Map<String, Object>, Node>() {
            @Override
            public Node apply(Map<String, Object> map) {
                return (Node) map.get(column);
            }
        };
    }

    public static <T> Function<GraphDatabaseService, Collection<T>> queryAll(final String query, final Function<Map<String, Object>, T> dataExtractor) {
        return new Function<GraphDatabaseService, Collection<T>>() {
            @Override
            public Collection<T> apply(GraphDatabaseService graphDb) {
                ExecutionEngine engine = new ExecutionEngine(graphDb);

                return from(engine.execute(query))
                        .transform(dataExtractor)
                        .toList();
            }
        };
    }

    public static Function<GraphDatabaseService, Node> findOr(final Label label, final String propertyKey, final String value, final Function<GraphDatabaseService, Node> or) {
        return new Function<GraphDatabaseService, Node>() {
            @Override
            public Node apply(GraphDatabaseService graphDb) {
                return from(graphDb.findNodesByLabelAndProperty(label, propertyKey, value)).first().or(or.apply(graphDb));
            }
        };
    }

    public static Function<Node, Node> addRelation(final Node endNode, final Direction direction, final Relations relations) {
        return new Function<Node, Node>() {
            @Override
            public Node apply(Node xebianNode) {
                boolean relationExists = from(xebianNode.getRelationships(direction, relations))
                        .transform(toEndNode)
                        .anyMatch(equalTo(endNode));

                if (!relationExists) {
                    xebianNode.createRelationshipTo(endNode, relations);
                }

                return xebianNode;
            }

        };
    }
}