/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.nbn.data.portal.exceptions.ForbiddenException;
import uk.org.nbn.nbnv.api.model.OrganisationJoinRequest;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
@Controller
@RequestMapping(value = "/Organisations/JoinRequest/{id}")
public class OrganisationJoinController {

    @Autowired
    WebResource resource;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(@PathVariable int id, Model model) {
        OrganisationJoinRequest request = resource.path(String.format("organisationMemberships/request/%d", id))
                .accept(MediaType.APPLICATION_JSON)
                .get(OrganisationJoinRequest.class);

        if (isUserOrgAdminOrRequestor(id,request)) {
            model.addAttribute("organisationID", request.getOrganisation().getId());
            model.addAttribute("requestID", id);
            
            return new ModelAndView("organisationJoinRequestView");
        }

        throw new ForbiddenException();
    }

    private boolean isUserOrgAdminOrRequestor(int id, OrganisationJoinRequest request) {
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (request.getUser().getId() == currentUser.getId()) {
            return true;
        }

        if (resource.path(String.format("organisationMemberships/%d/%d/isadmin", request.getOrganisation().getId(), currentUser.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .get(boolean.class)) {
            return true;
        }

        return false;
    }
}
