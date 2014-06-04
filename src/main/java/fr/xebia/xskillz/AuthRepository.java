package fr.xebia.xskillz;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/auth")
public class AuthRepository {

    @GET
    public String randomAccount() {
        return "john@xebia.fr";
    }
}
