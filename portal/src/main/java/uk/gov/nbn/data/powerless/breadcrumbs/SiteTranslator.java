/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.breadcrumbs;

import com.sun.jersey.api.client.WebResource;
import uk.org.nbn.nbnv.api.model.Feature;

/**
 *
 * @author paulbe
 */
public class SiteTranslator extends BreadcrumbTranslator {

    @Override
    public String previousCrumb() {
        return "Sites";
    }

    @Override
    public String translateName(String element, WebResource webResource) {
        return webResource.path("features/" + element).get(Feature.class).getLabel();
    }
    
}
