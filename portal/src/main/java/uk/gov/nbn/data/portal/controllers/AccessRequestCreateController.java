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

    @Autowired
    WebResource resource;

    @RequestMapping(value = "/AccessRequest/Create", method = RequestMethod.GET)
    public ModelAndView getPage() {
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
}
