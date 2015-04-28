/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources.utils;

import java.util.HashMap;
import uk.org.nbn.nbnv.api.model.TaxonObservation;

/**
 *
 * @author mattd
 */
public class TaxonObservationHelper {
    public static void setAttributeList(TaxonObservation observation, boolean includeAttributes) {
        if (includeAttributes && (observation.isFullVersion() || observation.isPublicAttribute())) {
            observation.setAttributes(new HashMap<String, String>());

            String[] attVals = org.apache.commons.lang.StringUtils.split(
                    org.springframework.util.StringUtils.hasText(observation.getAttrStr())
                    ? observation.getAttrStr() : "", "¦");
            for (String attVal : attVals) {
                if (org.springframework.util.StringUtils.hasText(attVal)) {
                    String[] vals = org.apache.commons.lang.StringUtils.split(attVal, "¬");
                    if (vals.length == 2) {
                        observation.getAttributes().put(vals[0], vals[1]);
                    } else if (vals.length == 1) {
                        observation.getAttributes().put(vals[0], "");
                    }
                }
            }
        }
        observation.setAttrStr(null);        
    }
}
