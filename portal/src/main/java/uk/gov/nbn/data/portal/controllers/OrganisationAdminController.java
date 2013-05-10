/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.nbn.data.portal.exceptions.UnauthorisedException;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
@Controller
public class OrganisationAdminController {
    @Autowired WebResource resource;
    
    @RequestMapping(value = "/Organisations/{id}/Admin", method = RequestMethod.GET) 
    public ModelAndView get (@PathVariable int id){
        
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);
              
        // We want a list, so have to generic it up otherwise we will get a list 
        // of linked hash maps from the JSON, saves us having to deal with types 
        // and other such gubbins
        GenericType<List<OrganisationMembership>> genericType = 
                new GenericType<List<OrganisationMembership>>() {};      
        List<OrganisationMembership> members = 
                resource.path("organisationMemberships/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .get(genericType);

        for (OrganisationMembership member : members) {
            if (member.getUser().getId() == currentUser.getId() && 
                    member.getRole() == OrganisationMembership.Role.administrator) {
                return new ModelAndView("organisationAdmin");
            }
        }
                
        throw new UnauthorisedException();
    }
}