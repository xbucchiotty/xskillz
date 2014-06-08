package fr.xebia.xskillz;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;

import javax.ws.rs.core.Response;

public class ResponseAssert extends AbstractAssert<ResponseAssert, Response> {

    ResponseAssert(Response actual) {
        super(actual, ResponseAssert.class);
    }


    public static ResponseAssert assertThatResponse(Response actual) {
        return new ResponseAssert(actual);
    }

    public ResponseAssert hasStatusCode(Response.Status status) {
        Assertions.assertThat(actual.getStatus()).isEqualTo(status.getStatusCode());
        return this;
    }

    public ResponseAssert hasEntity(Object anEntity) {
        Assertions.assertThat(actual.getEntity()).isEqualTo(anEntity);
        return this;
    }
}
