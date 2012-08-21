package uk.org.nbn.nbnv.api.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.authentication.TokenUser;

/**
 *
 * @author Chris Johnson
 */
@Component
@Path("/user")
public class UserResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getDetails(@TokenUser(allowPublic=false) User user) {
        return user;
    }
}
