package uk.org.nbn.nbnv.api.dao.providers;

import java.util.List;
import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
import org.springframework.util.StringUtils;

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
        SELECT("DISTINCT dd.*");
        createGenericQuery(params);
        INNER_JOIN("DatasetData dd ON o.datasetKey = dd.\"key\"");
        addYearRange((Integer)params.get("startYear"), (Integer)params.get("endYear"));
        return SQL();
    }
    
    private void createGenericQuery(Map<String, Object> params){
        FROM("UserTaxonObservationData o");
        INNER_JOIN("GridTree gt ON o.featureID = gt.featureID");
        INNER_JOIN("FeatureData fd ON gt.parentFeatureID = fd.id");
        INNER_JOIN("Resolution r ON fd.resolutionID = r.id");
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

}
