package fr.xebia.xskillz;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.xebia.xskillz.Database.execute;
import static fr.xebia.xskillz.Functions.liftO;
import static fr.xebia.xskillz.Labels.SKILL;

public abstract class Skills {

    public static Function<Node, Skill> fromNode = new Function<Node, Skill>() {
        @Override
        public Skill apply(Node node) {
            return new Skill(node.getId(), node.getProperty(Skill.Properties.NAME).toString());
        }
    };

    public static Function<GraphDatabaseService, Node> create(final String skillName) {
        return graphDb -> {
            Node node = graphDb.createNode(SKILL);
            node.setProperty(Skill.Properties.NAME, skillName);
            return node;
        };
    }

    public static Function<GraphDatabaseService, Optional<Node>> findByName(String skillName) {
        return execute("MATCH (n:" + SKILL + ") WHERE n.name=\"" + skillName + "\" RETURN n")
                .andThen(Stream::findFirst)
                .andThen(liftO(Database.extractNode("n")));
    }

    public static Function<GraphDatabaseService, Node> findOrCreate(String skillName) {
        return new Function<GraphDatabaseService, Node>() {
            @Override
            public Node apply(GraphDatabaseService db) {
                Optional<Node> optionalSkillNode = findByName(skillName).apply(db);

                return optionalSkillNode.orElseGet(() -> create(skillName).apply(db));
            }
        };
    }
}
