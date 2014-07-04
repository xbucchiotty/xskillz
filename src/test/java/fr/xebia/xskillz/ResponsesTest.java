package fr.xebia.xskillz;

import org.junit.Test;
import org.neo4j.graphdb.Node;

import javax.ws.rs.core.Response;
import java.util.Optional;

import static fr.xebia.xskillz.ResponseAssert.assertThatResponse;
import static javax.ws.rs.core.Response.Status.*;

public class ResponsesTest {

    private Object anEntity = "anEntity";

    @Test
    public void ok_should_build_ok_response_with_a_present_entity() {
        Response response = Responses.ok().apply(Optional.of(anEntity));

        assertThatResponse(response)
                .hasStatusCode(OK)
                .hasEntity(anEntity);
    }

    @Test
    public void ok_should_build_not_found_response_with_an_empty_entity() {
        Response response = Responses.ok().apply(Optional.empty());

        assertThatResponse(response)
                .hasStatusCode(NOT_FOUND);
    }

    @Test
    public void redirectToNode_should_redirect_to_resource_with_node_id_as_path_param() {
        Node node = new MockedNode();

        Response response = Responses.redirectToNode(XebianRepository.class).apply(node);

        assertThatResponse(response).hasStatusCode(SEE_OTHER);
    }
}
