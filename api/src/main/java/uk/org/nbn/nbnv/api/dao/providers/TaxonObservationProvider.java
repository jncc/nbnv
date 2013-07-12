package uk.org.nbn.nbnv.api.dao.providers;

import java.util.List;
import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.api.rest.resources.ObservationResourceDefaults;

/**
 * key
 *
 * @author Paul Gilbertson
 */
public class TaxonObservationProvider {

    public String filteredSelectRecords(Map<String, Object> params) {
        String from = createSelect(params, "o.*");
        BEGIN();
        SELECT("obs.*, obs.id AS observationID, f.label AS location, f.resolutionID, r.label AS resolution, si.providerKey AS siteKey, si.name AS siteName, pt.name AS pTaxonName, pt.authority AS pTaxonAuthority, rr.name AS recorder, rd.name AS determiner");
        FROM(from);
        INNER_JOIN("FeatureData f ON f.id = obs.featureID");
        INNER_JOIN("Resolution r ON r.id = f.resolutionID");
        INNER_JOIN("TaxonData pt ON pt.taxonVersionKey = obs.pTaxonVersionKey");
        LEFT_OUTER_JOIN("SiteData si ON si.id = obs.siteID");
        LEFT_OUTER_JOIN("RecorderData rr ON rr.id = obs.recorderID");
        LEFT_OUTER_JOIN("RecorderData rd ON rd.id = obs.determinerID");
        return SQL();
    }

    public String filteredSelectRequestableRecordIDs(Map<String, Object> params) {
        String from = createSelectEnhanced(params, "o.id");
        BEGIN();
        SELECT("obs.id");
        FROM(from);
        WHERE("obs.id NOT IN ( SELECT utoa.observationID FROM UserTaxonObservationID utoa WHERE utoa.userID = #{user.id} )");
        return SQL();
    }

    public String filteredSelectRequestableRecordIDsOrganisation(Map<String, Object> params) {
        String from = createSelectEnhanced(params, "o.id");
        BEGIN();
        SELECT("obs.id");
        FROM(from);
        WHERE("obs.id NOT IN ( SELECT utoa.observationID FROM OrganisationTaxonObservationID utoa WHERE utoa.organisationID = #{organisation.id} )");
        return SQL();
    }
    public String filteredSelectRecordsOrderedByDataset(Map<String, Object> params) {
        String from = createSelect(params, "o.*");
        BEGIN();
        SELECT("obs.*, obs.id AS observationID, f.label AS location, f.resolutionID, r.label AS resolution, si.providerKey AS siteKey, si.name AS siteName, pt.name AS pTaxonName, pt.authority AS pTaxonAuthority, rr.name AS recorder, rd.name AS determiner");
        FROM(from);
        INNER_JOIN("FeatureData f ON f.id = obs.featureID");
        INNER_JOIN("Resolution r ON r.id = f.resolutionID");
        INNER_JOIN("TaxonData pt ON pt.taxonVersionKey = obs.pTaxonVersionKey");
        LEFT_OUTER_JOIN("SiteData si ON si.id = obs.siteID");
        LEFT_OUTER_JOIN("RecorderData rr ON rr.id = obs.recorderID");
        LEFT_OUTER_JOIN("RecorderData rd ON rd.id = obs.determinerID");
        ORDER_BY("obs.datasetKey");
        return SQL();
    }

    public String filteredSelectOneAttribute(Map<String, Object> params) {
        String from = createSelect(params, "o.observationID");
        BEGIN();
        SELECT("obs.observationID, dad.label, dad.description, utoad.textValue");
        FROM(from);
        INNER_JOIN("UserTaxonObservationAttributeData utoad ON obs.observationID = utoad.observationID");
        INNER_JOIN("DatasetAttributeData dad ON utoad.attributeID = dad.attributeID");
        WHERE("dad.attributeID = #{attributeID}");
        WHERE("utoad.userID = #{user.id}");
        return SQL();
    }

    public String filteredSelectGroups(Map<String, Object> params) {
        String from = createSelect(params, "o.pTaxonVersionKey");
        BEGIN();
        SELECT("td.taxonOutputGroupKey, COUNT(DISTINCT td.pTaxonVersionKey) querySpecificSpeciesCount");
        FROM(from);
        INNER_JOIN("TaxonTaxonOutputGroupData td ON obs.pTaxonVersionKey = td.pTaxonVersionKey");
        GROUP_BY("td.taxonOutputGroupKey");
        return SQL();
    }

