package uk.org.nbn.nbnv.api.dao.providers;

import java.util.List;
import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.api.rest.resources.ObservationResourceDefaults;

/**
 *key
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
        SELECT("o.observationID, dad.label, dad.description, utoad.textValue");
        createSelectQuery(params);
        INNER_JOIN("UserTaxonObservationAttributeData utoad ON o.observationID = utoad.observationID");
        INNER_JOIN("DatasetAttributeData dad ON utoad.attributeID = dad.attributeID");
        WHERE("dad.attributeID = #{attributeID}");
        WHERE("utoad.userID = #{user.id}");
        return SQL();
    }
    
    public String filteredSelectGroups(Map<String, Object> params) {
        BEGIN();
        SELECT("taxonOutputGroupKey, COUNT(DISTINCT td.pTaxonVersionKey) querySpecificSpeciesCount");
        createSelectQuery(params);
        INNER_JOIN("TaxonTaxonGroupData td ON o.pTaxonVersionKey = td.pTaxonVersionKey");
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
    
    public String filteredSelectUnavailableDatasets(Map<String, Object> params){
        BEGIN();
        SELECT("tdd.datasetKey, COUNT(*) querySpecificObservationCount");
        createSelectQueryFromEnhancedRecords(params);
        INNER_JOIN("TaxonDatasetData tdd ON o.datasetKey = tdd.datasetKey");
        WHERE("tdd.publicResolutionID = 0");
        GROUP_BY("tdd.datasetKey");
        return SQL();
    }
    
    public String filteredSelectSitesForTVK(Map<String, Object> params){
        String spatialRelationship = ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT;
        if(params.containsKey("spatialRelationship") && params.get("spatialRelationship") != null){
            spatialRelationship = (String)params.get("spatialRelationship");
        }
        BEGIN();
        SELECT("DISTINCT sbd.featureID, sbd.name, sbd.providerKey, sbd.description, sbd.siteBoundaryDatasetKey, sbd.siteBoundaryCategoryID, fd.identifier");
        createSelectQuery(params);
        if(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_WITHIN.equals(spatialRelationship)){
            INNER_JOIN("FeatureContains fc ON o.featureID = fc.containedFeatureID");
            INNER_JOIN("FeatureData fd ON fc.featureID = fd.id");
        }else{
            INNER_JOIN("FeatureOverlaps fo ON o.featureID = fo.overlappedFeatureID");
            INNER_JOIN("FeatureData fd ON fo.featureID = fd.id");
        }
        INNER_JOIN("SiteBoundaryData sbd ON fd.id = sbd.featureID");
        return SQL();
    }
    
    public String filteredSelectDatasetsProviderNotInstantiated(Map<String, Object> params) {
        BEGIN();
        SELECT("DISTINCT o.datasetKey, dd.*");
        createSelectQuery(params);
        INNER_JOIN("DatasetData dd ON dd.\"key\" = o.datasetKey");
        ORDER_BY("dd.organisationName ASC, dd.title ASC");
        return SQL();
    }
    
    public String filteredSelectEnhancedRecordIDs(Map<String, Object> params) {
        BEGIN();
        SELECT("o.id");
        createSelectQueryFromEnhancedRecords(params);
        return SQL();        
    }
    
    private void createSelectQuery(Map<String, Object> params) {

        //FROM("UserTaxonObservationData o");
        FROM ("(SELECT obse.* FROM TaxonObservationDataEnhanced obse "
                + "INNER JOIN UserTaxonObservationID utoa ON utoa.observationID = obse.id "
                + "WHERE utoa.userID = #{user.id} "
                + "UNION ALL "
                + "SELECT obsp.* FROM TaxonObservationDataPublic obsp "
                + "WHERE obsp.id NOT IN ( "
                + "	SELECT utoa.observationID FROM UserTaxonObservationID utoa WHERE utoa.userID = #{user.id} "
                + ")) o");
        //WHERE("o.userID = #{user.id}");

        if (params.containsKey("startYear") && (Integer) params.get("startYear") > -1) {
            ProviderHelper.addStartYearFilter((Integer) params.get("startYear"));
        }

        if (params.containsKey("endYear") && (Integer) params.get("endYear") > -1) {
            ProviderHelper.addEndYearFilter((Integer) params.get("endYear"));
        }

        ProviderHelper.addDatasetKeysFilter(params);

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
            String spatialRelationship = ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT;
            if(params.containsKey("spatialRelationship") && params.get("spatialRelationship") != null){
                spatialRelationship = (String)params.get("spatialRelationship");
            }
            if(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_WITHIN.equals(spatialRelationship)){
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
    
    private void createSelectQueryFromEnhancedRecords(Map<String, Object> params) {

        FROM("TaxonObservationDataEnhanced o");

        if (params.containsKey("startYear") && (Integer) params.get("startYear") > -1) {
            ProviderHelper.addStartYearFilter((Integer) params.get("startYear"));
        }

        if (params.containsKey("endYear") && (Integer) params.get("endYear") > -1) {
            ProviderHelper.addEndYearFilter((Integer) params.get("endYear"));
        }

        ProviderHelper.addDatasetKeysFilter(params);
        
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
            String spatialRelationship = ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT;
            if(params.containsKey("spatialRelationship") && params.get("spatialRelationship") != null){
                spatialRelationship = (String)params.get("spatialRelationship");
            }
            if(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_WITHIN.equals(spatialRelationship)){
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

    private String taxaListToCommaList(List<String> list) {
        for (String d : list) {
            if (!d.matches("[A-Z0-9]{16}")) {
                throw new IllegalArgumentException("Non-taxon key in ptvk argument: " + d);
            }
        }

        return "('" + StringUtils.collectionToDelimitedString(list, "','") + "')";
    }
    
}
