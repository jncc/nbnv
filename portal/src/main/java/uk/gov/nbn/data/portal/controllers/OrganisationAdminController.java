package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.WebResource;
import java.util.HashMap;
import java.util.Map;
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
        //get the current logged in user
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (currentUser.getId() != User.PUBLIC_USER_ID) {
            if (isUserOrgAdmin(currentUser, id)) {
                model.addAttribute("organisationID", id);
                return new ModelAndView("organisationAdmin");
            }
        } else {
            Map<String, Object> modelSSO = new HashMap<String, Object>();
            modelSSO.put("redirect", "/Organisations/" + id + "/Admin");
            modelSSO.put("status", "You need to be logged in to view the organisation admin pages");
            return new ModelAndView("sso", modelSSO);
        }

        throw new ForbiddenException();
    }

    private boolean isUserOrgAdmin(User user, int orgId) {
        if (resource.path(String.format("organisationMemberships/%d/%d/isadmin", orgId, user.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .get(boolean.class)) {
            return true;
        }

        return false;
    }
}