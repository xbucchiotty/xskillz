package fr.xebia.xskillz;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MockedRelationship implements Relationship {

    private final Node startNode;
    private final Node endNode;
    private final RelationshipType relationshipType;
    private Map<String, Object> properties;

    private long id;

    public MockedRelationship(Node startNode, Node endNode, RelationshipType relationshipType) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.relationshipType = relationshipType;
        this.properties = new HashMap<>();
        this.id = new Random().nextLong();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void delete() {

    }

    @Override
    public Node getStartNode() {
        return startNode;
    }

    @Override
    public Node getEndNode() {
        return endNode;
    }

    @Override
    public Node getOtherNode(Node node) {
        return null;
    }

    @Override
    public Node[] getNodes() {
        return new Node[]{getStartNode(), getEndNode()};
    }

    @Override
    public RelationshipType getType() {
        return relationshipType;
    }

    @Override
    public boolean isType(RelationshipType relationshipType) {
        return getType().equals(relationshipType);
    }

    @Override
    public GraphDatabaseService getGraphDatabase() {
        return null;
    }

    @Override
    public boolean hasProperty(String s) {
        return properties.containsKey(s);
    }

    @Override
    public Object getProperty(String s) {
        return properties.get(s);
    }

    @Override
    public Object getProperty(String s, Object o) {
        Object value = properties.get(s);
        return value.equals(o) ? o : null;
    }

    @Override
    public void setProperty(String s, Object o) {
        properties.put(s, o);
    }

    @Override
    public Object removeProperty(String s) {
        return properties.remove(s);
    }

    @Override
    public Iterable<String> getPropertyKeys() {
        return properties.keySet();
    }
}
