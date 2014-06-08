package fr.xebia.xskillz;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import static fr.xebia.xskillz.Labels.SKILL;
import static fr.xebia.xskillz.Labels.XEBIAN;
import static fr.xebia.xskillz.Skills.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.neo4j.graphdb.Direction.OUTGOING;

public class SkillsTest {

    private GraphDatabaseService graphDatabaseService = new MockedGraphDb();

    @Test
    public void should_build_skill_from_node() {
        String name = "java";

        Node skillNode = create(name).apply(graphDatabaseService);

        assertThat(skillNode.getProperty(Skill.Properties.NAME)).isEqualTo(name);
    }

    @Test
    public void should_build_node_from_skill() {
        MockedNode node = new MockedNode(SKILL);
        node.setProperty(Skill.Properties.NAME, "java");

        Skill skill = Skills.fromNode.apply(node);

        assertThat(skill.getName()).isEqualTo("java");
    }


    @Test
    public void should_be_able_to_add_skill_as_outgoing_relation_to_a_node() {
        MockedNode skillNode = new MockedNode(SKILL);
        MockedNode startNode = new MockedNode(XEBIAN);

        addKnown(skillNode).apply(startNode);

        assertThat(startNode.getRelationships(Relations.KNOWS, OUTGOING)).isNotNull();
    }

    @Test
    public void should_find_skill_when_already_exists() {
        Node createdSkill = create("java").apply(graphDatabaseService);

        Node result = findOrCreate("java").apply(graphDatabaseService);

        assertThat(result).isEqualTo(createdSkill);
    }

    @Test
    public void should_create_skill_when_not_yet_created() {
        Node result = findOrCreate("java").apply(graphDatabaseService);

        assertThat(result.getProperty(Skill.Properties.NAME)).isEqualTo("java");
    }

}