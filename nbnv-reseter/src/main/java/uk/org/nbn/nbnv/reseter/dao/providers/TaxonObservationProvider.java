package uk.org.nbn.nbnv.reseter.dao.providers;

import java.util.List;
import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.reseter.utils.ObservationResourceDefaults;

/**
 * key
 *
 * @author Paul Gilbertson
 */
public class TaxonObservationProvider {

    String createSelectEnhanced(Map<String, Object> params, String fields) {
        String fullSelect = createSelectAllRecordsQuery(params, fields);
        return "(" + fullSelect + ") obs";
    }

    private String createSelectAllRecordsQuery(Map<String, Object> params, String fields) {
        BEGIN();
        SELECT(fields);
        FROM("TaxonObservationDataEnhanced o");

        if (params.containsKey("startYear") && (Integer) params.get("startYear") > -1) {
            ProviderHelper.addStartYearFilter((Integer) params.get("startYear"));
        }

        if (params.containsKey("endYear") && (Integer) params.get("endYear") > -1) {
            ProviderHelper.addEndYearFilter((Integer) params.get("endYear"));
        }

        ProviderHelper.addDatasetKeysFilter(params);

        if (params.containsKey("ptvk") && params.get("ptvk") != null && !params.get("ptvk").equals("")) {
            if (params.get("ptvk") instanceof List) {
                List<String> ptvkArgs = (List<String>) params.get("ptvk");
                if (ptvkArgs.size() > 0 && !"".equals(ptvkArgs.get(0))) {
                    INNER_JOIN("TaxonTree tt ON tt.childPTVK = o.pTaxonVersionKey");
                    WHERE("tt.nodePTVK IN " + taxaListToCommaList((List<String>) params.get("ptvk")));
                }
            } else {
                INNER_JOIN("TaxonTree tt ON tt.childPTVK = o.pTaxonVersionKey");
                WHERE("tt.nodePTVK = '" + params.get("ptvk") + "'");
            }
        }

        if (params.containsKey("featureID") && params.get("featureID") != null && !params.get("featureID").equals("")) {
            String spatialRelationship = ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT;
            if (params.containsKey("spatialRelationship") && params.get("spatialRelationship") != null) {
                spatialRelationship = (String) params.get("spatialRelationship");
            }
            if (ObservationResourceDefaults.SPATIAL_RELATIONSHIP_WITHIN.equals(spatialRelationship)) {
                INNER_JOIN("FeatureContains fc ON fc.containedFeatureID = o.featureID");
                INNER_JOIN("FeatureIdentifierData fd ON fd.id = fc.featureID");
                WHERE("fd.identifier = #{featureID}");
            } else {
                INNER_JOIN("FeatureOverlaps fo ON fo.overlappedFeatureID = o.featureID");
                INNER_JOIN("FeatureIdentifierData fd ON fd.id = fo.featureID");
                WHERE("fd.identifier = #{featureID}");
            }
        }

        if (params.containsKey("polygon") && params.get("polygon") != null && !params.get("polygon").equals("")) {
            String spatialRelationship = ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT;
            if (params.containsKey("spatialRelationship") && params.get("spatialRelationship") != null) {
                spatialRelationship = (String) params.get("spatialRelationship");
            }
            INNER_JOIN("Feature ftd WITH(INDEX(sidx_Feature_geom)) ON ftd.id = o.featureID");
            if (ObservationResourceDefaults.SPATIAL_RELATIONSHIP_WITHIN.equals(spatialRelationship)) {
                WHERE("ftd.geom.STWithin(geometry::STGeomFromText(#{polygon}, 4326)) = 1");
            } else {
                WHERE("ftd.geom.STIntersects(geometry::STGeomFromText(#{polygon}, 4326)) = 1");
                WHERE("ftd.geom.STTouches(geometry::STGeomFromText(#{polygon}, 4326)) = 0");
            }            
        }

        if (params.containsKey("sensitive") && (Boolean) params.get("sensitive")) {
            WHERE("sensitive <= 1");
        } else {
            WHERE("sensitive = 0");
        }

        if (params.containsKey("gridRef") && params.get("gridRef") != null && !"".equals((String) params.get("gridRef"))) {
            INNER_JOIN("GridTree gt ON gt.featureID = o.featureID");
            INNER_JOIN("GridSquareFeatureData gsfd ON gsfd.id = gt.parentFeatureID");
            WHERE("gsfd.label = #{gridRef}");
        }

        if (params.containsKey("designation") && params.get("designation") != null && !"".equals((String) params.get("designation"))) {
            INNER_JOIN("TaxonTree ttdtd ON ttdtd.childPTVK = o.pTaxonVersionKey");
            INNER_JOIN("DesignationTaxonData dtd ON dtd.pTaxonVersionKey = ttdtd.nodePTVK");
            WHERE("dtd.code = #{designation}");
        }
        
        if (params.containsKey("orgSuppliedList") && (Integer) params.get("orgSuppliedList") > 0) {
            INNER_JOIN("TaxonTree tttostl ON tttostl.childPTVK = o.pTaxonVersionKey");
            INNER_JOIN("TaxonOrganisationSuppliedTaxonList tostl ON tostl.pTaxonVersionKey = tttostl.nodePTVK");
            WHERE("tostl.orgListID = #{orgSuppliedList}");    
        }
        
        if (params.containsKey("taxonOutputGroup") && params.get("taxonOutputGroup") != null && !"".equals((String) params.get("taxonOutputGroup"))) {
            INNER_JOIN("TaxonData td ON td.taxonVersionKey = o.pTaxonVersionKey");
            WHERE("td.taxonOutputGroupKey =  #{taxonOutputGroup}");
        }

        return SQL();
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
