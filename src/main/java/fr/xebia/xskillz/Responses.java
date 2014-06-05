package fr.xebia.xskillz;

import com.google.common.base.Function;

import javax.ws.rs.core.Response;

public class Responses {

    public static <T> Function<T, Response> ok() {
        return new Function<T, Response>() {
            @Override
            public Response apply(T t) {
                return Response.ok(t).build();
            }
        };
    }

}
