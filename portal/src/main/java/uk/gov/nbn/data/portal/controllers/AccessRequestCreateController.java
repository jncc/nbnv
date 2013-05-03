/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.WebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author paulbe
 */
@Controller
public class AccessRequestCreateController {
    @Autowired WebResource resource;
    
    @RequestMapping(value= "/AccessRequest/Create", method= RequestMethod.GET)
    public ModelAndView getPage() {
        return new ModelAndView("accessRequestCreate");
    }
}
