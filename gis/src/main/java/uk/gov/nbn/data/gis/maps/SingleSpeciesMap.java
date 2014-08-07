package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.WebResource;
import java.awt.Color;
import java.util.*;
import javax.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.nbn.data.gis.maps.MapHelper.LayerDataGenerator;
import uk.gov.nbn.data.gis.maps.colour.Band;
import uk.org.nbn.nbnv.api.model.User;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Qualifier;
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ceh.dynamo.GridMap;
import uk.ac.ceh.dynamo.GridMap.GridLayer;
import uk.ac.ceh.dynamo.GridMap.Layer;
import uk.ac.ceh.dynamo.GridMap.Resolution;
import uk.ac.ceh.dynamo.arguments.annotations.ServiceURL;
import uk.gov.nbn.data.gis.maps.colour.Verification;
import uk.ac.ceh.dynamo.bread.BreadException;
import uk.ac.ceh.dynamo.bread.ShapefileBakery;
import uk.gov.nbn.data.gis.validation.Datasets;

/**
 * The following represents a Map service for SingleSpecies
 * 
 * It is configured to take the following filters :
 *  startyear
 *  endyear
 *  datasets
 *  taxonVersionKey (As part of the url call)
 * @author Christopher Johnson
 */
@Controller
@Validated
@RequestMapping("SingleSpecies")
public class SingleSpeciesMap {
    private static final String TEN_KM_LAYER_NAME = "Grid-10km";
    private static final String TWO_KM_LAYER_NAME = "Grid-2km";
    private static final String ONE_KM_LAYER_NAME = "Grid-1km";
    private static final String ONE_HUNDRED_M_LAYER_NAME = "Grid-100m";
    private static final String NONE_GRID_LAYER_NAME = "None-Grid";
    private static final Map<String,Color> COLOURS;
    private static final Map<String, Integer> LAYERS;
    private static final List<Verification> DEFAULT_VERIFICATION;
    
    @Autowired WebResource resource;
    @Autowired Properties properties;
    @Autowired @Qualifier("taxonLayerBaker") ShapefileBakery bakery;
    
    static {
        COLOURS = new HashMap<String, Color>();
        COLOURS.put(TEN_KM_LAYER_NAME, new Color(255, 255, 0));
        COLOURS.put(TWO_KM_LAYER_NAME, new Color(0, 255, 0));
        COLOURS.put(ONE_KM_LAYER_NAME, new Color(0, 255, 255));
        COLOURS.put(ONE_HUNDRED_M_LAYER_NAME, new Color(255, 0, 0));
        COLOURS.put(NONE_GRID_LAYER_NAME, new Color(255, 0, 255));
        
        LAYERS = new HashMap<String, Integer>();
        LAYERS.put(TEN_KM_LAYER_NAME, 1);
        LAYERS.put(TWO_KM_LAYER_NAME, 2);
        LAYERS.put(ONE_KM_LAYER_NAME, 3);
        LAYERS.put(ONE_HUNDRED_M_LAYER_NAME, 4);
        LAYERS.put(NONE_GRID_LAYER_NAME, -1);
        
        DEFAULT_VERIFICATION = new ArrayList<Verification>();
        DEFAULT_VERIFICATION.add(new Verification("4,cccccc,000000"));
        DEFAULT_VERIFICATION.add(new Verification("3,cccccc,000000"));
        DEFAULT_VERIFICATION.add(new Verification("1,0000ff,000000"));
    }
    
    @RequestMapping("{taxonVersionKey}")
    @GridMap(
        layers={
            @GridLayer(name="10km",     layer=TEN_KM_LAYER_NAME,        resolution=Resolution.TEN_KM),
            @GridLayer(name="2km",      layer=TWO_KM_LAYER_NAME,        resolution=Resolution.TWO_KM),
            @GridLayer(name="1km",      layer=ONE_KM_LAYER_NAME,        resolution=Resolution.ONE_KM),
            @GridLayer(name="100m",     layer=ONE_HUNDRED_M_LAYER_NAME, resolution=Resolution.ONE_HUNDRED_METERS)
        },
        defaultLayer="10km",
        overlays=@Layer(name="feature", layers="Selected-Feature" )
    )
    public ModelAndView getSingleSpeciesModel(
            final User user,
            @ServiceURL String mapServiceURL,
            @PathVariable("taxonVersionKey") @Pattern(regexp="[A-Z][A-Z0-9]{15}") final String key,
            @RequestParam(value="datasets", required=false) @Datasets final List<String> datasetKeys,
            @RequestParam(value="startyear", required=false) @Pattern(regexp="[0-9]{4}") final String startYear,
            @RequestParam(value="endyear", required=false) @Pattern(regexp="[0-9]{4}") final String endYear,
            @RequestParam(value="abundance", required=false, defaultValue="presence") @Pattern(regexp="(all)|(presence)|(absence)") String abundance,
            @RequestParam(value="feature", required=false) String featureID,
            @RequestParam(value="band", required=false) List<Band> bands,
	    @RequestParam(value="verification", required=false) final List<Verification> verifications
            ) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        boolean absence = abundance.equals("all") || abundance.equals("absence");
        boolean presence = abundance.equals("all") || abundance.equals("presence");
        data.put("layers", LAYERS.keySet());
        data.put("colours", COLOURS);
        data.put("osRequiredLayers", getOSRequiredLayers(LAYERS.keySet(), presence, absence, bands));
        data.put("enableAbsence", absence);
        data.put("enablePresence", presence);
        
