package uk.org.nbn.nbnv.api.dao.providers;

import java.util.List;
import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.api.dao.warehouse.FeatureMapper;
import uk.org.nbn.nbnv.api.model.Feature;
import uk.org.nbn.nbnv.api.rest.resources.ObservationResourceDefaults;

public class GridMapSquareProvider {

    public String gridMapSquares(Map<String, Object> params) {
        BEGIN();
        SELECT("DISTINCT gridRef");
        createGenericQuery(params);
        addYearBand(params);
        return SQL();
    }

    public String gridMapDatasets(Map<String, Object> params) {
        BEGIN();
        SELECT("DISTINCT dd.*, tdd.*");
        createGenericQuery(params);
        INNER_JOIN("DatasetData dd ON o.datasetKey = dd.\"key\"");
        INNER_JOIN("TaxonDatasetData tdd ON dd.\"key\" = tdd.datasetKey");
        addYearRange((Integer) params.get("startYear"), (Integer) params.get("endYear"));
        return SQL();
    }

    private void createGenericQuery(Map<String, Object> params) {
        FROM("UserTaxonObservationData o");
        INNER_JOIN("GridTree gt ON o.featureID = gt.featureID");
        INNER_JOIN("FeatureData fd ON gt.parentFeatureID = fd.id");
        INNER_JOIN("Resolution r ON fd.resolutionID = r.id");
        addViceCounty(params);
        WHERE("o.userID = #{user.id}");
        WHERE("o.pTaxonVersionKey = #{ptvk}");
        WHERE("r.label = #{resolution}");
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
