package uk.gov.nbn.data.gis.maps;

import uk.gov.nbn.data.gis.maps.colour.Bucket;
import uk.gov.nbn.data.gis.maps.colour.ColourHelper;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.jooq.util.sqlserver.SQLServerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.maps.MapHelper.ResolutionDataGenerator;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;
import uk.org.nbn.nbnv.api.model.User;
import org.jooq.Condition;
import org.jooq.SelectHavingStep;
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import static org.jooq.impl.Factory.*;
import uk.gov.nbn.data.gis.processor.GridMap;
/**
 * The following represents a Map service for DesignationSpeciesDensity
 * 
 * It is configured to take the following filters :
 *  startyear
 *  endyear
 *  datasets
 *  designationKey (As part of the url call)
 * @author Christopher Johnson
 */
@Component
@MapContainer("DesignationSpeciesDensity")
public class DesignationSpeciesDensityMap {
    private static final String TEN_KM_LAYER_NAME = "Grid-10km";
    private static final String TWO_KM_LAYER_NAME = "Grid-2km";
    private static final String ONE_KM_LAYER_NAME = "Grid-1km";
    private static final String ONE_HUNDRED_M_LAYER_NAME = "Grid-100m";
    private static final String NONE_GRID_LAYER_NAME = "None-Grid";
    
    private static final ColourHelper.ColourRampGenerator COLOUR_RAMP;
    
    private static final Map<String, List<Bucket>> BUCKETS;
    private static final Map<String, Integer> LAYERS;
    
    static {
        BUCKETS = new HashMap<String, List<Bucket>>();
        BUCKETS.put(TEN_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,6,11,16,21,36,51,76,101,251,501));
        BUCKETS.put(TWO_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,5,6,8,11,16,21,26,31,51,101));
        BUCKETS.put(ONE_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,5,6,8,11,16,21,26,31,51,101));
        BUCKETS.put(ONE_HUNDRED_M_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,5,6,7,8,9,10,11,16,21,51));
        BUCKETS.put(NONE_GRID_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,6,11,16,21,36,51,76,101,251,501));
        
        COLOUR_RAMP = new ColourHelper.ColourRampGenerator(new Color(255, 255, 128), new Color(107, 0, 0));
        LAYERS = new HashMap<String, Integer>();
        LAYERS.put(TEN_KM_LAYER_NAME, 1);
        LAYERS.put(TWO_KM_LAYER_NAME, 2);
        LAYERS.put(ONE_KM_LAYER_NAME, 3);
        LAYERS.put(ONE_HUNDRED_M_LAYER_NAME, 4);
        LAYERS.put(NONE_GRID_LAYER_NAME, -1);
    }
    
    @Autowired Properties properties;
     
    @MapService("{designationKey}")
    @GridMap(
        layers={
            @GridMap.GridLayer(name="10km",     layer=TEN_KM_LAYER_NAME,        resolution=GridMap.Resolution.TEN_KM),
            @GridMap.GridLayer(name="2km",      layer=TWO_KM_LAYER_NAME,        resolution=GridMap.Resolution.TWO_KM),
            @GridMap.GridLayer(name="1km",      layer=ONE_KM_LAYER_NAME,        resolution=GridMap.Resolution.ONE_KM),
            @GridMap.GridLayer(name="100m",     layer=ONE_HUNDRED_M_LAYER_NAME, resolution=GridMap.Resolution.ONE_HUNDRED_METERS)
        },
        defaultLayer="10km"
    )
    public MapFileModel getDesignationMapModel(
            final User user,
            @ServiceURL String mapServiceURL,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") final List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @PathParam(key="designationKey", validation="^[A-Z0-9.()/_\\-]+$") final String key) {
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("layers", LAYERS.keySet());
        data.put("colourRamp", COLOUR_RAMP);
        data.put("buckets", BUCKETS);
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("layerGenerator", new ResolutionDataGenerator() {
                @Override
                public String getData(String layerName) {
                    SQLServerFactory create = new SQLServerFactory();
                    
                    Condition condition = 
                            USERMAPPINGDATA.ABSENCE.eq(false)
                            .and(DESIGNATIONTAXONDATA.CODE.eq(key))
                            .and(USERMAPPINGDATA.USERID.eq(user.getId()).or(USERMAPPINGDATA.USERID.eq(1)));
                    condition = MapHelper.createTemporalSegment(condition, startYear, endYear, USERMAPPINGDATA.STARTDATE, USERMAPPINGDATA.ENDDATE);
                    condition = MapHelper.createInDatasetsSegment(condition, USERMAPPINGDATA.DATASETKEY, datasetKeys);
                    
                    SelectHavingStep observations = create
                        .select(
                            USERMAPPINGDATA.FEATUREID,
                            countDistinct(USERMAPPINGDATA.PTAXONVERSIONKEY).as("species"))
                        .from(USERMAPPINGDATA)
                        .join(DESIGNATIONTAXONDATA).on(DESIGNATIONTAXONDATA.PTAXONVERSIONKEY.eq(USERMAPPINGDATA.PTAXONVERSIONKEY))
                        .where(condition)
                        .groupBy(USERMAPPINGDATA.FEATUREID);
                    
                    return MapHelper.getMapData(FEATUREDATA.GEOM, FEATUREDATA.IDENTIFIER, 4326, create
                        .select(
                            FEATUREDATA.GEOM, 
                            FEATUREDATA.IDENTIFIER,
                            observations.getField("species")
                        )
                        .from(observations)
                        .join(FEATUREDATA).on(observations.getField(USERMAPPINGDATA.FEATUREID).eq(FEATUREDATA.ID))
                        .where(FEATUREDATA.RESOLUTIONID.eq(LAYERS.get(layerName)))
                    );
                }
        });
        return new MapFileModel("DesignationSpeciesDensity.map", data);
    }
}
