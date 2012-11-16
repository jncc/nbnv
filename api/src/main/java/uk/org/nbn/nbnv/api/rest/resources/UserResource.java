package uk.org.nbn.nbnv.api.rest.resources;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.authentication.ExpiredTokenException;
import uk.org.nbn.nbnv.api.authentication.InvalidCredentialsException;
import uk.org.nbn.nbnv.api.authentication.InvalidTokenException;
import uk.org.nbn.nbnv.api.authentication.Token;
import uk.org.nbn.nbnv.api.authentication.TokenAuthenticator;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.UserAuthenticationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.UserMapper;
import uk.org.nbn.nbnv.api.mail.TemplateMailer;
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
    @Autowired OperationalUserMapper oUserMapper;
    @Autowired UserAuthenticationMapper userAuthenticationMapper;
    @Autowired TemplateMailer mailer;
    
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
        ) throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException {
        Token token = tokenAuth.generateToken(username, password, tokenTTL);
        
        Map<String, Object> toReturn = new HashMap<String, Object>();
        toReturn.put("success", true);
        toReturn.put("user", tokenAuth.getUser(token));
        toReturn.put("token", token.getBytes());
        
        return Response.ok(toReturn)
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
    
        oUserMapper.setUserPassword(user, passwordHashSHA1, md5HashSHA1);
        return Response.ok("success").build();
    }
    
    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response destroyTokenCookie() {
        Map<String,Object> toReturn = new HashMap<String, Object>();
        toReturn.put("success", true);
        toReturn.put("user", User.PUBLIC_USER);
        
        return Response.ok(toReturn)
            .cookie(new NewCookie(tokenCookieKey, null, "/", domain, null, 0 , false))
            .build();
    }
    
    @GET
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON) 
    public Response registerNewUser(
            @QueryParam("username")  String username, 
            @QueryParam("forename")  String forename, 
            @QueryParam("surname")  String surname, 
            @QueryParam("phone")  String phone, 
            @QueryParam("email")  String email, 
            @QueryParam("password") String password
            ) throws UnsupportedEncodingException, IOException, TemplateException  {
        byte[] passwordHashSHA1 = sha1.digest(password.getBytes(STRING_ENCODING));
        byte[] md5HashSHA1 = sha1.digest(md5.digest(password.getBytes(STRING_ENCODING)));
        String activationKey = RandomStringUtils.randomAlphanumeric(12); //generate a random one off activation key
        //save that key in the database
        oUserMapper.registerNewUser(    username, forename, surname, phone,     
                                        email, Calendar.getInstance().getTime(), 
                                        activationKey, passwordHashSHA1, md5HashSHA1);
        //email the user with the activation key
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("name", forename);
        message.put("activationKey", activationKey);
        message.put("username", username);
        mailer.send("activation.ftl", email, "NBN Gateway: Please activate your account", message);
        
        Map<String, Object> toReturn = new HashMap<String, Object>();
        toReturn.put("success", true);
        toReturn.put("status", "An activation code has been sent you your e-mail.");
        return Response.ok(toReturn).build();
    }
    
    @GET
    @Path("/activate")
    @Produces(MediaType.APPLICATION_JSON) 
    public Response activateUser(@QueryParam("username") String username, @QueryParam("code") String activationCode) {
        if(oUserMapper.activateNewUser(username, activationCode) == 1) {
            return Response.ok("activated successfully").build();
        }
        else {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("The activation code is not valid for the given username")
                    .build();
        }
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserByID(@PathParam("id") int id) {
        return userMapper.getUser(id);
    }
}
