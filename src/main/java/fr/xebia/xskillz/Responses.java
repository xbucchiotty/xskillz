package fr.xebia.xskillz;

import org.neo4j.graphdb.Node;

import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.function.Function;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.seeOther;
import static javax.ws.rs.core.UriBuilder.fromResource;

public class Responses {

    public static <T> Function<Optional<T>, Response> ok() {
        return optionalT -> optionalT.map(t -> Response.ok(t).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    public static Function<Node, Response> redirectToNode() {
        return ((Function<Node, Long>) Node::getId)
                    .andThen(id -> seeOther(fromResource(XebianRepository.class).path(id.toString()).build()).build());
    }
}
