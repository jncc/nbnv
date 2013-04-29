/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.utils;

import uk.org.nbn.nbnv.api.model.meta.AccessRequestFilterJSON;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;

/**
 *
 * @author Administrator
 */
public class AccessRequestJSONToText {

    public static String convert(AccessRequestJSON ar) {
        String text = null;

        if ("ns".equals(ar.getSensitive())) {
            text = "All <b>non-sensitive</b> records";
        } else if ("sans".equals(ar.getSensitive())) {
            text = "All <b>sensitive and non-sensitive</b> records";
        }

        if (!ar.getYear().isAll()) {
            text += " between <b>" + Integer.toString(ar.getYear().getStartYear()) + "</b> and <b>" + Integer.toString(ar.getYear().getEndYear()) + "</b>";
        }

        if (!ar.getTaxon().isAll()) {
            text += " for <b><i>" + ar.getTaxon().getTvk() + "</i></b>";
        }

        if (!ar.getSpatial().isAll()) {
            text += " <b>" + ar.getSpatial().getMatch() + "</b> the boundary of <b>" + ar.getSpatial().getFeature() + "</b>";
        }


        return text;
    }
}
