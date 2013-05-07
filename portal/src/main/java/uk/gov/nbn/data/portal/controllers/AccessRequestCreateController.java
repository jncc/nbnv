/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author paulbe
 */
@Controller
public class AccessRequestCreateController {
    @Autowired WebResource resource;
    
    @RequestMapping(value= "/AccessRequest/Create", method= RequestMethod.GET)
    public ModelAndView getPage() {
        //get the current logged in user
        User currentUser = resource.path("user")
                                   .accept(MediaType.APPLICATION_JSON)
                                    .get(User.class);
        return new ModelAndView("accessRequestCreate");
    }
}
