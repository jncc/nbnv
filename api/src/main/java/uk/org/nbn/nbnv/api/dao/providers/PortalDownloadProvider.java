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
public class PortalDownloadProvider {
    public String selectPortalDownloadsByDataset(Map<String, Object> params) {
        BEGIN();
        SELECT("*");
        FROM("PortalDownloadStatisticsData pdsd");
        WHERE("pdsd.datasetKey = #{datasetKey}");
        
        if (params.containsKey("startDate") && !params.get("startDate").equals("")) {
            WHERE("pdsd.downloadTime >= '" + params.get("startDate") + "'");
        }
        if (params.containsKey("endDate") && !params.get("endDate").equals("")) {
            WHERE("pdsd.downloadTime <= '" + params.get("endDate") + "'");
        }
        
        ORDER_BY("pdsd.downloadDate DESC");

        return SQL();
    }
    
    public String selectPortalDownloadsByUser(Map<String, Object> params) {
        BEGIN();
        SELECT("*");
        FROM("PortalDownloadStatisticsData pdsd");
        INNER_JOIN("DatasetAdministrator da ON da.datasetKey = pdsd.datasetKey");
        WHERE("da.userID = #{user.id}");

        if (params.containsKey("startDate") && !params.get("startDate").equals("")) {
            WHERE("pdsd.downloadTime >= '" + params.get("startDate") + "'");
        }
        if (params.containsKey("endDate") && !params.get("endDate").equals("")) {
            WHERE("pdsd.downloadTime <= '" + params.get("endDate") + "'");
        }
        
        ORDER_BY("pdsd.downloadDate DESC");

        return SQL();
    }
}
