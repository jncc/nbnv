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
        } else if ("s".equals(ar.getSensitive())) {
            text = "All <b>sensitive</b> records";
        }

        for (AccessRequestFilterJSON filter : ar.getFilters()) {
            if ("year".equals(filter.getType())) {
                text += " between <b>" + Integer.toString(filter.getStart()) + "</b> and <b>" + Integer.toString(filter.getEnd()) + "</b>";
            } else if ("taxon".equals(filter.getType())) {
                text += " for <b><i>" + filter.getSciname() + "</i></b>";
            } else if ("spatial".equals(filter.getType())) {
                text += " <b>" + filter.getMatch() + "</b> the boundary of <b>" + filter.getBoundary() + "</b>";
            }
        }
        
        return text;
    }
}
