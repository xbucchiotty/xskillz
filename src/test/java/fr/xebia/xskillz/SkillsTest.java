package fr.xebia.xskillz;

import org.junit.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.IOException;
import java.util.Optional;

import static fr.xebia.xskillz.Labels.SKILL;
import static fr.xebia.xskillz.Skills.*;
import static java.nio.file.Files.createTempDirectory;
import static org.fest.assertions.api.Assertions.assertThat;

public class SkillsTest {

    private GraphDatabaseService mockedDb = new MockedGraphDb();

    private static GraphDatabaseService db;
    private Transaction transaction;

    @Test
    public void fromNode_should_build_skill_from_node() {
        MockedNode node = new MockedNode(SKILL);
        node.setProperty(Skill.Properties.NAME, "java");

        Skill skill = Skills.fromNode.apply(node);

        assertThat(skill.getName()).isEqualTo("java");
    }

    @Test
    public void create_should_build_node_from_skill() {
        String name = "java";

        Node skillNode = create(name).apply(mockedDb);

        assertThat(skillNode.getProperty(Skill.Properties.NAME)).isEqualTo(name);
    }

    @Test
    public void findByName_should_find_existing_skill() {
        Node createdSkill = create("java").apply(db);

        Optional<Node> result = findByName("java").apply(db);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(createdSkill.getId());
    }

    @Test
    public void findByName_should_not_find_non_existing_skill() {
        Optional<Node> result = findByName("java").apply(db);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void findOrCreate_should_find_skill_when_already_exists() {
        Node createdSkill = create("java").apply(db);

        Node result = findOrCreate("java").apply(db);

        assertThat(result).isEqualTo(createdSkill);
    }

    @Test
    public void findOrCreate_should_create_skill_when_not_yet_created() {
        Node result = findOrCreate("java").apply(db);

        assertThat(result.getProperty(Skill.Properties.NAME)).isEqualTo("java");
    }

    @Before
    public void setupTransaction() {
        transaction = db.beginTx();
    }

    @After
    public void closeTransaction() {
        if (transaction != null) {
            transaction.failure();
            transaction.close();
        }
    }

    @BeforeClass
    public static void setUp() throws IOException {
        db = new GraphDatabaseFactory().newEmbeddedDatabase(createTempDirectory("xsillz-db").toString());
    }

    @AfterClass
    public static void tearDown() {
        if (db != null) {
            db.shutdown();
        }
    }
}