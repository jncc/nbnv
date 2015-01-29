package uk.org.nbn.nbnv.api.dao.providers;

import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
import uk.org.nbn.nbnv.api.utils.ObservationResourceDefaults;

public class GridMapSquareProvider {

    private static final TaxonObservationProvider TAXON_OBSERVATION_PROVIDER = new TaxonObservationProvider();

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
        String from = TAXON_OBSERVATION_PROVIDER.createSelect(params, "o.featureID");

        BEGIN();
        SELECT("DISTINCT fd.identifier gridref");
        createGenericQuery(params, from);
        return SQL();
    }

    public String gridMapSquaresInspire(Map<String, Object> params) {
        String from = TAXON_OBSERVATION_PROVIDER.createSelect(params, "*");

        BEGIN();
        SELECT("DISTINCT fd.identifier gridref");
        createInspireQuery(params, from);
        return SQL();
    }

    public String gridMapDatasets(Map<String, Object> params) {
        String from = TAXON_OBSERVATION_PROVIDER.createSelect(params, "o.datasetKey, o.featureID");

        BEGIN();
        SELECT("DISTINCT dd.*, tdd.*, tdd.label publicResolution");
        createGenericQuery(params, from);
        INNER_JOIN("DatasetData dd ON obs.datasetKey = dd.\"key\"");
        INNER_JOIN("TaxonDatasetData tdd ON dd.\"key\" = tdd.datasetKey");
        return SQL();
    }

    private void createInspireQuery(Map<String, Object> params, String from) {
        FROM(from);
        INNER_JOIN("GridTree gt ON obs.featureID = gt.featureID");
        INNER_JOIN("FeatureData fd ON gt.parentFeatureID = fd.id");
        INNER_JOIN("Resolution r ON fd.resolutionID = r.id");
        WHERE("r.label = #{resolution}");
        WHERE("obs.absence = 0");
    }

    private void createGenericQuery(Map<String, Object> params, String from) {
        FROM(from);
        INNER_JOIN("GridTree gt ON obs.featureID = gt.featureID");
        INNER_JOIN("FeatureData fd ON gt.parentFeatureID = fd.id");
        INNER_JOIN("Resolution r ON fd.resolutionID = r.id");
        addViceCounty(params);
        WHERE("r.label = #{resolution}");
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