    public String filteredSelectSpecies(Map<String, Object> params) {
        String from = createSelect(params, "o.pTaxonVersionKey");
        BEGIN();
        SELECT("obs.pTaxonVersionKey, COUNT(*) querySpecificObservationCount");
        FROM(from);
        GROUP_BY("obs.pTaxonVersionKey");
        return SQL();
    }

    public String filteredSelectDatasets(Map<String, Object> params) {
        String from = createSelect(params, "o.datasetKey");
        BEGIN();
        SELECT("obs.datasetKey, COUNT(*) querySpecificObservationCount");
        FROM(from);
        GROUP_BY("obs.datasetKey");
        return SQL();
    }

    public String filteredSelectAllDatasets(Map<String, Object> params) {
        String from = createSelectEnhanced(params, "o.datasetKey");
        BEGIN();
        SELECT("obs.datasetKey, COUNT(*) querySpecificObservationCount");
        FROM(from);
        GROUP_BY("obs.datasetKey");
        return SQL();
    }

    public String filteredSelectRequestableDatasets(Map<String, Object> params) {
        String from = createSelectEnhanced(params, "o.datasetKey, o.sensitive, o.id");
        BEGIN();
        SELECT("obs.datasetKey, COUNT(*) querySpecificObservationCount, SUM(CAST(obs.sensitive AS int)) querySpecificSensitiveObservationCount");
        FROM(from);
        WHERE("obs.id NOT IN ( SELECT utoa.observationID FROM UserTaxonObservationID utoa WHERE utoa.userID = #{user.id} )");
        GROUP_BY("obs.datasetKey");
        return SQL();
    }
        
    public String filteredSelectRequestableDatasetsOrganisation(Map<String, Object> params) {
        String from = createSelectEnhanced(params, "o.datasetKey, o.sensitive, o.id");
        BEGIN();
        SELECT("obs.datasetKey, COUNT(*) querySpecificObservationCount, SUM(CAST(obs.sensitive AS int)) querySpecificSensitiveObservationCount");
        FROM(from);
        WHERE("obs.id NOT IN ( SELECT utoa.observationID FROM OrganisationTaxonObservationID utoa WHERE utoa.organisationID = #{organisation.id} )");
        GROUP_BY("obs.datasetKey");
        return SQL();
    }

    public String filteredSelectRequestableSensitiveDatasets(Map<String, Object> params) {
        params.put("sensitive", Boolean.TRUE);
        String from = createSelectEnhanced(params, "o.datasetKey, o.sensitive, o.id");
        BEGIN();
        SELECT("obs.datasetKey, COUNT(*) querySpecificObservationCount");
        FROM(from);
        WHERE("obs.id NOT IN ( SELECT utoa.observationID FROM UserTaxonObservationID utoa WHERE utoa.userID = #{user.id} )");
        WHERE("obs.sensitive = 1");
        GROUP_BY("obs.datasetKey");
        return SQL();
    }

    public String filteredSelectRequestableSensitiveDatasetsOrganisation(Map<String, Object> params) {
        params.put("sensitive", Boolean.TRUE);
        String from = createSelectEnhanced(params, "o.datasetKey, o.sensitive, o.id");
        BEGIN();
        SELECT("obs.datasetKey, COUNT(*) querySpecificObservationCount");
        FROM(from);
        WHERE("obs.id NOT IN ( SELECT utoa.observationID FROM OrganisationTaxonObservationID utoa WHERE utoa.organisationID = #{organisation.id} )");
        WHERE("obs.sensitive = 1");
        GROUP_BY("obs.datasetKey");
        return SQL();
    }

    public String filteredSelectUnavailableDatasets(Map<String, Object> params) {
        String from = createSelectEnhanced(params, "o.datasetKey, o.id");
        BEGIN();
        SELECT("tdd.datasetKey, COUNT(*) querySpecificObservationCount");
        FROM(from);
        INNER_JOIN("TaxonDatasetData tdd ON obs.datasetKey = tdd.datasetKey");
        WHERE("tdd.publicResolutionID = 0");
        GROUP_BY("tdd.datasetKey");
        return SQL();
    }

    public String filteredSelectSitesForTVK(Map<String, Object> params) {
        String spatialRelationship = ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT;
        if (params.containsKey("spatialRelationship") && params.get("spatialRelationship") != null) {
            spatialRelationship = (String) params.get("spatialRelationship");
        }
        String from = createSelect(params, "o.featureID");
        BEGIN();
        SELECT("DISTINCT sbd.featureID, sbd.name, sbd.providerKey, sbd.description, sbd.siteBoundaryDatasetKey, sbd.siteBoundaryCategoryID, fd.identifier");
        FROM(from);
        if (ObservationResourceDefaults.SPATIAL_RELATIONSHIP_WITHIN.equals(spatialRelationship)) {
            INNER_JOIN("FeatureContains fc ON obs.featureID = fc.containedFeatureID");
            INNER_JOIN("FeatureData fd ON fc.featureID = fd.id");
        } else {
            INNER_JOIN("FeatureOverlaps fo ON obs.featureID = fo.overlappedFeatureID");
            INNER_JOIN("FeatureData fd ON fo.featureID = fd.id");
        }
        INNER_JOIN("SiteBoundaryData sbd ON fd.id = sbd.featureID");
        return SQL();
    }

