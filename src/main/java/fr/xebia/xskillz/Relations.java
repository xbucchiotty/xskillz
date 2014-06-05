package fr.xebia.xskillz;

import com.google.common.base.Function;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

public enum Relations implements RelationshipType {
    KNOWS;


    public static Function<Relationship, Node> toEndNode = new Function<Relationship, Node>() {
        @Override
        public Node apply(Relationship relationship) {
            return relationship.getEndNode();
        }
    };
}
