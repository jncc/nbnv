package uk.org.nbn.nbnv.api.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.authentication.InvalidCredentialsException;
import uk.org.nbn.nbnv.api.authentication.Token;
import uk.org.nbn.nbnv.api.authentication.TokenAuthenticator;

@Component
@Path("/token")
public class UserTokenResource {
    @Autowired TokenAuthenticator tokenAuth;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Token getToken(
            @QueryParam("username") String username, 
            @QueryParam("password")String password
            ) throws InvalidCredentialsException {
        return tokenAuth.generateToken(username, password, 1000);
    }
}
