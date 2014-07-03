package fr.xebia.xskillz;

import org.junit.Test;

import javax.ws.rs.core.Response;

import java.util.Optional;

import static fr.xebia.xskillz.ResponseAssert.assertThatResponse;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

public class ResponsesTest {

    private Object anEntity = "anEntity";

    @Test
    public void should_build_ok_response_with_a_present_entity() {
        Response response = Responses.ok().apply(Optional.of(anEntity));

        assertThatResponse(response)
                .hasStatusCode(OK)
                .hasEntity(anEntity);
    }

    @Test
    public void should_build_not_found_response_with_an_empty_entity() {
        Response response = Responses.ok().apply(Optional.empty());

        assertThatResponse(response)
                .hasStatusCode(NOT_FOUND);
    }
}
