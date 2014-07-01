package fr.xebia.xskillz;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.util.function.Function;

import static fr.xebia.xskillz.Database.addRelation;
import static fr.xebia.xskillz.Database.findOr;
import static fr.xebia.xskillz.Labels.SKILL;
import static fr.xebia.xskillz.Relations.KNOWS;
import static org.neo4j.graphdb.Direction.OUTGOING;

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

    public static Function<Node, Node> addKnown(Node skill) {
        return addRelation(skill, OUTGOING, KNOWS);
    }

    public static Function<GraphDatabaseService, Node> findOrCreate(String skillName) {
        return findOr(SKILL, Skill.Properties.NAME, skillName, create(skillName));
    }
}
