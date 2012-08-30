package uk.gov.nbn.data.gis.maps;

import java.util.Iterator;
import java.util.List;

/**
 * The following class provides methods which can be used to inject SQL filters
 * into a SQL statement.
 * 
 * USE WITH CAUTION. Ensure that the parameters passed to these methods are 
 * validly sanitised to avoid SQL injection attacks
 * @author Chris Johnson
 */
class MapHelper {
    /**
     * @param startYear
     * @return A startyear filter for a given start year prefixed with AND for 
     * use in a WHERE clause of an SQL query
     */
    static String createStartYearSegment(String startYear) {
        if(startYear != null) {
            return new StringBuilder("AND startDate >= ")
                .append(sqlQuote(startYear))
                .toString();
        }
        else {
            return "";
        }
    }
    
     /**
     * @param endYear
     * @return A endYear filter for a given end year prefixed with AND for 
     * use in a WHERE clause of an SQL query
     */
    static String createEndYearSegment(String endYear) {
        if(endYear != null) {
            return new StringBuilder("AND endDate <= ")
                .append(sqlQuote(endYear))
                .toString();
        }
        else {
            return "";
        }
    }
    
    /**
     * @param datasetKeys List of dataset keys 
     * @return A IN filter with all of dataset keys defined prefixed with AND to
     * use in a WHERE clause of an SQL Query
     */
    static String createInDatasetsSegment(List<String> datasetKeys) {
        if(datasetKeys !=null && !datasetKeys.isEmpty()) {
            StringBuilder toReturn = new StringBuilder("AND datasetKey IN (");
            Iterator<String> datasetKeyIter = datasetKeys.iterator();
            toReturn.append(sqlQuote(datasetKeyIter.next()));
            
            while(datasetKeyIter.hasNext()) {
                toReturn.append(",").append(sqlQuote(datasetKeyIter.next()));
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
