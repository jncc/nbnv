/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uk.org.nbn.nbnv.api;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
 * @author cjohn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jersey-jetty-applicationContext.xml")
@DirtiesContext
public class UserRegistrationTest {
    @Autowired WebResource resource;
    
    @Test
    public void attemptToReRegisterUsername() {
        //given
        User existingUser = new User();
        existingUser.setUsername("tester");
        existingUser.setEmail("some_unused@email.com");
        existingUser.setForename("Existing");
        existingUser.setSurname("Username");
        existingUser.setPassword("UnUsedPassword");
        existingUser.setPhone("0124123123");
        
        //when
        ClientResponse response = resource.path("user")
                                        .type(MediaType.APPLICATION_JSON)
                                        .post(ClientResponse.class, existingUser);
        
        //then
        assertEquals("Expected a bad request for already existing user", 
                            Status.BAD_REQUEST, response.getClientResponseStatus());
    }

    @Test
    public void attemptToReRegisterEmail() {
        //given
        User existingUser = new User();
        existingUser.setUsername("not_registered_user");
        existingUser.setEmail("test@user.com");
        existingUser.setForename("Existing");
        existingUser.setSurname("Username");
        existingUser.setPassword("UnUsedPassword");
        existingUser.setPhone("0124123123");
        
        //when
        ClientResponse response = resource.path("user")
                                        .type(MediaType.APPLICATION_JSON)
                                        .post(ClientResponse.class, existingUser);
        
        //then
        assertEquals("Expected a bad request for already existing user", 
                            Status.BAD_REQUEST, response.getClientResponseStatus());
    }
    
    @Test
    public void attemptToRegisterUserWithoutPassword() {
        //given
        User existingUser = new User();
        existingUser.setUsername("not_registered_user");
        existingUser.setEmail("not_registered_user@user.com");
        existingUser.setForename("Existing");
        existingUser.setSurname("Username");
        //existingUser.setPassword("Not setting password");
        existingUser.setPhone("0124123123");
        
        //when
        ClientResponse response = resource.path("user")
                                        .type(MediaType.APPLICATION_JSON)
                                        .post(ClientResponse.class, existingUser);
        
        //then
        assertEquals("Expected a bad request for missing password", 
                            Status.BAD_REQUEST, response.getClientResponseStatus());
    }
}
