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
import uk.org.nbn.nbnv.api.rest.providers.TokenUserProvider;

@Component
@Path("/token")
public class UserTokenResource {
    private static final int DEFAULT_TOKEN_TTL = 30000;
    @Autowired TokenAuthenticator tokenAuth;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Token getToken(
            @QueryParam("username") String username, 
            @QueryParam("password")String password
            ) throws InvalidCredentialsException {
        return tokenAuth.generateToken(username, password, DEFAULT_TOKEN_TTL);
    }
    
    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaxonNavigationGroup(
            @QueryParam("username") String username, 
            @QueryParam("password")String password,
            @Context HttpServletRequest request
        ) throws InvalidCredentialsException {
        System.out.println();
        Token token = tokenAuth.generateToken(username, password, DEFAULT_TOKEN_TTL);
        return Response.ok()
           .cookie(new NewCookie(
               TokenUserProvider.TOKEN_COOKIE_KEY, 
               Base64.encodeBase64URLSafeString(token.getBytes()),
                "/",
                request.getServerName(),
                "authentication token",
                DEFAULT_TOKEN_TTL/1000,
                false
            ))
           .build();
    }
}
