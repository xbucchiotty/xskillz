package fr.xebia.xskillz;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.util.Set;

import static com.google.common.collect.FluentIterable.from;
import static fr.xebia.xskillz.Database.queryNodeById;
import static fr.xebia.xskillz.Labels.XEBIAN;
import static fr.xebia.xskillz.Relations.toEndNode;
import static org.neo4j.graphdb.Direction.OUTGOING;

public abstract class Xebians {

    public static Function<Node, Xebian> fromNode = new Function<Node, Xebian>() {
        @Override
        public Xebian apply(Node node) {
            Set<Skill> skills = from(node.getRelationships(OUTGOING, Relations.KNOWS))
                    .transform(toEndNode)
                    .transform(Skills.fromNode)
                    .toSet();

            return new Xebian(node.getId(), node.getProperty(Xebian.Properties.EMAIL).toString(),skills);
        }
    };

    public static Function<GraphDatabaseService, Node> create(final String email) {
        return new Function<GraphDatabaseService, Node>() {
            @Override
            public Node apply(GraphDatabaseService graphDb) {
                Node node = graphDb.createNode(XEBIAN);
                node.setProperty(Xebian.Properties.EMAIL, email);
                return node;
            }
        };
    }

    public static Function<GraphDatabaseService, Optional<Node>> findById(long id) {
        return queryNodeById(id);
    }
}
