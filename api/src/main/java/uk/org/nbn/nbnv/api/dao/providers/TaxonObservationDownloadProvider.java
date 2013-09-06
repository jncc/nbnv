/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.providers;

import java.util.List;
import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;

/**
 *
 * @author Matt Debont
 */
public class TaxonObservationDownloadProvider {
    
    public String selectDownloadReportsForDataset(Map<String, Object> params) {
        String from = "(" + filterDownloadReports(params) + ") AS tods";
        BEGIN();
        SELECT("tods.*, od.name AS organisationName, tdd.recordCount AS totalRecords, (SELECT SUM(recordCount) FROM TaxonObservationDownloadStatisticsData s WHERE s.filterID = tods.filterID) AS totalDownloaded");
        FROM(from);
        LEFT_OUTER_JOIN("OrganisationData od ON od.id = tods.organisationID");
        INNER_JOIN("TaxonDatasetData tdd ON tdd.datasetKey = tods.datasetKey");
        ORDER_BY("tods.downloadTime DESC");
        return SQL();
    }
    
    public String selectDistinctUsersForDatasets(Map<String, Object> params) {
        BEGIN();
        SELECT_DISTINCT("userID, forename, surname, email");
        FROM("TaxonObservationDownloadStatisticsData");
        
        if (params.containsKey("datasetKey") && !params.get("datasetKey").equals("")) {
            if (params.get("datasetKey") instanceof List) {
                List<String> datasetArgs = (List<String>) params.get("datasetKey");
                if (datasetArgs.size() > 0 && !"".equals(datasetArgs.get(0))) {
                    WHERE("todsd.datasetKey IN " + ProviderHelper.datasetListToCommaList((List<String>) params.get("datasetKey")));
                }
            } else {
                WHERE("todsd.datasetKey = '" + params.get("datasetKey") + "'");
            }
        }        
        return SQL();
    }
    
    public String selectTotalDownloadsForDatasets(Map<String, Object> params) {
        String from = "(" + filterDownloadReports(params) + ") AS tods";
        BEGIN();
        SELECT("DATEADD(DAY,0, DATEDIFF(DAY,0, tods.downloadTime)) AS created, SUM(tods.recordCount) AS total");
        FROM(from);        
        GROUP_BY("DATEADD(DAY,0, DATEDIFF(DAY,0, tods.downloadTime))");
        return SQL();
    }
    
    public String createFilter(Map<String, Object> params) {
        BEGIN();
        SELECT("todsd.*");
        FROM("TaxonObservationDownloadStatisticsData todsd");

        if (params.containsKey("datasetKey") && !params.get("datasetKey").equals("")) {
            if (params.get("datasetKey") instanceof List) {
                List<String> datasetArgs = (List<String>) params.get("datasetKey");
                if (datasetArgs.size() > 0 && !"".equals(datasetArgs.get(0))) {
                    WHERE("todsd.datasetKey IN " + ProviderHelper.datasetListToCommaList((List<String>) params.get("datasetKey")));
                }
            } else {
                WHERE("todsd.datasetKey = '" + params.get("datasetKey") + "'");
            }
        }              
        
        if (params.containsKey("startDate") && !params.get("startDate").equals("")) {
            ProviderHelper.addStartDateFilter((String) params.get("startDate"));
        }
        if (params.containsKey("endDate") && !params.get("endDate").equals("")) {
            ProviderHelper.addStartDateFilter((String) params.get("endDate"));
        }
        if (params.containsKey("purposeID") && (Integer) params.get("purposeID") > 0) {
            WHERE("todsd.purposeID = " + params.get("purposeID"));
        }
        if (params.containsKey("organisationID") && (Integer) params.get("organisationID") > 0) {
            WHERE("todsd.organisationID = " + params.get("organisationID"));
        }
        // Do not include if the public user ID (1) is passed through
        if (params.containsKey("userID") && (Integer) params.get("userID") > 1) {
            WHERE("todsd.organistionID = " + params.get("userID"));
        }
        if (params.containsKey("filterID") && (Integer) params.get("filterID") > 0) {
            WHERE("todsd.filterID = " + params.get("filterID"));
        }
        
        return SQL();
    }
    
    public String filterDownloadReports(Map<String, Object> params) {
        BEGIN();
        SELECT("todsd.*");
        FROM("TaxonObservationDownloadStatisticsData todsd");
        
        WHERE("todsd.datasetKey = '" + params.get("datasetKey") + "'");
// Need to figure out a sensible way of handling the permissions against a set of datasets
//        if (params.containsKey("datasetKey") && !params.get("datasetKey").equals("")) {
//            if (params.get("datasetKey") instanceof List) {
//                List<String> datasetArgs = (List<String>) params.get("datasetKey");
//                if (datasetArgs.size() > 0 && !"".equals(datasetArgs.get(0))) {
//                    WHERE("todsd.datasetKey IN " + datasetListToCommaList((List<String>) params.get("datasetKey")));
//                }
//            } else {
//                WHERE("todsd.datasetKey = '" + params.get("datasetKey") + "'");
//            }
//        }
        if (params.containsKey("startDate") && !params.get("startDate").equals("")) {
            ProviderHelper.addStartDateFilter((String) params.get("startDate"));
        }
        if (params.containsKey("endDate") && !params.get("endDate").equals("")) {
            ProviderHelper.addStartDateFilter((String) params.get("endDate"));
        }
        if (params.containsKey("purposeID") && (Integer) params.get("purposeID") > 0) {
            WHERE("todsd.purposeID = " + params.get("purposeID"));
        }
        if (params.containsKey("organisationID") && (Integer) params.get("organisationID") > 0) {
            WHERE("todsd.organisationID = " + params.get("organisationID"));
        }
        // Do not include if the public user ID (1) is passed through
        if (params.containsKey("userID") && (Integer) params.get("userID") > 1) {
            WHERE("todsd.organistionID = " + params.get("userID"));
        }
        if (params.containsKey("filterID") && (Integer) params.get("filterID") > 0) {
            WHERE("todsd.filterID = " + params.get("filterID"));
        }
        
        return SQL();
    }
}
