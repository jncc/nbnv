/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.breadcrumbs;

import com.sun.jersey.api.client.WebResource;
import uk.org.nbn.nbnv.api.model.TaxonNavigationGroup;

/**
 *
 * @author paulbe
 */
public class NavigationGroupTranslator extends BreadcrumbTranslator {

    @Override
    public String previousCrumb() {
        return "Species_Group";
    }

    @Override
    public String translateName(String element, WebResource webResource) {
        return webResource.path("taxonNavigationGroups/" + element).get(TaxonNavigationGroup.class).getName();
    }
    
    
}
