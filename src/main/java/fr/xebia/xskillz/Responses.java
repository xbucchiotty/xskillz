package fr.xebia.xskillz;

import javax.ws.rs.core.Response;
import java.util.function.Function;

public class Responses {

    public static <T> Function<T, Response> ok() {
        return t -> Response.ok(t).build();
    }

}