        if (bands == null && verifications == null) {
            data.put("verifications", DEFAULT_VERIFICATION);    
            data.put("bands", null);
        } else if (verifications != null) {
            // Sort to be always in the correct order rather than relying on the
            // user
            Collections.sort(verifications);
            data.put("verifications", verifications);
            data.put("bands", null);
        } else {
            data.put("verifications", null);
            data.put("bands", bands);
        }
        //data.put("bands", bands);
        //data.put("verifications", verifications);        
            
        data.put("mapServiceURL", mapServiceURL);
        data.put("featureData", MapHelper.getSelectedFeatureData(featureID));
        data.put("properties", properties);
        data.put("absenceLayerGenerator", getSingleSpeciesResolutionDataGenerator(bakery, FEATURE.GEOM, key, user, datasetKeys, startYear, endYear, true, getVerificationKeys(verifications)));
        data.put("presencelayerGenerator", getSingleSpeciesResolutionDataGenerator(bakery, FEATURE.GEOM, key, user, datasetKeys, startYear, endYear, false, getVerificationKeys(verifications)));
        data.put("bandLayerGenerator", new SingleSpeciesBandSqlGenerator() {
            @Override public String getData(String layerName, Band dateBand) throws BreadException {
		return bakery.getData(getSQL(FEATURE.GEOM, key, user, datasetKeys, dateBand.getStartYear(), dateBand.getEndYear(), false, layerName, getVerificationKeys(verifications), null));
            }
        });
        data.put("verificationLayerGenerator", new SingleSpeciesVerificationSqlGenerator() {
            @Override public String getData(String layerName, List<Band> dateBands, Verification verificationStatus) throws BreadException {
                return bakery.getData(getSQL(FEATURE.GEOM, key, user, datasetKeys, null, null, false, layerName, Arrays.asList(verificationStatus.getStatus().getKey()), dateBands));
            }
        });
        return new ModelAndView("SingleSpecies.map",data);
    }
    
    public interface SingleSpeciesBandSqlGenerator {
        public String getData(String layerName, Band dateBand) throws BreadException;
    }
    
    public interface SingleSpeciesVerificationSqlGenerator {
        public String getData(String layerName, List<Band> dateBands, Verification verificationStatus) throws BreadException;
    }
    
    //Factored out the single species resolution data generator so that it can be used by the atlas map
    static LayerDataGenerator getSingleSpeciesResolutionDataGenerator(
            final ShapefileBakery bakery,
            final Field<?> geometry,
            final String taxonKey, 
            final User user, 
            final List<String> datasetKeys, 
            final String startYear, 
            final String endYear,
            final boolean absence,
	    final List<Integer> verificationKeys) {
            return new LayerDataGenerator() {
                @Override public String getData(String layerName) throws BreadException {
                    return bakery.getData(getSQL(geometry, taxonKey, user, datasetKeys, startYear, endYear, absence, layerName, verificationKeys, null));
                }
            };
    }      
    
    private static String getSQL(   Field<?> geometry,
                                    String taxonKey, User user, 
                                    List<String> datasetKeys, 
                                    String startYear, String endYear, 
                                    boolean absence, String layerName,
									List<Integer> verificationKeys,
								List<Band> dateBands) {
	boolean isMultipleDates = !(dateBands == null || dateBands.isEmpty());
        DSLContext create = MapHelper.getContext();
        Condition publicCondition = TAXONTREE.NODEPTVK.eq(taxonKey)
			.and(MAPPINGDATAPUBLIC.ABSENCE.eq(absence))
			.and(MAPPINGDATAPUBLIC.RESOLUTIONID.eq(LAYERS.get(layerName)));
		if(isMultipleDates){
			publicCondition = MapHelper.createTemporalSegments(publicCondition, dateBands, MAPPINGDATAPUBLIC.STARTDATE, MAPPINGDATAPUBLIC.ENDDATE);
		}else{
			publicCondition = MapHelper.createTemporalSegment(publicCondition, startYear, endYear, MAPPINGDATAPUBLIC.STARTDATE, MAPPINGDATAPUBLIC.ENDDATE);
		}
			publicCondition = MapHelper.createInDatasetsSegment(publicCondition, MAPPINGDATAPUBLIC.DATASETKEY, datasetKeys);
		publicCondition = MapHelper.createInVerificationKeysSegment(publicCondition, MAPPINGDATAPUBLIC.VERIFICATIONID, verificationKeys);

        Select<Record1<Integer>> nested = create
                    .select(MAPPINGDATAPUBLIC.FEATUREID.as("FEATUREID"))
                    .from(MAPPINGDATAPUBLIC)
                    .join(TAXONTREE).on(TAXONTREE.CHILDPTVK.eq(MAPPINGDATAPUBLIC.PTAXONVERSIONKEY))
                    .where(publicCondition);
        
        if (!User.PUBLIC_USER.equals(user))  {
            Condition enhancedCondition = TAXONTREE.NODEPTVK.eq(taxonKey)
                                    .and(USERTAXONOBSERVATIONID.USERID.eq(user.getId()))
                                    .and(MAPPINGDATAENHANCED.ABSENCE.eq(absence))
                                    .and(MAPPINGDATAENHANCED.RESOLUTIONID.eq(LAYERS.get(layerName)));
            if(isMultipleDates){
                    enhancedCondition = MapHelper.createTemporalSegments(enhancedCondition, dateBands, MAPPINGDATAENHANCED.STARTDATE, MAPPINGDATAENHANCED.ENDDATE);
            }else{
                    enhancedCondition = MapHelper.createTemporalSegment(enhancedCondition, startYear, endYear, MAPPINGDATAENHANCED.STARTDATE, MAPPINGDATAENHANCED.ENDDATE);
            }
            enhancedCondition = MapHelper.createInDatasetsSegment(enhancedCondition, MAPPINGDATAENHANCED.DATASETKEY, datasetKeys);
            enhancedCondition = MapHelper.createInVerificationKeysSegment(enhancedCondition, MAPPINGDATAENHANCED.VERIFICATIONID, verificationKeys);

            nested = nested.unionAll(create
                        .select(MAPPINGDATAENHANCED.FEATUREID)
                        .from(MAPPINGDATAENHANCED)
                        .join(TAXONTREE).on(TAXONTREE.CHILDPTVK.eq(MAPPINGDATAENHANCED.PTAXONVERSIONKEY))
                        .join(USERTAXONOBSERVATIONID).on(USERTAXONOBSERVATIONID.OBSERVATIONID.eq(MAPPINGDATAENHANCED.ID))
                        .where(enhancedCondition)
                    );
        }
        
        SelectJoinStep<Record1<Integer>> dNested = create
                .selectDistinct((Field<Integer>)nested.field(0))
                .from(nested);
	
        return MapHelper.getMapData(create
            .select(geometry, FEATURE.IDENTIFIER)
            .from(FEATURE)
            .join(dNested).on(FEATURE.ID.eq((Field<Integer>)dNested.field(0))));
           
    }
    
    /** The following method will create a list of layer names which will be used
     * in this map**/
    private static List<String> getOSRequiredLayers(Collection<String> layerNames, boolean presence, boolean absence, List<Band> bands) {
        List<String> toReturn = new ArrayList<String>();
        for(String layer : layerNames) {
            if(bands != null) {
                for(int i=0; i<bands.size(); i++) {
                    toReturn.add(layer + "_" + i);
                }
            }
            else {
                if(presence) {
                    toReturn.add(layer + "_Presence");
                }
                if(absence) {
                    toReturn.add(layer + "_Absence");
                }
            }
        }
        return toReturn;
    }
    
    private List<Integer> getVerificationKeys(List<Verification> verifications){
        List<Integer> toReturn = new ArrayList<Integer>();
        if(verifications != null && !verifications.isEmpty()){
            for(Verification verification : verifications){
                toReturn.add(verification.getStatus().getKey());
            }
        }
        return toReturn;
    }
}
