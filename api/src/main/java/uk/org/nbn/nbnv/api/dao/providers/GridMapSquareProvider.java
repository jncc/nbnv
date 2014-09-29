package uk.org.nbn.nbnv.api.dao.providers;

import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
import uk.org.nbn.nbnv.api.rest.resources.ObservationResourceDefaults;

public class GridMapSquareProvider {

    public String searchForMatchingResolutions(Map<String, Object> params) {
        BEGIN();
        SELECT("g.identifier");
        FROM("GridSquareFeatureData g");
        WHERE("g.identifier LIKE '%" + params.get("term") + "%'");
        WHERE("g.resolution = '" + params.get("resolution") + "'");
        ORDER_BY("g.identifier");
        return SQL();
    }
    
    public String gridMapSquares(Map<String, Object> params) {
        BEGIN();
        SELECT("DISTINCT fd.identifier gridref");
        createGenericQuery(params);
        addYearBand(params);
        return SQL();
    }
    
    public String gridMapSquaresInspire(Map<String, Object> params) {
        BEGIN();
        SELECT("DISTINCT fd.identifier gridref");
        createInspireQuery(params);
        return SQL();
    }

    public String gridMapDatasets(Map<String, Object> params) {
        BEGIN();
        SELECT("DISTINCT dd.*, tdd.*, tdd.label publicResolution");
        createGenericQuery(params);
        INNER_JOIN("DatasetData dd ON o.datasetKey = dd.\"key\"");
        INNER_JOIN("TaxonDatasetData tdd ON dd.\"key\" = tdd.datasetKey");
        addYearRange((Integer) params.get("startYear"), (Integer) params.get("endYear"));
        return SQL();
    }

    private void createInspireQuery(Map<String, Object> params) {
        FROM("UserTaxonObservationData o");
        INNER_JOIN("GridTree gt ON o.featureID = gt.featureID");
        INNER_JOIN("TaxonTree tt ON tt.childPTVK = o.pTaxonVersionKey");
        INNER_JOIN("FeatureData fd ON gt.parentFeatureID = fd.id");
        INNER_JOIN("Resolution r ON fd.resolutionID = r.id");
        WHERE("o.userID = #{user.id}");
        WHERE("tt.nodePTVK = #{ptvk}");
        WHERE("r.label = #{resolution}");
        WHERE("o.absence = 0");
    }
    
    private void createGenericQuery(Map<String, Object> params) {
        FROM("UserTaxonObservationData o");
        INNER_JOIN("GridTree gt ON o.featureID = gt.featureID");
        INNER_JOIN("TaxonTree tt ON tt.childPTVK = o.pTaxonVersionKey");
        INNER_JOIN("FeatureData fd ON gt.parentFeatureID = fd.id");
        INNER_JOIN("Resolution r ON fd.resolutionID = r.id");
        addViceCounty(params);
        WHERE("o.userID = #{user.id}");
        WHERE("tt.nodePTVK = #{ptvk}");
        WHERE("r.label = #{resolution}");
        if (params.containsKey("absence")) {
            WHERE("o.absence = #{absence}");
        }
        ProviderHelper.addDatasetKeysFilter(params);
    }

    //Here is an example year band: 2000-2012,ff0000,000000
    private void addYearBand(Map<String, Object> params) {
        if (params.containsKey("band") && !params.get("band").equals("")) {
            addYearRange(ProviderHelper.getStartYear((String) params.get("band")), ProviderHelper.getEndYear((String) params.get("band")));
        } else {
            throw new IllegalArgumentException("No year band arguments supplied, a 'band' argument is required (eg band=2000-2012,ff0000,000000)");
        }
    }

    private void addYearRange(Integer startYear, Integer endYear) {
        ProviderHelper.addStartYearFilter(startYear);
        ProviderHelper.addEndYearFilter(endYear);
    }

    private void addViceCounty(Map<String, Object> params) {
        String viceCountyParamKey = "viceCountyIdentifier";
        boolean addViceCountyFilter = params.containsKey(viceCountyParamKey) && !params.get(viceCountyParamKey).equals(ObservationResourceDefaults.defaultFeatureID);
        if (addViceCountyFilter) {
            INNER_JOIN("FeatureOverlaps fo ON fd.id = fo.overlappedFeatureID");
            INNER_JOIN("FeatureData fd1 ON fo.featureID = fd1.id");
            WHERE("fd1.identifier = #{" + viceCountyParamKey + "}");
        }

    }
}
