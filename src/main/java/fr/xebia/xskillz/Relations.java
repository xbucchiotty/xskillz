package fr.xebia.xskillz;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import java.util.function.Function;

public enum Relations implements RelationshipType {
    KNOWS;

    public static Function<Relationship, Node> toEndNode = Relationship::getEndNode;
}
