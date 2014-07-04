package fr.xebia.xskillz;

import com.google.inject.util.Providers;
import org.junit.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static fr.xebia.xskillz.Relations.KNOWS;
import static fr.xebia.xskillz.ResponseAssert.assertThatResponse;
import static java.nio.file.Files.createTempDirectory;
import static javax.ws.rs.core.Response.Status.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class XebianRepositoryTest {

    public static final String NEO4J_SKILL = "neo4j";
    public static final String AN_UNKNOWN_SKILL = "C++";
    public static final String EMPTY_SKILL_QUERY = "";
    public static final String EMPTY_EMAIL_QUERY = "";

    private static GraphDatabaseService db;
    private org.neo4j.graphdb.Transaction tx;

    private static XebianRepository repository;

    @Test
    public void findById_should_find_xebian_by_id_and_returns_it_as_ok() {
        final Node neo4JNode = Skills.findOrCreate(NEO4J_SKILL).apply(db);
        Skills.findOrCreate(AN_UNKNOWN_SKILL).apply(db);
        String anEmail = generateEmail();
        final Node xebianNode = aXebianNode(anEmail, neo4JNode);
        Xebian expected = new Xebian(xebianNode.getId(), anEmail);
        expected.addSkill(Skills.fromNode.apply(neo4JNode));

        Response result = repository.findById(xebianNode.getId());

        assertThatResponse(result)
                .hasStatusCode(OK)
                .isWithEntityMatching(entity -> {
                    assertThat(entity).isInstanceOf(Xebian.class);
                    assertThat(entity).isEqualsToByComparingFields(expected);
                    return null;
                });
    }

    @Test
    public void findById_should_reply_not_found_when_xebian_not_found() {
        final Node neo4JNode = Skills.findOrCreate(NEO4J_SKILL).apply(db);

        Response result = repository.findById(neo4JNode.getId());

        assertThatResponse(result).hasStatusCode(NOT_FOUND);
    }

    @Test
    public void createAXebian_should_create_a_new_xebian_from_email() {
        String email = generateEmail();
        Response response = repository.createAXebian(email);

        assertThatResponse(response).hasStatusCode(CREATED);

        Optional<Node> xebianFromDatabase = Xebians.findByEmail(email).apply(db);

        assertThat(xebianFromDatabase.isPresent()).isTrue();
    }

    @Test
    public void createAXebian_should_redirect_when_email_already_exists() {
        String email = generateEmail();
        Xebians.create(email).apply(db);
        Response response = repository.createAXebian(email);

        assertThatResponse(response).hasStatusCode(SEE_OTHER);
    }

    @Test
    public void searchForXebians_should_list_all_xebians_with_skills_from_database_when_no_query_params_are_given() {
        String email1 = generateEmail();
        String email2 = generateEmail();
        Node xebian1 = Xebians.create(email1).apply(db);
        Xebians.create(email2).apply(db);
        Node neo4JSkill = Skills.findOrCreate(NEO4J_SKILL).apply(db);

        Database.addRelationshipTo(xebian1, KNOWS).apply(neo4JSkill);

        Collection<Xebian> xebians = repository.searchForXebians(EMPTY_SKILL_QUERY, EMPTY_EMAIL_QUERY);

        assertThat(xebians.size()).isGreaterThanOrEqualTo(2);
        assertThat(xebians.stream().anyMatch(xebian -> xebian.getEmail().equals(email1))).isTrue();
        assertThat(xebians.stream().anyMatch(xebian -> xebian.getEmail().equals(email2))).isTrue();
    }

    @Test
    public void searchForXebians_should_filter_xebians_by_email() {
        String email1 = generateEmail();
        String email2 = generateEmail();
        Node xebian1 = Xebians.create(email1).apply(db);
        Xebians.create(email2).apply(db);
        Node neo4JSkill = Skills.findOrCreate(NEO4J_SKILL).apply(db);

        Database.addRelationshipTo(xebian1, KNOWS).apply(neo4JSkill);

        Collection<Xebian> xebians = repository.searchForXebians(EMPTY_SKILL_QUERY, email1);

        assertThat(xebians).hasSize(1);
        assertThat(xebians.iterator().next().getEmail()).isEqualTo(email1);
    }

    @Test
    public void searchForXebians_should_filter_xebians_by_skill() {
        String email1 = generateEmail();
        String email2 = generateEmail();
        Node xebian1 = Xebians.create(email1).apply(db);
        Xebians.create(email2).apply(db);
        Node neo4JSkill = Skills.findOrCreate(NEO4J_SKILL).apply(db);

        Database.addRelationshipTo(xebian1, KNOWS).apply(neo4JSkill);

        Collection<Xebian> xebians = repository.searchForXebians(NEO4J_SKILL, EMPTY_EMAIL_QUERY);

        assertThat(xebians).hasSize(1);
        assertThat(xebians.iterator().next().getEmail()).isEqualTo(email1);
    }

    @Test
    public void addSkill_should_add_skill_to_existing_xebian_when_skill_exists() {
        String email1 = generateEmail();
        Node xebianNode = Xebians.create(email1).apply(db);
        Skills.create(NEO4J_SKILL).apply(db);

        Response response = repository.addSkill(xebianNode.getId(), NEO4J_SKILL);

        assertThatResponse(response)
                .hasStatusCode(OK)
                .isWithEntityMatching(entity -> {
                    assertThat(entity).isInstanceOf(Xebian.class);
                    Xebian xebian = (Xebian) entity;
                    assertThat(xebian.getId().getValue()).isEqualTo(xebianNode.getId());
                    assertThat(xebian.getSkills().iterator().next().getName()).isEqualTo(NEO4J_SKILL);
                    return null;
                });

    }

    @Test
    public void addSkill_should_add_skill_to_existing_xebian_when_skill_does_not_exists() {
        String email1 = generateEmail();
        Node xebianNode = Xebians.create(email1).apply(db);
        String aSKillName = generateSkillName();

        Response response = repository.addSkill(xebianNode.getId(), aSKillName);

        assertThatResponse(response)
                .hasStatusCode(OK)
                .isWithEntityMatching(entity -> {
                    assertThat(entity).isInstanceOf(Xebian.class);
                    Xebian xebian = (Xebian) entity;
                    assertThat(xebian.getId().getValue()).isEqualTo(xebianNode.getId());
                    assertThat(xebian.getSkills().iterator().next().getName()).isEqualTo(aSKillName);
                    return null;
                });

    }

    @Test
    public void addSkill_should_reply_not_found_when_xebian_does_not_exist() {
        final Node neo4JNode = Skills.findOrCreate(NEO4J_SKILL).apply(db);

        Response result = repository.addSkill(neo4JNode.getId(), NEO4J_SKILL);

        assertThatResponse(result).hasStatusCode(NOT_FOUND);
    }

    public Node aXebianNode(String email, Node... skills) {
        Node xebian = Xebians.create(email).apply(db);

        for (Node skill : skills) {
            Database.addRelationshipTo(xebian, KNOWS).apply(skill);
        }

        return xebian;
    }

    public String generateSkillName() {
        return UUID.randomUUID().toString();
    }

    public String generateEmail() {
        return "test-" + UUID.randomUUID().getMostSignificantBits() + "@xebia.fr";
    }

    @Before
    public void setupTransaction() {
        tx = db.beginTx();
    }

    @After
    public void closeTransaction() {
        if (tx != null) {
            tx.failure();
            tx.close();
        }
    }

    @BeforeClass
    public static void setUp() throws IOException {
        db = new GraphDatabaseFactory().newEmbeddedDatabase(createTempDirectory("xsillz-db").toString());
        repository = new XebianRepository(Providers.of(db));
    }

    @AfterClass
    public static void tearDown() {
        if (db != null) {
            db.shutdown();
        }
    }

}
