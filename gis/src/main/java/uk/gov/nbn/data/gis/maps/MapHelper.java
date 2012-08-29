/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.gis.maps;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Chris Johnson
 */
public class MapHelper {
    
    static String createInDatasetsSegment(List<String> datasetKeys) {
        if(datasetKeys !=null && !datasetKeys.isEmpty()) {
            StringBuilder toReturn = new StringBuilder("AND datasetKey IN (");
            Iterator<String> datasetKeyIter = datasetKeys.iterator();
            toReturn.append(sqlQuote(datasetKeyIter.next()));
            
            while(datasetKeyIter.hasNext()) {
                toReturn.append(",");
                toReturn.append(sqlQuote(datasetKeyIter.next()));
            }
            toReturn.append(")");
            return toReturn.toString();
        }
        else {
            return "";
        }
    }
    
    private static String sqlQuote(String toQuote) {
        return "'" + toQuote + "'";
    }
}
