/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.WebResource;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.api.model.OrganisationAccessRequest;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserAccessRequest;

/**
 *
 * @author paulbe
 */
@Controller
public class AccessRequestController {
    @Autowired WebResource resource;

    @RequestMapping(value = "/AccessRequest/Create", method = RequestMethod.GET)
    public ModelAndView getCreatePage() {
        //get the current logged in user
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (currentUser.getId() != User.PUBLIC_USER_ID) {
            return new ModelAndView("accessRequestCreate");
        } else {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("redirect", "/AccessRequest/Create");
            model.put("status", "You need to be logged in to create an access request.");
            return new ModelAndView("sso", model);
        }
    }
    
    @RequestMapping(value = "/AccessRequest/Create/Grant", method = RequestMethod.GET)
    public ModelAndView getCreateGrantPage() {
        //get the current logged in user
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (currentUser.getId() != User.PUBLIC_USER_ID) {
            return new ModelAndView("accessGrantCreate");
        } else {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("redirect", "/AccessRequest/Create/Grant");
            model.put("status", "You need to be logged in to create an access grant.");
            return new ModelAndView("sso", model);
        }
    }

    @RequestMapping(value = "/AccessRequest/Edit/User/{id}", method = RequestMethod.GET)
    public ModelAndView getUserEditPage(@PathVariable("id") int id) {
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);
        
        UserAccessRequest request = resource.path("/user/userAccesses/requests/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .get(UserAccessRequest.class);

        if (currentUser.getId() == User.PUBLIC_USER_ID || request == null) {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("redirect", "/AccessRequest/Edit/User/" + id);
            model.put("status", "You need to be logged in to edit an access request.");
            return new ModelAndView("sso", model);            
        }
        
        return new ModelAndView("userAccessRequestEdit", "model", request);
    }

    @RequestMapping(value = "/AccessRequest/History/{dataset}", method = RequestMethod.GET)
    public ModelAndView getHistoryPage(@PathVariable("dataset") String dataset) {
        //get the current logged in user
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (currentUser.getId() != User.PUBLIC_USER_ID) {
            return new ModelAndView("accessRequestHistory", "model", dataset);
        } else {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("redirect", "/AccessRequest/History/" + dataset);
            model.put("status", "You need to be logged in to view access request history.");
            return new ModelAndView("sso", model);
        }
    }
    
    @RequestMapping(value = "/AccessRequest/Edit/Organisation/{id}", method = RequestMethod.GET)
    public ModelAndView getOrgEditPage(@PathVariable("id") int id) {
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);
        
        OrganisationAccessRequest request = resource.path("/organisation/organisationAccesses/requests/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .get(OrganisationAccessRequest.class);

        if (currentUser.getId() == User.PUBLIC_USER_ID || request == null) {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("redirect", "/AccessRequest/Edit/Organisation/" + id);
            model.put("status", "You need to be logged in to edit an access request.");
            return new ModelAndView("sso", model);            
        }
        
        return new ModelAndView("orgAccessRequestEdit", "model", request);
    }
}
