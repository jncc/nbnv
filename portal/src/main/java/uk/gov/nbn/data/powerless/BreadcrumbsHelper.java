/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

/**
 * The following class presents a single static method for obtaining a set of 
 * breadcrumbs for a given collection of Spring MVC HandlerMappings and a 
 * HttpServletRequest
 * @author cjohn
 */
public class BreadcrumbsHelper {
    
    public static List<Breadcrumb> getBreadcrumbs(Collection<HandlerMapping> mappings, HttpServletRequest request) throws Exception {
        List<Breadcrumb> toReturn = new ArrayList<Breadcrumb>();
        
        String requestURI = request.getRequestURI().substring(1);
        if(!requestURI.isEmpty()) { //are there paths to be added?
            StringBuilder currUri = new StringBuilder("/");
            for(String currUriPart : requestURI.split("/")) {
                Breadcrumb toAdd = new Breadcrumb(currUriPart.replace('_', ' '));
                currUri.append(currUriPart);
                if(hasMappingHandler(mappings, currUri.toString())){
                    toAdd.setLink(currUri.toString());
                }
                currUri.append("/");
                toReturn.add(toAdd);
            }
        }
        return toReturn;
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
