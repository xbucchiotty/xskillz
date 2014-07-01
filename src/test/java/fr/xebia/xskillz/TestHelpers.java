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

                    @Override
                    public void remove() {
                    }
                };
            }
        };
    }

    public static Function<Relationship, RelationshipType> toType() {
        return Relationship::getType;
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

    public static Predicate<Node> nodeId(final long l) {
        return input -> input.getId() == l;
    }

    public static Function<Node, Iterable<Relationship>> toRelations() {
        return Node::getRelationships;
    }

    public static Predicate<Relationship> relationshipId(final long l) {
        return relationship -> relationship.getId() == l;
    }

    public static Predicate<Node> nodeHasLabel(final Label label) {
        return input -> input.hasLabel(label);
    }

    public static Predicate<Node> hasProperty(final String s, final Object o) {
        return input -> input.getProperty(s, o) != null;
    }
}
