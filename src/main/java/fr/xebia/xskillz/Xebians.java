package fr.xebia.xskillz;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fr.xebia.xskillz.Database.queryNodeById;
import static fr.xebia.xskillz.Functions.stream;
import static fr.xebia.xskillz.Labels.XEBIAN;
import static fr.xebia.xskillz.Relations.toEndNode;
import static org.neo4j.graphdb.Direction.OUTGOING;

public abstract class Xebians {

    public static Function<Node, Xebian> fromNode = new Function<Node, Xebian>() {
        @Override
        public Xebian apply(Node node) {
            Set<Skill> skills = stream(node.getRelationships(OUTGOING, Relations.KNOWS))
                    .map(toEndNode)
                    .map(Skills.fromNode)
                    .collect(Collectors.toSet());

            return new Xebian(node.getId(), node.getProperty(Xebian.Properties.EMAIL).toString(), skills);
        }
    };

    public static Function<GraphDatabaseService, Node> create(final String email) {
        return graphDb -> {
            Node node = graphDb.createNode(XEBIAN);
            node.setProperty(Xebian.Properties.EMAIL, email);
            return node;
        };
    }

    public static Function<GraphDatabaseService, Optional<Node>> findById(long id) {
        return queryNodeById(id);
    }
}
