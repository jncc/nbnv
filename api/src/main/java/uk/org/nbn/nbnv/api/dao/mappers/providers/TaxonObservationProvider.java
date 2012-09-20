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
        createSelectQuery(params);
        return SQL();
    }
    
    public String filteredSelectGroups(Map<String, Object> params) {
        BEGIN();
        SELECT("DISTINCT taxonGroupKey, sortOrder, taxonGroupName, descriptor, parent");
        createSelectQuery(params);
        INNER_JOIN("TaxonData td ON o.taxonVersionKey = td.taxonVersionKey");
        INNER_JOIN("TaxonOutputGroupData togd ON td.outputGroupKey = togd.taxonGroupKey");
        ORDER_BY("taxonGroupName");
        return SQL();
    }
    
    public String filteredSelectSpecies(Map<String, Object> params) {
        BEGIN();
        SELECT("DISTINCT td.taxonVersionKey, prefnameTaxonVersionKey, name, authority, lang");
        createSelectQuery(params);
        if ("".equals((String) params.get("taxonOutputGroup"))) {
            INNER_JOIN("TaxonData td ON td.taxonVersionKey = o.pTaxonVersionKey");
        }
        ORDER_BY("name");
        return SQL();
    }
    
    private void createSelectQuery(Map<String, Object> params) {

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
        
        if (!"".equals((String) params.get("gridRef"))) {
            INNER_JOIN("GridTree gt ON gt.featureID = o.featureID");
            INNER_JOIN("GridSquareFeatureData gsfd ON gsfd.featureID = gt.parentFeatureID");
            WHERE("gsfd.label = #{gridRef}");
        }
        
        if (!"".equals((String) params.get("taxonOutputGroup"))) {
            INNER_JOIN("TaxonData td ON td.taxonVersionKey = o.pTaxonVersionKey");
            WHERE("td.outputGroupKey =  #{taxonOutputGroup}");
        }
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
