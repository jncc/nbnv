/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.breadcrumbs;

import com.sun.jersey.api.client.WebResource;
import uk.org.nbn.nbnv.api.model.Taxon;

/**
 *
 * @author paulbe
 */
public class TaxonTranslator extends BreadcrumbTranslator {

    @Override
    public String previousCrumb() {
        return "Taxa";
    }

    @Override
    public String translateName(String element, WebResource webResource) {
        return webResource.path("taxa/" + element).get(Taxon.class).getName();
    }
    
}
