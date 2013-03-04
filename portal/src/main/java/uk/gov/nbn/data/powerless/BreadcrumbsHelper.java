/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless;

import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;
import uk.gov.nbn.data.powerless.breadcrumbs.BreadcrumbTranslator;
import uk.gov.nbn.data.powerless.breadcrumbs.DatasetTranslator;
import uk.gov.nbn.data.powerless.breadcrumbs.TaxonTranslator;

/**
 * The following class presents a single static method for obtaining a set of 
 * breadcrumbs for a given collection of Spring MVC HandlerMappings and a 
 * HttpServletRequest
 * @author cjohn
 */
public class BreadcrumbsHelper {
    private static HashMap<String, BreadcrumbTranslator> nameTranslator = createTranslatorMapping();
    
    public static List<Breadcrumb> getBreadcrumbs(Collection<HandlerMapping> mappings, HttpServletRequest request, WebResource webResource) throws Exception {
        List<Breadcrumb> toReturn = new ArrayList<Breadcrumb>();
        
        String requestURI = request.getRequestURI().substring(1);
        String prevUriPart = "";
        if(!requestURI.isEmpty()) { //are there paths to be added?
            StringBuilder currUri = new StringBuilder("/");
            for(String currUriPart : requestURI.split("/")) {
                Breadcrumb toAdd = new Breadcrumb(createNameFromUriPart(prevUriPart, currUriPart, webResource));
                currUri.append(currUriPart);
                if(hasMappingHandler(mappings, currUri.toString())){
                    toAdd.setLink(currUri.toString());
                }
                currUri.append("/");
                prevUriPart = currUriPart;
                toReturn.add(toAdd);
            }
        }
        return toReturn;
    }

    private static String createNameFromUriPart(String prev, String curr, WebResource webResource) {
        if (nameTranslator.containsKey(prev)) {
            return nameTranslator.get(prev).translateName(curr, webResource);
        } else {
            return curr.replace('_', ' ');
        }
    }
    private static HashMap<String, BreadcrumbTranslator> createTranslatorMapping() {
        HashMap<String, BreadcrumbTranslator> res = new HashMap<String, BreadcrumbTranslator>();
        
        Reflections ref = new Reflections("uk.gov.nbn.data.powerless.breadcrumbs");
        Set<Class<? extends BreadcrumbTranslator>> classes = ref.getSubTypesOf(BreadcrumbTranslator.class);
        
        for (Class<? extends BreadcrumbTranslator> c : classes) {
            try {
                BreadcrumbTranslator bt = c.newInstance();
                res.put(bt.previousCrumb(), bt);
            } catch (InstantiationException ex) {
                Logger.getLogger(BreadcrumbsHelper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(BreadcrumbsHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return res;
    }
    private static boolean hasMappingHandler(Collection<HandlerMapping> mappings, String breadcrumbUrl) throws Exception {
        for(HandlerMapping currMapping : mappings) {
            if(currMapping.getHandler(new MockHttpServletRequest("GET", breadcrumbUrl)) != null) {
                return true;
            }
        }
        return false;
    }
    
    public static class Breadcrumb {
        private final String name;
        
        private String link;
        
        public Breadcrumb(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
