package fr.xebia.xskillz;

import com.google.inject.util.Providers;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import javax.ws.rs.core.Response;

import static fr.xebia.xskillz.Relations.KNOWS;
import static fr.xebia.xskillz.ResponseAssert.assertThatResponse;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.fest.assertions.api.Assertions.assertThat;

public class XebianRepositoryTest {

    public static final int UNKOWN_XEBIAN_ID = 42;
    public static final String A_KNOWN_SKILL = "neo4j";
    public static final String AN_UNKNOWN_SKILL = "C++";

    private GraphDatabaseService graphDb = new MockedGraphDb();

    private XebianRepository repository;

    private String anEmail = "anEmail@xebia.fr";

    @Test
    public void findById_should_find_xebian_by_id_and_returns_it_as_ok() {
        final Node knownSkillNode = aSkillNode(A_KNOWN_SKILL);
        aSkillNode(AN_UNKNOWN_SKILL);
        final Node xebianNode = aXebianNode(anEmail, knownSkillNode);

        Response result = repository.findById(xebianNode.getId());

        assertThatResponse(result)
                .hasStatusCode(OK)
                .isWithEntityMatching(entity -> {
                    assertThat(entity).isInstanceOf(Xebian.class);

                    Xebian expected = new Xebian(xebianNode.getId(), anEmail);
                    expected.addSkill(Skills.fromNode.apply(knownSkillNode));
                    assertThat(entity).isEqualsToByComparingFields(expected);
                    return null;
                });
    }

    @Test
    public void findById_should_response_not_found_if_no_xebian_with_given_id() {
        Response result = repository.findById(UNKOWN_XEBIAN_ID);

        assertThatResponse(result).hasStatusCode(NOT_FOUND);
    }

    @Before
    public void setUp() throws Exception {
        repository = new XebianRepository(Providers.of(graphDb));
    }

    public Node aXebianNode(String email, Node... skills) {
        Node xebian = Xebians.create(email).apply(graphDb);

        for (Node skill : skills) {
            Database.createRelationFrom(xebian, KNOWS).apply(skill);
        }

        return xebian;
    }

    public Node aSkillNode(String skillName) {
        return Skills.create(skillName).apply(graphDb);
    }

}
