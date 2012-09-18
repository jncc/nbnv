package test.uk.org.nbn.nbnv.api;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.org.nbn.nbnv.api.model.User;
import static org.junit.Assert.*;
import org.junit.Before;
/**
 *
 * @author Christopher Johnson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jersey-jetty-applicationContext.xml")
@DirtiesContext
public class UserMD5HashAuthenticationTest {
    private MessageDigest md5Digest;
    @Autowired WebResource resource;
    
    @Before
    public void createMD5Digest() throws NoSuchAlgorithmException {
        md5Digest = MessageDigest.getInstance("md5");
    }
    
    @Test
    public void makeAuthenticatedRequestUsingMD5HashPassKey() {
        //Given
        String username = "tester";
        String password = "password";
        byte[] md5PasswordByes = md5Digest.digest(password.getBytes());
        String md5PasswordHex = Hex.encodeHexString(md5PasswordByes);
        
        //when
        User user = resource
            .path("user")
            .queryParam("username", username)
            .queryParam("userkey", md5PasswordHex)
            .accept(MediaType.APPLICATION_JSON)
            .get(User.class);
        
        //then
        assertEquals("The username was invalid", user.getUsername(), username);
    }
    
    @Test
    public void makeInvalidAuthenticatedRequestUsingMD5HashPassKey() {
        //Given
        String username = "tester";
        String password = "gibber";
        byte[] md5PasswordByes = md5Digest.digest(password.getBytes());
        String md5PasswordHex = Hex.encodeHexString(md5PasswordByes);
        
        //when
        Status status = resource
            .path("user")
            .queryParam("username", username)
            .queryParam("userkey", md5PasswordHex)
            .head()
            .getClientResponseStatus();
        
        //then
        assertEquals("Expected unauthorized exception", Status.UNAUTHORIZED, status);
    }
    
    @Test
    public void attemptToLoginWithoutPassKey() {
        //Given
        String username = "tester";
        
        //when
        Status status = resource
            .path("user")
            .queryParam("username", username)
            .head()
            .getClientResponseStatus();
        
        //then
        assertEquals("Expected unauthorized exception", Status.BAD_REQUEST, status);
    }
    
    @Test
    public void attemptLoginWithUsernameAndUserKeyAndCookie() {
        //Given
        String username = "tester";
        String password = "password";
        byte[] md5PasswordByes = md5Digest.digest(password.getBytes());
        String md5PasswordHex = Hex.encodeHexString(md5PasswordByes);
        
        String cookieUsername = "tester2";
        String cookiepassword = "password";
        //when
        
        List<NewCookie> cookies = resource
           .path("user")
           .path("login")
           .queryParam("username", cookieUsername)
           .queryParam("password", cookiepassword)
           .accept(MediaType.APPLICATION_JSON)
           .head()
           .getCookies();
        
        User user = addCookies(
                resource.path("user")
                .queryParam("username", username)
                .queryParam("userkey", md5PasswordHex), cookies)
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);
        
        //then
        assertEquals("The md5 has username", username, user.getUsername());
    }
    
    
    /**Helper method for passing a list of cookies to request*/
    private static WebResource.Builder addCookies(WebResource resource, List<? extends Cookie> cookies) {
        WebResource.Builder builder = resource.getRequestBuilder();
        for (Cookie c : cookies) {
            builder = builder.cookie(c);
        }
        return builder;
    }
}
