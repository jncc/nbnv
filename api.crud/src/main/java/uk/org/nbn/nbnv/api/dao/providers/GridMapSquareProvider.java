package uk.org.nbn.nbnv.api.dao.providers;

import java.util.List;
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
		addYearBandOrVerificationStatus(params);
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

    private void addYearBandOrVerificationStatus(Map<String, Object> params){
	       List<String> bands = null;
	List<Integer> verificationKeys = null;
	Boolean isGroupByDate = null;
	if(params.containsKey("bands")){
	    bands = (List<String>)params.get("bands");
	}
	if (bands == null || bands.isEmpty()) {
            System.out.println("No year band arguments supplied, a 'band' argument is required (eg band=2000-2012,ff0000,000000)");
	    throw new IllegalArgumentException("No year band arguments supplied, a 'band' argument is required (eg band=2000-2012,ff0000,000000)");
	}
	if(params.containsKey("verificationKeys")){
	    verificationKeys = (List<Integer>)params.get("verificationKeys");
	}
	if (verificationKeys == null || verificationKeys.isEmpty()) {
            System.out.println("No verification keys supplied, at least one is required (values are 1-4)");
	    throw new IllegalArgumentException("No verification keys supplied, at least one is required (values are 1-4)");
	}
	if(params.containsKey("isGroupByDate")){
	    isGroupByDate = (Boolean)params.get("isGroupByDate");
	}else{
            System.out.println("No isGroupByDate argument supplied.");
	    throw new IllegalArgumentException("No isGroupByDate argument supplied.");
	}
	if(isGroupByDate){
	    addYearRange(ProviderHelper.getStartYear(bands.get(0)), ProviderHelper.getEndYear(bands.get(0)));
	}else{
	    ProviderHelper.addYearRanges(bands);
	}
	ProviderHelper.addVerifications(verificationKeys);
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
