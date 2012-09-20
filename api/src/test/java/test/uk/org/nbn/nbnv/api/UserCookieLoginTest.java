package test.uk.org.nbn.nbnv.api;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import uk.org.nbn.nbnv.api.rest.resources.UserResource;
import java.util.Arrays;
import javax.ws.rs.core.Cookie;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.org.nbn.nbnv.api.model.User;
import static org.junit.Assert.*;
/**
 *
 * @author Christopher Johnson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jersey-jetty-applicationContext.xml")
@DirtiesContext
public class UserCookieLoginTest {
    @Autowired WebResource resource;
    @Autowired Properties properties;
    
    @Test
    public void validLogin() {
        //Given
        String username = "tester";
        String password = "password";
        
        //when
        List<NewCookie> cookies = resource
           .path("user")
           .path("login")
           .queryParam("username", username)
           .queryParam("password", password)
           .accept(MediaType.APPLICATION_JSON)
           .head()
           .getCookies();
        
 
        //then
        User user = addCookies(resource.path("user"), cookies)
            .accept(MediaType.APPLICATION_JSON)
            .get(User.class);
       
        assertEquals("The username was invalid", user.getUsername(), username);
    }
    
    @Test
    public void invalidToken() {
        //Given
        String cookieKey = properties.getProperty("sso_token_key");
        Cookie invalidCookie = new Cookie(cookieKey, "Giberish");
        
        //when
        Status statusResponse = addCookies(resource.path("user"), Arrays.asList(invalidCookie))
            .accept(MediaType.APPLICATION_JSON)
            .head()
            .getClientResponseStatus();
        
        //then
        assertEquals("The forbidden status was expected", Status.UNAUTHORIZED, statusResponse);
    }
    
    @Test
    public void noToken() {
        //Given Nothing
        
        //when
        User user = resource
            .path("user")
            .accept(MediaType.APPLICATION_JSON)
            .get(User.class);
        
       //then
        assertEquals("The username was invalid", User.PUBLIC_USER, user);
    }
    
    @Test
    public void invalidUsernameLogin() {
        //Given
        String username = "wrong_tester";
        String password = "password";
        
        //when
        Status status = resource
            .path("user")
            .path("login")
            .queryParam("username", username)
            .queryParam("password", password)
            .accept(MediaType.APPLICATION_JSON)
            .head()
            .getClientResponseStatus();
        
        //then
        assertEquals("Expected unauthorised exception", Status.UNAUTHORIZED, status);          
    }
    
    @Test
    public void invalidPasswordLogin() {
        //Given
        String username = "tester";
        String password = "wrong_password";
        
        //when
        int status = resource
            .path("user")
            .path("login")
            .queryParam("username", username)
            .queryParam("password", password)
            .accept(MediaType.APPLICATION_JSON)
            .head()
            .getStatus();
        
        //then
        assertEquals("Expected unauthorised exception", 401, status);      
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
