/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.nbn.data.portal.exceptions.UnauthorisedException;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
@Controller
public class OrganisationAdminController {

    @Autowired
    WebResource resource;

    @RequestMapping(value = "/Organisations/{id}/Admin", method = RequestMethod.GET)
    public ModelAndView get(@PathVariable int id, Model model) {

        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        OrganisationMembership member = resource.path("organisationMemberships/" + id + "/" + currentUser.getId())
                .accept(MediaType.APPLICATION_JSON)
                .get(OrganisationMembership.class);

        if (member != null && member.getRole() == OrganisationMembership.Role.administrator) {
            // Push org ID into the model so we can grab the data a bit more 
            // easilly
            model.addAttribute("organisationID", id);

            return new ModelAndView("organisationAdmin");
        }

        throw new UnauthorisedException();
    }

    @RequestMapping(value = "/Organisations/{id}/Admin/{user}/modify", method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyUser(@PathVariable int id, @PathVariable int user, String json) {
        if (isUserOrgAdmin(id, user)) {
            return Response.ok("success").build();
        }

        return Response.serverError().build();
    }
    
    private boolean isUserOrgAdmin(int orgId, int userId) {
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (resource.path(String.format("organisationMemberships/%i/%i/isadmin", id, currentUser.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .get(boolean.class)) {
            return true;
        }        
        
        return false;
    }
}