/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
@Controller
public class OrganisationJoinController {
    @Autowired WebResource resource;
    
    @RequestMapping(value="/organisations/{id}/join", method= RequestMethod.GET)
    public ModelAndView joinOrganisation(@PathParam("id") int id) {
        
        return new ModelAndView();
    }
    
    @RequestMapping(value="/organisations/{id}/join", method= RequestMethod.POST)
    public ModelAndView createJoinOrganisationRequest(@PathParam("id") int id) {
        User currentUser = resource.path("user")
                           .accept(MediaType.APPLICATION_JSON)
                            .get(User.class);
        
        Organisation org = resource.path("organisations/" + id).accept(MediaType.APPLICATION_JSON).get(Organisation.class);
              
        // Create request for this user
        
        return new ModelAndView();
    }
}
