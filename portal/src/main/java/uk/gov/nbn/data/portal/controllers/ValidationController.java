package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.WebResource;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
@Controller
public class ValidationController {
    
    @Autowired WebResource resource;

    @RequestMapping(value = "/Validator", method = RequestMethod.GET)
    public ModelAndView get(Model model) {
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);
        
        if (currentUser.getId() == User.PUBLIC_USER_ID) {
            Map<String, Object> modelSSO = new HashMap<String, Object>();
            modelSSO.put("redirect", "/Validator");
            modelSSO.put("status", "You need to be logged in to user the online validation tool");
            return new ModelAndView("sso", modelSSO);
        }
        
        return new ModelAndView("validator");
    }
}