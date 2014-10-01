/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.providers;

import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;

/**
 *
 * @author Matt Debont
 */
public class ApiObservationViewProvider {
    public String selectApiObservationViewsByDataset(Map<String, Object> params) {
        BEGIN();
        SELECT("aovsd.*, tdd.recordCount AS totalDatasetRecords");
        FROM("ApiObservationViewStatisticsData aovsd");
        WHERE("aovsd.datasetKey = #{datasetKey}");
        INNER_JOIN("TaxonDatasetData tdd ON tdd.datasetKey = aovsd.datasetKey");
        if (params.containsKey("startDate") && !params.get("startDate").equals("")) {
            WHERE("aovsd.viewTime >= '" + params.get("startDate") + "'");
        }
        if (params.containsKey("endDate") && !params.get("endDate").equals("")) {
            WHERE("aovsd.viewTime <= '" + params.get("endDate") + "'");
        }
        
        ORDER_BY("aovsd.viewTime DESC");
        
        return SQL();
    }
    
    public String selectApiObservationViewsByUser(Map<String, Object> params) {
        BEGIN();
        SELECT("*");
        FROM("ApiObservationViewStatisticsData aovsd");
        INNER_JOIN("DatasetAdministrator da ON da.datasetKey = aovsd.datasetKey");
        WHERE("da.userID = #{user.id}");

        if (params.containsKey("startDate") && !params.get("startDate").equals("")) {
            WHERE("aovsd.downloadTime >= '" + params.get("startDate") + "'");
        }
        if (params.containsKey("endDate") && !params.get("endDate").equals("")) {
            WHERE("aovsd.downloadTime <= '" + params.get("endDate") + "'");
        }
        
        ORDER_BY("aovsd.downloadDate DESC");

        return SQL();
    }
}
