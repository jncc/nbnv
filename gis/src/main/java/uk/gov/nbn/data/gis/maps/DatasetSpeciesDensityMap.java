package uk.gov.nbn.data.gis.maps;

import uk.gov.nbn.data.gis.maps.colour.Bucket;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.jooq.util.sqlserver.SQLServerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.maps.MapHelper.ResolutionDataGenerator;
import uk.gov.nbn.data.gis.maps.colour.ColourHelper.ColourRampGenerator;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.gov.nbn.data.gis.processor.GridMap.GridLayer;
import uk.gov.nbn.data.gis.processor.GridMap.Resolution;
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


/**
 * The following represents a Map service for DatasetSpeciesDensitys
 * 
 * It is configured to take the following filters :
 *  startyear
 *  endyear
 *  datasetKey (As part of the url call)
 * @author Christopher Johnson
 */
@Component
@MapContainer("DatasetSpeciesDensity")
public class DatasetSpeciesDensityMap {
    private static final String TEN_KM_LAYER_NAME = "Grid-10km";
    private static final String TWO_KM_LAYER_NAME = "Grid-2km";
    private static final String ONE_KM_LAYER_NAME = "Grid-1km";
    private static final String ONE_HUNDRED_M_LAYER_NAME = "Grid-100m";
    private static final String NONE_GRID_LAYER_NAME = "None-Grid";
    
    private static final ColourRampGenerator COLOUR_RAMP;
    
    private static final Map<String, List<Bucket>> BUCKETS;
    private static final Map<String, Integer> LAYERS;
    
    static {
        BUCKETS = new HashMap<String, List<Bucket>>();
        BUCKETS.put(TEN_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,6,11,16,21,36,51,76,101,251,501));
        BUCKETS.put(TWO_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,5,6,8,11,16,21,26,31,51,101));
        BUCKETS.put(ONE_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,5,6,8,11,16,21,26,31,51,101));
        BUCKETS.put(ONE_HUNDRED_M_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,5,6,7,8,9,10,11,16,21,51));
        BUCKETS.put(NONE_GRID_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,6,11,16,21,36,51,76,101,251,501));
        
        COLOUR_RAMP = new ColourRampGenerator(new Color(255, 255, 128), new Color(107, 0, 0));
        LAYERS = new HashMap<String, Integer>();
        LAYERS.put(TEN_KM_LAYER_NAME, 1);
        LAYERS.put(TWO_KM_LAYER_NAME, 2);
        LAYERS.put(ONE_KM_LAYER_NAME, 3);
        LAYERS.put(ONE_HUNDRED_M_LAYER_NAME, 4);
        LAYERS.put(NONE_GRID_LAYER_NAME, -1);
    }
    

    @Autowired Properties properties;
    
    @MapService("{datasetKey}")
    @GridMap(
        layers={
            @GridLayer(name="10km",     layer=TEN_KM_LAYER_NAME,        resolution=Resolution.TEN_KM),
            @GridLayer(name="2km",      layer=TWO_KM_LAYER_NAME,        resolution=Resolution.TWO_KM),
            @GridLayer(name="1km",      layer=ONE_KM_LAYER_NAME,        resolution=Resolution.ONE_KM),
            @GridLayer(name="100m",     layer=ONE_HUNDRED_M_LAYER_NAME, resolution=Resolution.ONE_HUNDRED_METERS)
        },
        defaultLayer="10km"
    )
    public MapFileModel getDatasetMapModel(
            final User user,
            @ServiceURL String mapServiceURL,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @PathParam(key="datasetKey", validation="^[A-Z0-9]{8}$") final String key) {
        
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
                            USERDATASETMAPPINGDATA.ABSENCE.eq(false)
                            .and(USERDATASETMAPPINGDATA.DATASETKEY.eq(key))
                            .and(USERDATASETMAPPINGDATA.USERID.eq(user.getId()));
                    condition = MapHelper.createTemporalSegment(condition, startYear, endYear, USERDATASETMAPPINGDATA.STARTDATE, USERDATASETMAPPINGDATA.ENDDATE);
                    
                    SelectHavingStep observations = create
                        .select(
                            USERDATASETMAPPINGDATA.FEATUREID,
                            countDistinct(USERDATASETMAPPINGDATA.PTAXONVERSIONKEY).as("species"))
                         .from(USERDATASETMAPPINGDATA)
                         .where(condition)
                         .groupBy(USERDATASETMAPPINGDATA.FEATUREID);
                    
                    return MapHelper.getMapData(FEATUREDATA.GEOM, FEATUREDATA.IDENTIFIER, 4326, create
                        .select(FEATUREDATA.GEOM, FEATUREDATA.IDENTIFIER, FEATUREDATA.LABEL, observations.getField("species"))
                        .from(observations)
                        .join(FEATUREDATA).on(FEATUREDATA.ID.eq(observations.getField(USERDATASETMAPPINGDATA.FEATUREID)))
                        .where(FEATUREDATA.RESOLUTIONID.eq(LAYERS.get(layerName)))
                    );
                }
        });
        return new MapFileModel("DatasetSpeciesDensity.map", data);
    }
}
