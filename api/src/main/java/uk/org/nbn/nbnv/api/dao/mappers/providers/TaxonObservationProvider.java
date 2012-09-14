/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.mappers.providers;

import java.util.List;
import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
import org.springframework.util.StringUtils;

/**
 *
 * @author Paul Gilbertson
 */
public class TaxonObservationProvider {

    public String filteredSelect(Map<String, Object> params) {
        BEGIN();
        SELECT("*");
        FROM("UserTaxonObservationData o");
        WHERE("userKey = #{userKey}");

        if ((Integer) params.get("startYear") > -1) {
            WHERE("YEAR(endDate) >= #{startYear}");
        }

        if ((Integer) params.get("endYear") > -1) {
            WHERE("YEAR(startDate) <= #{endYear}");
        }

        if (params.get("datasetKey") != null) {
            WHERE("datasetKey IN " + datasetListToCommaList((List<String>) params.get("datasetKey")));
        }

        if (params.get("ptvk") != null) {
            WHERE("pTaxonVersionKey IN " + taxaListToCommaList((List<String>) params.get("ptvk")));
        }

        if ((Integer) params.get("overlaps") > -1) {
            INNER_JOIN("FeatureOverlaps fo ON fo.overlappedFeatureID = o.featureID");
            WHERE("fo.parentFeatureID = #{overlaps}");
        }

        if ((Integer) params.get("within") > -1) {
            INNER_JOIN("FeatureContains fc ON fc.containedFeatureID = o.featureID");
            WHERE("fc.parentFeatureID = #{within}");
        }

        if (!(Boolean) params.get("sensitive")) {
            WHERE("sensitive = #{sensitive}");
        }
            
        if (!"".equals((String) params.get("designation"))) {
            INNER_JOIN("DesignationTaxonData dtd ON dtd.pTaxonVersionKey = o.pTaxonVersionKey");
            WHERE("dtd.code = #{designation}");
        }
        
        return SQL();
    }

    private String datasetListToCommaList(List<String> list) {
        for (String d : list) {
            if (!d.matches("[A-Z0-9]{8}")) {
                throw new IllegalArgumentException("Non-dataset key in dataset argument: " + d);
            }
        }

        return "('" + StringUtils.collectionToDelimitedString(list, "','") + "')";
    }

    private String taxaListToCommaList(List<String> list) {
        for (String d : list) {
            if (!d.matches("[A-Z0-9]{16}")) {
                throw new IllegalArgumentException("Non-taxon key in ptvk argument: " + d);
            }
        }

        return "('" + StringUtils.collectionToDelimitedString(list, "','") + "')";
    }
}
