/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nbn.common.taxon.Taxon;

/**
 *
 * @author Administrator
 */
public class TaxonListUtils {
    public static String getCommaDelimitedTVKList(Collection<Taxon> list) {
        if (list == null || list.isEmpty())
            return "";

        StringBuilder result = new StringBuilder();

        for (Taxon td : list) {
            result.append(',');
            result.append(td.getTaxonVersionKey());
        }

        return result.substring(1);
    }

    public static String getCommaDelimitedTaxonKeyList(Collection<Taxon> list) {
        if (list == null || list.isEmpty())
            return "";
        
        StringBuilder result = new StringBuilder();

        for (Taxon td : list) {
            result.append(',');
            result.append(td.getTaxonKey());
        }

        return result.substring(1);
    }

    public static Map<String, Taxon> getHashMappedTaxonList(List<Taxon> list) {
        if (list == null)
            return null;
        
        Map<String, Taxon> map = new HashMap<String, Taxon>();

        for (Taxon t : list) {
            if (!map.containsKey(t.getTaxonVersionKey()))
                map.put(t.getTaxonVersionKey(), t);
        }

        return map;
    }
}
