package fr.xebia.xskillz;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import org.neo4j.graphdb.*;

import java.util.Iterator;
import java.util.Set;

public class TestHelpers {
    public static <T> ResourceIterable<T> resourceIterableFrom(final Iterable<T> iterable) {

        return new ResourceIterable<T>() {

            @Override
            public ResourceIterator<T> iterator() {

                return new ResourceIterator<T>() {
                    final Iterator<T> iterator = iterable.iterator();

                    @Override
                    public void close() {
                    }

                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public T next() {
                        return iterator.next();
                    }
                };
            }
        };
    }

    public static Function<Relationship, RelationshipType> toType() {
        return new Function<Relationship, RelationshipType>() {
            @Override
            public RelationshipType apply(Relationship relationship) {
                return relationship.getType();
            }
        };
    }

    public static Predicate<Relationship> relationshipIsOfType(final RelationshipType... relationshipTypes) {
        return new Predicate<Relationship>() {
            final Set<RelationshipType> expectedTypes = ImmutableSet.copyOf(relationshipTypes);

            @Override
            public boolean apply(final Relationship relationship) {
                return expectedTypes.contains(relationship.getType());
            }
        };
    }

    public static Predicate<Relationship> hasEndNode(final Node node) {
        return new Predicate<Relationship>() {
            @Override
            public boolean apply(Relationship input) {
                return input.getEndNode().equals(node);
            }
        };
    }

    public static Predicate<Node> nodeId(final long l) {
        return new Predicate<Node>() {
            @Override
            public boolean apply(Node input) {
                return input.getId() == l;
            }
        };
    }

    public static Function<Node, Iterable<Relationship>> toRelations() {
        return new Function<Node, Iterable<Relationship>>() {
            @Override
            public Iterable<Relationship> apply(Node node) {
                return node.getRelationships();
            }
        };
    }

    public static Predicate<Relationship> relationshipId(final long l) {
        return new Predicate<Relationship>() {
            @Override
            public boolean apply(Relationship relationship) {
                return relationship.getId() == l;
            }
        };
    }

    public static Predicate<Node> nodeHasLabel(final Label label) {
        return new Predicate<Node>() {
            @Override
            public boolean apply(Node input) {
                return input.hasLabel(label);
            }
        };
    }

    public static Predicate<Node> hasProperty(final String s, final Object o) {
        return new Predicate<Node>() {
            @Override
            public boolean apply(Node input) {
                return input.getProperty(s, o) != null;
            }
        };
    }
}
