package uk.org.nbn.nbnv.api.rest.resources;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.authentication.InvalidCredentialsException;
import uk.org.nbn.nbnv.api.authentication.Token;
import uk.org.nbn.nbnv.api.authentication.TokenAuthenticator;
import uk.org.nbn.nbnv.api.dao.mappers.UserAuthenticationMapper;
import uk.org.nbn.nbnv.api.dao.mappers.UserMapper;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;

/**
 *
 * @author Chris Johnson
 */
@Component
@Path("/user")
public class UserResource {  
    private static final String STRING_ENCODING = "UTF-8";
    
    private final int tokenTTL;
    private final String tokenCookieKey;
    private final String domain;
    private final MessageDigest sha1, md5;
    
    @Autowired TokenAuthenticator tokenAuth;
    @Autowired UserMapper userMapper;
    @Autowired UserAuthenticationMapper userAuthenticationMapper;
    
    @Autowired public UserResource(Properties properties) throws NoSuchAlgorithmException {
        tokenTTL = Integer.parseInt(properties.getProperty("sso_token_default_ttl"));
        tokenCookieKey = properties.getProperty("sso_token_key");
        domain = properties.getProperty("sso_token_domain");
        sha1 = MessageDigest.getInstance("SHA-1");
        md5 = MessageDigest.getInstance("MD5");
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getDetails(@TokenUser User user) {
        return user;
    }

    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTokenCookie(
            @QueryParam("username")  String username, 
            @QueryParam("password") String password
        ) throws InvalidCredentialsException {
        Token token = tokenAuth.generateToken(username, password, tokenTTL);
        return Response.ok("success")
           .cookie(new NewCookie(
                tokenCookieKey, 
                Base64.encodeBase64URLSafeString(token.getBytes()),
                "/", domain, "authentication token",
                tokenTTL/1000, false
            ))
           .build();
    }
    
    @GET
    @Path("/changePassword")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setUserPassword(
            @TokenUser(allowPublic=false) User user,
            @QueryParam("password") String password
        ) throws UnsupportedEncodingException {
        byte[] passwordHashSHA1 = sha1.digest(password.getBytes(STRING_ENCODING));
        byte[] md5HashSHA1 = sha1.digest(md5.digest(password.getBytes(STRING_ENCODING)));
    
        userAuthenticationMapper.setUserPassword(user, passwordHashSHA1, md5HashSHA1);
        return Response.ok("success").build();
    }
    
    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response destroyTokenCookie() {
        return Response.ok("loggedout")
            .cookie(new NewCookie(tokenCookieKey, null, "/", domain, null, 0 , false))
            .build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserByID(@PathParam("id") int id) {
        return userMapper.getUser(id);
    }
}
