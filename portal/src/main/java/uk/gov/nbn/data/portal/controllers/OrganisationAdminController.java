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
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
@Controller
@RequestMapping(value = "/Organisations/{id}/Admin")
public class OrganisationAdminController {

    @Autowired
    WebResource resource;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(@PathVariable int id, Model model) {
        if (isUserOrgAdmin(id)) {
            // Push org ID into the model so we can grab the data a bit more 
            // easilly
            model.addAttribute("organisationID", id);

            return new ModelAndView("organisationAdmin");
        }

        throw new ForbiddenException();
    }

    private boolean isUserOrgAdmin(int orgId) {
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (resource.path(String.format("organisationMemberships/%d/%d/isadmin", orgId, currentUser.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .get(boolean.class)) {
            return true;
        }

        return false;
    }
}