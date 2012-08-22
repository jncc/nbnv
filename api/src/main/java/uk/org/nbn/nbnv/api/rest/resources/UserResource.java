package uk.org.nbn.nbnv.api.rest.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.authentication.InvalidCredentialsException;
import uk.org.nbn.nbnv.api.authentication.Token;
import uk.org.nbn.nbnv.api.authentication.TokenAuthenticator;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.authentication.TokenUser;

/**
 *
 * @author Chris Johnson
 */
@Component
@Path("/user")
public class UserResource {
    private static final int DEFAULT_TOKEN_TTL = 2 * 7 * 24 * 60 * 60 * 1000;//2 weeks
    public static final String TOKEN_COOKIE_KEY = "nbn.token_key";
    
    @Autowired TokenAuthenticator tokenAuth;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getDetails(@TokenUser User user) {
        return user;
    }
    
    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTokenCookie(
            @QueryParam("username") String username, 
            @QueryParam("password")String password,
            @Context HttpServletRequest request
        ) throws InvalidCredentialsException {
        Token token = tokenAuth.generateToken(username, password, DEFAULT_TOKEN_TTL);
        return Response.ok("success")
           .cookie(new NewCookie(
               TOKEN_COOKIE_KEY, 
               Base64.encodeBase64URLSafeString(token.getBytes()),
                "/",
                request.getServerName(),
                "authentication token",
                DEFAULT_TOKEN_TTL/1000,
                false
            ))
           .build();
    }
    
    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response destroyTokenCookie(@Context HttpServletRequest request) {
        return Response.ok("loggedout")
            .cookie(new NewCookie(TOKEN_COOKIE_KEY, null, "/", request.getServerName(), null, 0 , false))
            .build();
    }
}
