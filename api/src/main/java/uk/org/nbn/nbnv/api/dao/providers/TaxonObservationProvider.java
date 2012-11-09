package uk.org.nbn.nbnv.api.dao.providers;

import java.util.List;
import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.api.rest.resources.TaxonObservationResource;

/**
 *
 * @author Paul Gilbertson
 */
public class TaxonObservationProvider {

    public String filteredSelectRecords(Map<String, Object> params) {
        BEGIN();
        SELECT("*");
        createSelectQuery(params);
        return SQL();
    }

    public String filteredSelectRecordsOrderedByDataset(Map<String, Object> params) {
        BEGIN();
        SELECT("*");
        createSelectQuery(params);
        ORDER_BY("datasetKey");
        return SQL();
    }

    public String filteredSelectOneAttribute(Map<String, Object> params) {
        BEGIN();
        SELECT("o.observationID, dad.label, dad.description, toad.textValue");
        INNER_JOIN("TaxonObservationAttributeData toad ON o.observationID = toad.observationID");
        INNER_JOIN("DatasetAttributeData dad ON toad.attributeID = dad.attributeID");
        WHERE("dad.attributeID = #{attributeID}");
        createSelectQuery(params);
        return SQL();
    }
    
    public String filteredSelectGroups(Map<String, Object> params) {
        BEGIN();
        SELECT("taxonOutputGroupKey, COUNT(DISTINCT td.pTaxonVersionKey) querySpecificSpeciesCount");
        createSelectQuery(params);
        INNER_JOIN("TaxonData td ON o.pTaxonVersionKey = td.pTaxonVersionKey");
        GROUP_BY("taxonOutputGroupKey");
        return SQL();
    }
    
    public String filteredSelectSpecies(Map<String, Object> params) {
        BEGIN();
        SELECT("o.pTaxonVersionKey, COUNT(*) querySpecificObservationCount");
        createSelectQuery(params);
        GROUP_BY("o.pTaxonVersionKey");
        return SQL();
    }
    
    public String filteredSelectDatasets(Map<String, Object> params){
        BEGIN();
        SELECT("datasetKey, COUNT(*) querySpecificObservationCount");
        createSelectQuery(params);
        GROUP_BY("datasetKey");
        return SQL();
    }
    
    public String filteredSelectDatasetsProviderNotInstantiated(Map<String, Object> params) {
        BEGIN();
        SELECT("DISTINCT o.datasetKey, dd.*");
        createSelectQuery(params);
        INNER_JOIN("DatasetData dd ON dd.\"key\" = o.datasetKey");
        return SQL();
    }
    
    private void createSelectQuery(Map<String, Object> params) {

        FROM("UserTaxonObservationData o");
        WHERE("userID = #{user.id}");

        if (params.containsKey("startYear") && (Integer) params.get("startYear") > -1) {
            WHERE("YEAR(endDate) >= #{startYear}");
        }

        if (params.containsKey("endYear") && (Integer) params.get("endYear") > -1) {
            WHERE("YEAR(startDate) <= #{endYear}");
        }

        if (params.containsKey("datasetKey") && !params.get("datasetKey").equals("")) {
            if(params.get("datasetKey") instanceof List){
                List<String> datasetArgs = (List<String>)params.get("datasetKey");
                if(datasetArgs.size() > 0 && !"".equals(datasetArgs.get(0))){
                    WHERE("datasetKey IN " + datasetListToCommaList((List<String>) params.get("datasetKey")));
                }
            }else{
                WHERE("o.datasetKey = '" + params.get("datasetKey") + "'");
            }
        }

        if (params.containsKey("ptvk") && !params.get("ptvk").equals("")) {
            if(params.get("ptvk") instanceof List){
                List<String> ptvkArgs = (List<String>)params.get("ptvk");
                if(ptvkArgs.size() > 0 && !"".equals(ptvkArgs.get(0))){
                    WHERE("pTaxonVersionKey IN " + taxaListToCommaList((List<String>) params.get("ptvk")));
                }
            }else{
                WHERE("pTaxonVersionKey = '" + params.get("ptvk") + "'");
            }
        }
        
        if(params.containsKey("featureID") && !params.get("featureID").equals("")){
            String spatialRelationship = TaxonObservationResource.SPATIAL_RELATIONSHIP_DEFAULT;
            if(params.containsKey("spatialRelationship") && params.get("spatialRelationship") != null){
                spatialRelationship = (String)params.get("spatialRelationship");
            }
            if(TaxonObservationResource.SPATIAL_RELATIONSHIP_WITHIN.equals(spatialRelationship)){
                INNER_JOIN("FeatureContains fc ON fc.containedFeatureID = o.featureID");
                INNER_JOIN("FeatureData fd ON fd.id = fc.featureID");
                WHERE("fd.identifier = #{featureID}");
            }else{
                INNER_JOIN("FeatureOverlaps fo ON fo.overlappedFeatureID = o.featureID");
                INNER_JOIN("FeatureData fd ON fd.id = fo.featureID");
                WHERE("fd.identifier = #{featureID}");
            }
        }
        
        if (params.containsKey("sensitive") && (Boolean) params.get("sensitive")) {
            WHERE("sensitive = #{sensitive}");
        }
            
        if (params.containsKey("designation") && !"".equals((String) params.get("designation"))) {
            INNER_JOIN("DesignationTaxonData dtd ON dtd.pTaxonVersionKey = o.pTaxonVersionKey");
            WHERE("dtd.code = #{designation}");
        }
        
        if (params.containsKey("gridRef") && !"".equals((String) params.get("gridRef"))) {
            INNER_JOIN("GridTree gt ON gt.featureID = o.featureID");
            INNER_JOIN("GridSquareFeatureData gsfd ON gsfd.id = gt.parentFeatureID");
            WHERE("gsfd.label = #{gridRef}");
        }
        
        if (params.containsKey("taxonOutputGroup") && !"".equals((String) params.get("taxonOutputGroup"))) {
            INNER_JOIN("TaxonData td ON td.taxonVersionKey = o.pTaxonVersionKey");
            WHERE("td.taxonOutputGroupKey =  #{taxonOutputGroup}");
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
