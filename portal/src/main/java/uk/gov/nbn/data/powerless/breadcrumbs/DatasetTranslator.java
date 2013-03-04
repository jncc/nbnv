/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.breadcrumbs;

import com.sun.jersey.api.client.WebResource;
import uk.org.nbn.nbnv.api.model.Dataset;

/**
 *
 * @author paulbe
 */
public class DatasetTranslator extends BreadcrumbTranslator {

    @Override
    public String previousCrumb() {
        return "Datasets";
    }

    @Override
    public String translateName(String element, WebResource webResource) {
        return webResource.path("datasets/" + element).get(Dataset.class).getTitle();
    }
    
}
