package fr.xebia.xskillz;

import com.google.common.collect.Iterables;
import org.neo4j.graphdb.*;

import java.util.*;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Iterables.concat;
import static java.util.Arrays.asList;

public class MockedNode implements Node {
    private final long id = new Random().nextLong();

    public boolean deleted = false;

    private Set<Label> labels = new HashSet<>();

    private Collection<Relationship> outgoingRelationships = new HashSet<>();
    private Collection<Relationship> ingoingRelationships = new HashSet<>();
    private Map<String, Object> properties = new HashMap<>();

    public MockedNode() {
    }

    public MockedNode(Label... labels) {
        this.labels.addAll(asList(labels));
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void delete() {
        deleted = true;
    }

    @Override
    public Iterable<Relationship> getRelationships() {
        return outgoingRelationships;
    }

    @Override
    public boolean hasRelationship() {
        return !outgoingRelationships.isEmpty();
    }

    @Override
    public Iterable<Relationship> getRelationships(final RelationshipType... relationshipTypes) {
        return from(allRelationships())
                .filter(TestHelpers.relationshipIsOfType(relationshipTypes));
    }

    @Override
    public Iterable<Relationship> getRelationships(Direction direction, RelationshipType... relationshipTypes) {
        return from(relationshipsFor(direction))
                .filter(TestHelpers.relationshipIsOfType(relationshipTypes));
    }

    @Override
    public boolean hasRelationship(RelationshipType... relationshipTypes) {
        return from(allRelationships())
                .anyMatch(TestHelpers.relationshipIsOfType(relationshipTypes));

    }

    @Override
    public boolean hasRelationship(Direction direction, RelationshipType... relationshipTypes) {
        return from(relationshipsFor(direction))
                .anyMatch(TestHelpers.relationshipIsOfType(relationshipTypes));
    }

    @Override
    public Iterable<Relationship> getRelationships(Direction direction) {
        return relationshipsFor(direction);
    }

    @Override
    public boolean hasRelationship(Direction direction) {
        return Iterables.isEmpty(getRelationships(direction));
    }

    @Override
    public Iterable<Relationship> getRelationships(RelationshipType relationshipType, Direction direction) {
        return getRelationships(direction, relationshipType);
    }

    @Override
    public boolean hasRelationship(RelationshipType relationshipType, Direction direction) {
        return Iterables.isEmpty(getRelationships(direction, relationshipType));
    }

    @Override
    public Relationship getSingleRelationship(RelationshipType relationshipType, Direction direction) {
        return from(getRelationships(direction, relationshipType)).first().orNull();
    }

    @Override
    public Relationship createRelationshipTo(final Node node, final RelationshipType relationshipType) {
        MockedRelationship relationship = new MockedRelationship(this, node, relationshipType);
        outgoingRelationships.add(relationship);
        return relationship;
    }

    @Override
    public Iterable<RelationshipType> getRelationshipTypes() {
        return from(allRelationships()).transform(TestHelpers.toType());
    }

    @Override
    public int getDegree() {
        return 0;
    }

    @Override
    public int getDegree(RelationshipType relationshipType) {
        return 0;
    }

    @Override
    public int getDegree(Direction direction) {
        return 0;
    }

    @Override
    public int getDegree(RelationshipType relationshipType, Direction direction) {
        return 0;
    }

    @Override
    public Traverser traverse(Traverser.Order order, StopEvaluator stopEvaluator, ReturnableEvaluator returnableEvaluator, RelationshipType relationshipType, Direction direction) {
        return null;
    }

    @Override
    public Traverser traverse(Traverser.Order order, StopEvaluator stopEvaluator, ReturnableEvaluator returnableEvaluator, RelationshipType relationshipType, Direction direction, RelationshipType relationshipType2, Direction direction2) {
        return null;
    }

    @Override
    public Traverser traverse(Traverser.Order order, StopEvaluator stopEvaluator, ReturnableEvaluator returnableEvaluator, Object... objects) {
        return null;
    }

    @Override
    public void addLabel(Label label) {
        labels.add(label);
    }

    @Override
    public void removeLabel(Label label) {
        labels.remove(label);
    }

    @Override
    public boolean hasLabel(Label label) {
        return labels.contains(label);
    }

    @Override
    public Iterable<Label> getLabels() {
        return labels;
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
        Object value = getProperty(s);
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

    private Iterable<Relationship> relationshipsFor(Direction direction) {
        final Iterable<Relationship> relations;

        switch (direction) {
            case INCOMING:
                relations = ingoingRelationships;
                break;
            case OUTGOING:
                relations = outgoingRelationships;
                break;
            case BOTH:
                relations = allRelationships();
                break;
            default:
                relations = asList();
        }

        return relations;
    }

    private Iterable<Relationship> allRelationships() {
        return concat(ingoingRelationships, outgoingRelationships);
    }

}
