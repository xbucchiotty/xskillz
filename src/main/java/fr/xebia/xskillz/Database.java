package fr.xebia.xskillz;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.xebia.xskillz.Functions.stream;

public abstract class Database {

    private Database() {

    }

    public static Function<Map<String, Object>, Node> extractNode(String column) {
        return row -> (Node) row.get(column);
    }

    public static Function<GraphDatabaseService, Stream<Map<String, Object>>> execute(String query) {
        return (GraphDatabaseService db) -> {
            ExecutionEngine executionEngine = new ExecutionEngine(db);

            return stream(executionEngine.execute(query));
        };
    }

    public static Function<Node, Relationship> addRelationshipTo(Node from, RelationshipType relationType) {
        return skillNode -> from.createRelationshipTo(skillNode, relationType);
    }
}