package fr.xebia.xskillz;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.xebia.xskillz.Database.execute;
import static fr.xebia.xskillz.Database.extractNode;
import static fr.xebia.xskillz.Functions.*;
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
        return execute("START n=node(" + id + ") RETURN n")
                .andThen(Stream::findFirst)
                .andThen(liftO(extractNode("n")));
    }

    public static Function<GraphDatabaseService, Stream<Xebian>> queryAll() {
        return execute("MATCH (n:" + XEBIAN + ") RETURN n")
                .andThen(liftS(extractNode("n").andThen(fromNode)));
    }

    public static Function<GraphDatabaseService, Optional<Node>> findByEmail(String email) {
        return execute("MATCH (n:" + XEBIAN + ") WHERE n.email=\"" + email + "\" RETURN n")
                .andThen(Stream::findFirst)
                .andThen(liftO(extractNode("n")));
    }

}
