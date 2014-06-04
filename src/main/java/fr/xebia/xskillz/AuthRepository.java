package fr.xebia.xskillz;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import static com.google.common.collect.FluentIterable.from;

@Path("/auth")
@Singleton
public class AuthRepository {

    private final XebianRepository xebiansRepository;

    @Inject
    public AuthRepository(XebianRepository xebians) {
        this.xebiansRepository = xebians;
    }

    @GET
    public String randomAccount() {
        return from(xebiansRepository.allXebians()).first().get().getId().getEmail();
    }
}
