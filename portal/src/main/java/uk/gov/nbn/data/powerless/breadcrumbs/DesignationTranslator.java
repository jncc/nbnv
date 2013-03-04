/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.breadcrumbs;

import com.sun.jersey.api.client.WebResource;
import uk.org.nbn.nbnv.api.model.Designation;

/**
 *
 * @author paulbe
 */
public class DesignationTranslator extends BreadcrumbTranslator {

    @Override
    public String previousCrumb() {
        return "Designation_Categories";
    }

    @Override
    public String translateName(String element, WebResource webResource) {
        return webResource.path("designations/" + element).get(Designation.class).getName();
    }
    
}
