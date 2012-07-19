/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.portal;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.util.List;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.api.model.DesignationCategory;
import uk.org.nbn.nbnv.api.model.DesignationCategory;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
public class DesignationCategoryController {
    @RequestMapping("/dc")
    public ModelAndView singleCategory(Model model) {
        ModelAndView mav = new ModelAndView("designationCategory", "model", model);
        
        ClientConfig cc = new DefaultClientConfig();
        cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, Boolean.TRUE);
        Client c = Client.create(cc);
        WebResource r = c.resource("http://localhost:8084/api/designationCategories/20");
        DesignationCategory dc = r.accept(MediaType.APPLICATION_JSON).get(DesignationCategory.class);
        model.addAttribute("dc", dc);
        return mav;
    }

    @RequestMapping("/dcs")
    public ModelAndView listCategories(Model model) {
        ModelAndView mav = new ModelAndView("designationCategories", "model", model);
        
        ClientConfig cc = new DefaultClientConfig();
        cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, Boolean.TRUE);
        Client c = Client.create(cc);
        WebResource r = c.resource("http://localhost:8084/api/designationCategories");
        GenericType<List<DesignationCategory>> gt = new GenericType<List<DesignationCategory>>() { };
        List<DesignationCategory> dc = r.accept(MediaType.APPLICATION_JSON).get(gt);
        model.addAttribute("dcl", dc);
        return mav;
    }
}
