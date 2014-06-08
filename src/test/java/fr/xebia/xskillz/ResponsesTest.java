package fr.xebia.xskillz;

import org.junit.Test;

import javax.ws.rs.core.Response;

import static fr.xebia.xskillz.ResponseAssert.assertThatResponse;
import static javax.ws.rs.core.Response.Status.OK;

public class ResponsesTest {

    private Object anEntity = "anEntity";

    @Test
    public void should_build_ok_response_with_entity() {
        Response response = Responses.ok().apply(anEntity);

        assertThatResponse(response)
                .hasStatusCode(OK)
                .hasEntity(anEntity);
    }
}
