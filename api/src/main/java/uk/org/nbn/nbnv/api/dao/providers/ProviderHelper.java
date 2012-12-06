package uk.org.nbn.nbnv.api.dao.providers;

import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;
import static org.apache.ibatis.jdbc.SelectBuilder.*;

public class ProviderHelper {

    static void addDatasetKeysFilter(Map<String, Object> params) {
        if (params.containsKey("datasetKey") && !params.get("datasetKey").equals("")) {
            if (params.get("datasetKey") instanceof List) {
                List<String> datasetArgs = (List<String>) params.get("datasetKey");
                if (datasetArgs.size() > 0 && !"".equals(datasetArgs.get(0))) {
                    WHERE("o.datasetKey IN " + datasetListToCommaList((List<String>) params.get("datasetKey")));
                }
            } else {
                WHERE("o.datasetKey = '" + params.get("datasetKey") + "'");
            }
        }
    }

    static void addStartYearFilter(Integer startYear) {
        WHERE("YEAR(o.endDate) >= " + startYear);
    }

    static void addEndYearFilter(Integer endYear) {
        WHERE("YEAR(o.startDate) <= " + endYear);

    }

    public static String datasetListToCommaList(List<String> list) {
        for (String d : list) {
            if (!d.matches("[A-Z0-9]{8}")) {
                throw new IllegalArgumentException("Non-dataset key in dataset argument: " + d);
            }
        }

        return "('" + StringUtils.collectionToDelimitedString(list, "','") + "')";
    }
    
    public static Integer getStartYear(String band) {
        int delimIndex = band.indexOf("-");
        String startYear = band.substring(0, delimIndex);
        if (!startYear.matches("[0-9]{4}")) {
            throw new IllegalArgumentException("startYear is incorrect: " + startYear);
        }
        return Integer.parseInt(startYear);
    }

    public static Integer getEndYear(String band) {
        int delimIndex = band.indexOf("-");
        String endYear = band.substring(delimIndex + 1, delimIndex + 5);
        if (!endYear.matches("[0-9]{4}")) {
            throw new IllegalArgumentException("endYear is incorrect: " + endYear);
        }
        return Integer.parseInt(endYear);
    }

}
