/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.breadcrumbs;

import com.sun.jersey.api.client.WebResource;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author paulbe
 */
public abstract class BreadcrumbTranslator {
    @Autowired Properties properties; 
    
    public abstract String previousCrumb();
    public abstract String translateName(String element, WebResource webResource);
}
