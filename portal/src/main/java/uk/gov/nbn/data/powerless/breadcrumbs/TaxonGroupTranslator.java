/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.breadcrumbs;

import com.sun.jersey.api.client.WebResource;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroup;

/**
 *
 * @author paulbe
 */
public class TaxonGroupTranslator extends BreadcrumbTranslator {

    @Override
    public String previousCrumb() {
        return "Groups";
    }

    @Override
    public String translateName(String element, WebResource webResource) {
        return webResource.path("taxonOutputGroups/" + element).get(TaxonOutputGroup.class).getName();
    }
    
    
}
