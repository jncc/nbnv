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
public class TaxonObservationDownloadProvider {
    
    public String selectDownloadReportsForDataset(Map<String, Object> params) {
        BEGIN();
        SELECT("tods.*, od.name AS organisationName, tdd.recordCount AS totalRecords, (SELECT SUM(recordCount) FROM TaxonObservationDownloadStatisticsData s WHERE s.filterID = tods.filterID) AS totalDownloaded");
        FROM("TaxonObservationDownloadStatisticsData tods");
        LEFT_OUTER_JOIN("OrganisationData od ON od.id = tods.organisationID");
        INNER_JOIN("TaxonDatasetData tdd ON tdd.datasetKey = tods.datasetKey");
        WHERE(String.format("tods.datasetKey = '%s'", params.get("datasetKey")));
        return SQL();
    }
}
