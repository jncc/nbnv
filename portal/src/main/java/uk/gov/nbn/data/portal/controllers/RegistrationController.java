package uk.gov.nbn.data.portal.controllers;


import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following Controller handles logging into and out of the NBN Gateway data
 * API
 * forward the cookie token to the browser
 * @author Christopher Johnson
 */
@Controller
public class RegistrationController {
    @Autowired WebResource resource; 
    
    @RequestMapping(value = "/User/Register", method = RequestMethod.GET)
    public ModelAndView processRequest() {
        return new ModelAndView("register", "user", new User());
    }
    
    @RequestMapping(value = "/User/Register", method = RequestMethod.POST)
    public ModelAndView registerUser(User newUser) {
        ClientResponse regResponse = resource.path("user")
                                             .type(MediaType.APPLICATION_JSON)
                                             .post(ClientResponse.class, newUser);
        if(regResponse.getClientResponseStatus() == Status.OK) {
            return new ModelAndView("activationWait");
        }
        else {
            //to do. handle errors
            return new ModelAndView("register", "user", newUser);
        }
    }
    
    @RequestMapping(value = "/User/Activate/{username}", method = RequestMethod.GET)
    public ModelAndView activateUser(
            @PathVariable("username") String username,
            @RequestParam("code") String activationCode ) {
        ClientResponse activationResponse = resource.path("user/activations")
                                                  .path(username)
                                                  .put(ClientResponse.class, activationCode);
        if(activationResponse.getClientResponseStatus() == Status.OK) {
            return new ModelAndView("activated");
        }
        else {
            return new ModelAndView("error", "status", activationResponse.getEntity(JSONObject.class));
        }
    }
}
