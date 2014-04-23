package uk.org.nbn.nbnv.api.dao.providers;

import java.util.Arrays;
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
    
    static void addVerifications(List<Integer> verificationKeys){
	WHERE(String.format("verification in (%s)", StringUtils.collectionToDelimitedString(verificationKeys,",")));
    }
    
    static void addYearRanges(List<String> bands){
	if(bands != null && !bands.isEmpty()){
	    String yearRange = "(YEAR(o.endDate) >= %s AND YEAR(o.startDate) <= %s)";
	    StringBuilder forWhere = new StringBuilder("(");
	    switch(bands.size()){
		case 3:
		    forWhere.append(String.format(yearRange,getStartYear(bands.get(2)).toString(),getEndYear(bands.get(2)).toString())).append(" OR ");
		case 2:
		    forWhere.append(String.format(yearRange,getStartYear(bands.get(1)).toString(),getEndYear(bands.get(1)).toString())).append(" OR ");
		case 1:
		    forWhere.append(String.format(yearRange,getStartYear(bands.get(0)).toString(),getEndYear(bands.get(0)).toString()));
	    }
	    forWhere.append(")");
	    WHERE(forWhere.toString());
	}else{
	    throw new IllegalArgumentException("No year band arguments supplied, a 'band' argument is required (eg band=2000-2012,ff0000,000000)");
	}
    }
    
    static void addStartDateFilter(String startDate) {
        WHERE("todsd.downloadTime >= '" + startDate + "'");
    }
    
    static void addEndDateFilter(String endDate) {
        WHERE("todsd.downloadTime <= '" + endDate + "'");
    }

    public static String datasetListToCommaList(List<String> list) {
        for (String d : list) {
            if (!d.matches("[A-Z0-9]{8}")) {
                throw new IllegalArgumentException("Non-dataset key in dataset argument: " + d);
            }
        }

        return "('" + StringUtils.collectionToDelimitedString(list, "','") + "')";
    }
    
    public static String IntegerListToCommaList(List<Integer> list) {
        return "('" + StringUtils.collectionToDelimitedString(list, ",") + "')";
    }
    
    public static String taxaListToCommaList(List<String> list) {
        for (String d : list) {
            if (!d.matches("[A-Z0-9]{16}")) {
                throw new IllegalArgumentException("Non-taxa key in taxa argument: " + d);
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