    public String filteredSelectDatasetsProviderNotInstantiated(Map<String, Object> params) {
        String from = createSelect(params, "o.datasetKey");
        BEGIN();
        SELECT("DISTINCT obs.datasetKey, dd.*");
        FROM(from);
        INNER_JOIN("DatasetData dd ON dd.\"key\" = obs.datasetKey");
        ORDER_BY("dd.organisationName ASC, dd.title ASC");
        return SQL();
    }

    public String filteredSelectEnhancedRecordIDs(Map<String, Object> params) {
        String from = createSelectEnhanced(params, "o.id");
        BEGIN();
        SELECT("obs.id");
        FROM(from);
        return SQL();
    }

    private String createSelect(Map<String, Object> params, String fields) {
        String publicSelect = createSelectQuery(params, false, fields);
        String fullSelect = createSelectQuery(params, true, fields);
        return "(" + fullSelect + " UNION ALL " + publicSelect + ") obs";
    }

    private String createSelectEnhanced(Map<String, Object> params, String fields) {
        String fullSelect = createSelectAllRecordsQuery(params, fields);
        return "(" + fullSelect + ") obs";
    }

    private String createSelectQuery(Map<String, Object> params, boolean full, String fields) {
        BEGIN();
        SELECT(fields);
        if (full) {
            FROM("TaxonObservationDataEnhanced o");
            INNER_JOIN("UserTaxonObservationID utoa ON utoa.observationID = o.id ");
            WHERE("utoa.userID = #{user.id}");
        } else {
            FROM("TaxonObservationDataPublic o");
            WHERE("o.id NOT IN ( SELECT utoa.observationID FROM UserTaxonObservationID utoa WHERE utoa.userID = #{user.id} )");
        }

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
                    WHERE("pTaxonVersionKey IN " + taxaListToCommaList((List<String>) params.get("ptvk")));
                }
            } else {
                WHERE("pTaxonVersionKey = '" + params.get("ptvk") + "'");
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
            INNER_JOIN("FeatureData ftd ON ftd.id = o.featureID");
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

        if (params.containsKey("designation") && params.get("designation") != null && !"".equals((String) params.get("designation"))) {
            INNER_JOIN("DesignationTaxonData dtd ON dtd.pTaxonVersionKey = o.pTaxonVersionKey");
            WHERE("dtd.code = #{designation}");
        }

        if (params.containsKey("gridRef") && params.get("gridRef") != null && !"".equals((String) params.get("gridRef"))) {
            INNER_JOIN("GridTree gt ON gt.featureID = o.featureID");
            INNER_JOIN("GridSquareFeatureData gsfd ON gsfd.id = gt.parentFeatureID");
            WHERE("gsfd.label = #{gridRef}");
        }

        if (params.containsKey("taxonOutputGroup") && params.get("taxonOutputGroup") != null && !"".equals((String) params.get("taxonOutputGroup"))) {
            INNER_JOIN("TaxonData td ON td.taxonVersionKey = o.pTaxonVersionKey");
            WHERE("td.taxonOutputGroupKey =  #{taxonOutputGroup}");
        }

        return SQL();
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
                    WHERE("pTaxonVersionKey IN " + taxaListToCommaList((List<String>) params.get("ptvk")));
                }
            } else {
                WHERE("pTaxonVersionKey = '" + params.get("ptvk") + "'");
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
            INNER_JOIN("FeatureData ftd ON ftd.id = o.featureID");
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

        if (params.containsKey("designation") && params.get("designation") != null && !"".equals((String) params.get("designation"))) {
            INNER_JOIN("DesignationTaxonData dtd ON dtd.pTaxonVersionKey = o.pTaxonVersionKey");
            WHERE("dtd.code = #{designation}");
        }

        if (params.containsKey("gridRef") && params.get("gridRef") != null && !"".equals((String) params.get("gridRef"))) {
            INNER_JOIN("GridTree gt ON gt.featureID = o.featureID");
            INNER_JOIN("GridSquareFeatureData gsfd ON gsfd.id = gt.parentFeatureID");
            WHERE("gsfd.label = #{gridRef}");
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
