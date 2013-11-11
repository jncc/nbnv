package uk.gov.nbn.data.gis.maps;

import uk.gov.nbn.data.gis.maps.colour.Bucket;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.jooq.util.sqlserver.SQLServerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.nbn.data.gis.maps.MapHelper.ResolutionDataGenerator;
import uk.gov.nbn.data.gis.maps.colour.ColourHelper.ColourRampGenerator;
import uk.org.nbn.nbnv.api.model.User;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.SelectHavingStep;
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import static org.jooq.impl.Factory.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ceh.dynamo.DynamoMap;
import uk.ac.ceh.dynamo.DynamoMap.GridLayer;
import uk.ac.ceh.dynamo.DynamoMap.Resolution;
import uk.ac.ceh.dynamo.arguments.annotations.ServiceURL;

/**
 * The following represents a Map service for DatasetSpeciesDensitys
 *
 * It is configured to take the following filters : startyear endyear datasetKey
 * (As part of the url call)
 *
 * @author Christopher Johnson
 */
@Controller
@RequestMapping("DatasetSpeciesDensity")
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
        BUCKETS.put(TEN_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1, 2, 3, 4, 6, 11, 16, 21, 36, 51, 76, 101, 251, 501));
        BUCKETS.put(TWO_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1, 2, 3, 4, 5, 6, 8, 11, 16, 21, 26, 31, 51, 101));
        BUCKETS.put(ONE_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1, 2, 3, 4, 5, 6, 8, 11, 16, 21, 26, 31, 51, 101));
        BUCKETS.put(ONE_HUNDRED_M_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 16, 21, 51));
        BUCKETS.put(NONE_GRID_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1, 2, 3, 4, 6, 11, 16, 21, 36, 51, 76, 101, 251, 501));

        COLOUR_RAMP = new ColourRampGenerator(new Color(255, 255, 128), new Color(107, 0, 0));
        LAYERS = new HashMap<String, Integer>();
        LAYERS.put(TEN_KM_LAYER_NAME, 1);
        LAYERS.put(TWO_KM_LAYER_NAME, 2);
        LAYERS.put(ONE_KM_LAYER_NAME, 3);
        LAYERS.put(ONE_HUNDRED_M_LAYER_NAME, 4);
        LAYERS.put(NONE_GRID_LAYER_NAME, -1);
    }
    @Autowired
    Properties properties;

    @RequestMapping("{datasetKey}")
    @DynamoMap(
        layers = {
        @GridLayer(name = "10km", layer = TEN_KM_LAYER_NAME, resolution = Resolution.TEN_KM),
        @GridLayer(name = "2km", layer = TWO_KM_LAYER_NAME, resolution = Resolution.TWO_KM),
        @GridLayer(name = "1km", layer = ONE_KM_LAYER_NAME, resolution = Resolution.ONE_KM),
        @GridLayer(name = "100m", layer = ONE_HUNDRED_M_LAYER_NAME, resolution = Resolution.ONE_HUNDRED_METERS)
    },
    defaultLayer = "10km")
    public ModelAndView getDatasetMapModel(
            final User user,
            @ServiceURL String mapServiceURL,
            @RequestParam(value="startyear", required=false)/*, validation = "[0-9]{4}")*/ final String startYear,
            @RequestParam(value="endyear", required=false)/*, validation = "[0-9]{4}")*/ final String endYear,
            @PathVariable("datasetKey")/*, validation = "^[A-Z0-9]{8}$")*/ final String key) {

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
                Condition publicCondition =
                        DATASETMAPPINGDATAPUBLIC.ABSENCE.eq(false)
                        .and(DATASETMAPPINGDATAPUBLIC.DATASETKEY.eq(key))
                        .and(DATASETMAPPINGDATAPUBLIC.RESOLUTIONID.eq(LAYERS.get(layerName)));
                publicCondition = MapHelper.createTemporalSegment(publicCondition, startYear, endYear, DATASETMAPPINGDATAPUBLIC.STARTDATE, DATASETMAPPINGDATAPUBLIC.ENDDATE);

                Condition enhancedCondition =
                        DATASETMAPPINGDATAENHANCED.ABSENCE.eq(false)
                        .and(DATASETMAPPINGDATAENHANCED.DATASETKEY.eq(key))
                        .and(DATASETMAPPINGDATAENHANCED.RESOLUTIONID.eq(LAYERS.get(layerName)))
                        .and(USERTAXONOBSERVATIONID.USERID.eq(user.getId()));
                enhancedCondition = MapHelper.createTemporalSegment(enhancedCondition, startYear, endYear, DATASETMAPPINGDATAENHANCED.STARTDATE, DATASETMAPPINGDATAENHANCED.ENDDATE);

                Select<Record> observations = create
                        .select(
                        DATASETMAPPINGDATAPUBLIC.FEATUREID,
                        DATASETMAPPINGDATAPUBLIC.PTAXONVERSIONKEY)
                        .from(DATASETMAPPINGDATAPUBLIC)
                        .where(publicCondition)
                        .unionAll(create
                        .select(
                        DATASETMAPPINGDATAENHANCED.FEATUREID,
                        DATASETMAPPINGDATAENHANCED.PTAXONVERSIONKEY)
                        .from(DATASETMAPPINGDATAENHANCED)
                        .join(USERTAXONOBSERVATIONID).on(USERTAXONOBSERVATIONID.OBSERVATIONID.eq(DATASETMAPPINGDATAENHANCED.ID))
                        .where(enhancedCondition));
                
                SelectHavingStep squares = create
                        .select((Field<Integer>)observations.getField(0).as("featureID"), countDistinct(observations.getField(1)).as("species"))
                        .from(observations)
                        .groupBy(observations.getField(0));

                return MapHelper.getMapData(FEATURE.GEOM, FEATURE.IDENTIFIER, 4326, create
                        .select(FEATURE.GEOM, FEATURE.IDENTIFIER, squares.getField("species"))
                        .from(squares)
                        .join(FEATURE).on(FEATURE.ID.eq((Field<Integer>)squares.getField(0))));
            }
        });
        return new ModelAndView("DatasetSpeciesDensity.map", data);
    }
}
