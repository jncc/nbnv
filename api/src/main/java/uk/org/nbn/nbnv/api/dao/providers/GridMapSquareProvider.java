package uk.org.nbn.nbnv.api.dao.providers;

import java.util.List;
import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
import org.springframework.util.StringUtils;

public class GridMapSquareProvider {

    public String gridMapSquares(Map<String, Object> params) {
        BEGIN();
        SELECT("DISTINCT gridRef");
        FROM("UserTaxonObservationData o");
        INNER_JOIN("GridTree gt ON o.featureID = gt.featureID");
        INNER_JOIN("FeatureData fd ON gt.parentFeatureID = fd.id");
        INNER_JOIN("Resolution r ON fd.resolutionID = r.id");
        WHERE("o.userID = #{user.id}");
        WHERE("o.pTaxonVersionKey = #{ptvk}");
        WHERE("r.label = #{resolution}");
        ProviderHelper.addDatasetKeysFilter(params);
        addYearBand(params);
        return SQL();
    }

    //Here is an example year band: 2000-2012,ff0000,000000
    private void addYearBand(Map<String, Object> params) {
        if (params.containsKey("band") && !params.get("band").equals("")) {
            addYearRange(getStartYear((String) params.get("band")), getEndYear((String) params.get("band")));
        } else {
            throw new IllegalArgumentException("No year band arguments supplied, a 'band' argument is required (eg band=2000-2012,ff0000,000000)");
        }
    }

    private void addYearRange(Integer startYear, Integer endYear) {
        ProviderHelper.addStartYearFilter(startYear);
        ProviderHelper.addEndYearFilter(endYear);
    }

    private Integer getStartYear(String band) {
        int delimIndex = band.indexOf("-");
        String startYear = band.substring(0, delimIndex);
        if (!startYear.matches("[0-9]{4}")) {
            throw new IllegalArgumentException("startYear is incorrect: " + startYear);
        }
        return Integer.parseInt(startYear);
    }

    private Integer getEndYear(String band) {
        int delimIndex = band.indexOf("-");
        String endYear = band.substring(delimIndex + 1, delimIndex + 5);
        if (!endYear.matches("[0-9]{4}")) {
            throw new IllegalArgumentException("endYear is incorrect: " + endYear);
        }
        return Integer.parseInt(endYear);
    }
}
