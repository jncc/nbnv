/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.reseter.utils;

import java.util.ArrayList;
import java.util.List;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;

/**
 *
 * @author paulbe
 */
public class AccessRequestUtils {

    public List<String> createSpeciesList(AccessRequestJSON accessRequest) {
        List<String> species = null;
        if (accessRequest.getTaxon().getTvk() != null && !accessRequest.getTaxon().getTvk().isEmpty()) {
            species = new ArrayList<String>();
            species.add(accessRequest.getTaxon().getTvk());
        }

        return species;
    }
}
