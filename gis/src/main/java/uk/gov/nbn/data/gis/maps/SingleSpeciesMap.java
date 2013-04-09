package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.WebResource;
import java.awt.Color;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.maps.MapHelper.ResolutionDataGenerator;
import uk.gov.nbn.data.gis.maps.colour.Band;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.gov.nbn.data.gis.processor.GridMap.Layer;
import uk.gov.nbn.data.gis.processor.GridMap.GridLayer;
import uk.gov.nbn.data.gis.processor.GridMap.Resolution;
import uk.gov.nbn.data.gis.providers.annotations.DefaultValue;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;
import uk.org.nbn.nbnv.api.model.User;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;
import static uk.gov.nbn.data.dao.jooq.Tables.*;

import org.jooq.util.sqlserver.SQLServerFactory;

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
@Component
@MapContainer("SingleSpecies")
public class SingleSpeciesMap {
    private static final String TEN_KM_LAYER_NAME = "Grid-10km";
    private static final String TWO_KM_LAYER_NAME = "Grid-2km";
    private static final String ONE_KM_LAYER_NAME = "Grid-1km";
    private static final String ONE_HUNDRED_M_LAYER_NAME = "Grid-100m";
    private static final String NONE_GRID_LAYER_NAME = "None-Grid";
    
    private static final Map<String,Color> COLOURS;
    private static final Map<String, Integer> LAYERS;
    
    @Autowired WebResource resource;
    @Autowired Properties properties;
    
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
    }
    
    @MapService("{taxonVersionKey}")
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
    public MapFileModel getSingleSpeciesModel(
            final User user,
            @ServiceURL String mapServiceURL,
            @PathParam(key="taxonVersionKey", validation="[A-Z][A-Z0-9]{15}") final String key,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") final List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @QueryParam(key="abundance", validation="(all)|(presence)|(absence)") @DefaultValue("presence") String abundance,
            @QueryParam(key="feature") String featureID,
            @QueryParam(key="band", commaSeperated=false) List<Band> bands
            ) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        boolean absence = abundance.equals("all") || abundance.equals("absence");
        boolean presence = abundance.equals("all") || abundance.equals("presence");
        data.put("layers", LAYERS.keySet());
        data.put("colours", COLOURS);
        data.put("osRequiredLayers", getOSRequiredLayers(LAYERS.keySet(), presence, absence, bands));
        data.put("enableAbsence", absence);
        data.put("enablePresence", presence);
        data.put("bands", bands);
        data.put("mapServiceURL", mapServiceURL);
        data.put("featureData", MapHelper.getSelectedFeatureData(featureID));
        data.put("properties", properties);
        data.put("absenceLayerGenerator", getSingleSpeciesResolutionDataGenerator(key, user, datasetKeys, startYear, endYear, true));
        data.put("presencelayerGenerator", getSingleSpeciesResolutionDataGenerator(key, user, datasetKeys, startYear, endYear, false));
        data.put("bandLayerGenerator", new SingleSpeciesBandSqlGenerator() {
            @Override public String getData(String layerName, Band dateBand) {
                return getSQL(key, user, datasetKeys, dateBand.getStartYear(), dateBand.getEndYear(), false, layerName);
            }
        });
        return new MapFileModel("SingleSpecies.map",data);
    }
    
    public interface SingleSpeciesBandSqlGenerator {
        public String getData(String layerName, Band dateBand);
    }
    
    //Factored out the single species resolution data generator so that it can be used by the atlas map
    static ResolutionDataGenerator getSingleSpeciesResolutionDataGenerator(
            final String taxonKey, 
            final User user, 
            final List<String> datasetKeys, 
            final String startYear, 
            final String endYear,
            final boolean absence) {
        return new ResolutionDataGenerator() {
            @Override public String getData(String layerName) {
                return getSQL(taxonKey, user, datasetKeys, startYear, endYear, absence, layerName);
            }
        };
    }
            
    
    private static String getSQL(   String taxonKey, User user, 
                                    List<String> datasetKeys, 
                                    String startYear, String endYear, 
                                    boolean absence, String layerName) {
        SQLServerFactory create = new SQLServerFactory();
        Condition publicCondition = TAXONTREE.NODEPTVK.eq(taxonKey)
                .and(MAPPINGDATAPUBLIC.ABSENCE.eq(absence))
                .and(MAPPINGDATAPUBLIC.RESOLUTIONID.eq(LAYERS.get(layerName)));
        publicCondition = MapHelper.createTemporalSegment(publicCondition, startYear, endYear, MAPPINGDATAPUBLIC.STARTDATE, MAPPINGDATAPUBLIC.ENDDATE);
        publicCondition = MapHelper.createInDatasetsSegment(publicCondition, MAPPINGDATAPUBLIC.DATASETKEY, datasetKeys);

        Condition enhancedCondition = TAXONTREE.NODEPTVK.eq(taxonKey)
                .and(USERTAXONOBSERVATIONID.USERID.eq(user.getId()))
                .and(MAPPINGDATAENHANCED.ABSENCE.eq(absence))
                .and(MAPPINGDATAENHANCED.RESOLUTIONID.eq(LAYERS.get(layerName)));
        enhancedCondition = MapHelper.createTemporalSegment(enhancedCondition, startYear, endYear, MAPPINGDATAENHANCED.STARTDATE, MAPPINGDATAENHANCED.ENDDATE);
        enhancedCondition = MapHelper.createInDatasetsSegment(enhancedCondition, MAPPINGDATAENHANCED.DATASETKEY, datasetKeys);

        Select<Record> nested = create
                    .select(MAPPINGDATAPUBLIC.FEATUREID.as("FEATUREID"))
                    .from(MAPPINGDATAPUBLIC)
                    .join(TAXONTREE).on(TAXONTREE.CHILDPTVK.eq(MAPPINGDATAPUBLIC.PTAXONVERSIONKEY))
                    .where(publicCondition)
                    .unionAll(create
                        .select(MAPPINGDATAENHANCED.FEATUREID)
                        .from(MAPPINGDATAENHANCED)
                        .join(TAXONTREE).on(TAXONTREE.CHILDPTVK.eq(MAPPINGDATAENHANCED.PTAXONVERSIONKEY))
                        .join(USERTAXONOBSERVATIONID).on(USERTAXONOBSERVATIONID.OBSERVATIONID.eq(MAPPINGDATAENHANCED.ID))
                        .where(enhancedCondition)
                    );

        return MapHelper.getMapData(FEATUREDATA.GEOM, FEATUREDATA.IDENTIFIER, 4326 ,create
            .select(FEATUREDATA.GEOM, FEATUREDATA.IDENTIFIER, FEATUREDATA.LABEL)
            .from(FEATUREDATA)
            .join(nested).on(FEATUREDATA.ID.eq((Field<Integer>)nested.getField(0))));
           
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
}
