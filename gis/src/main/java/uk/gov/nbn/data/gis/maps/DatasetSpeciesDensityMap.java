package uk.gov.nbn.data.gis.maps;

import uk.gov.nbn.data.gis.maps.colour.Bucket;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.maps.MapHelper.ResolutionDataGenerator;
import uk.gov.nbn.data.gis.maps.colour.ColourHelper.ColourRampGenerator;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;
import uk.org.nbn.nbnv.api.model.User;

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
        
    private static final ColourRampGenerator COLOUR_RAMP;
    
    private static final Map<String, List<Bucket>> BUCKETS;
    private static final String[] LAYERS;
    
    static {
        BUCKETS = new HashMap<String, List<Bucket>>();
        BUCKETS.put(TEN_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,6,11,16,21,36,51,76,101,251,501));
        BUCKETS.put(TWO_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,5,6,8,11,16,21,26,31,51,101));
        BUCKETS.put(ONE_KM_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,5,6,8,11,16,21,26,31,51,101));
        BUCKETS.put(ONE_HUNDRED_M_LAYER_NAME, Bucket.getOpenEndedBucketFromValues("SPECIES", 1,2,3,4,5,6,7,8,9,10,11,16,21,51));
        
        COLOUR_RAMP = new ColourRampGenerator(new Color(255, 255, 128), new Color(107, 0, 0));
        LAYERS = new String[]{TEN_KM_LAYER_NAME, TWO_KM_LAYER_NAME, ONE_KM_LAYER_NAME, ONE_HUNDRED_M_LAYER_NAME};
    }
    
    private static final String QUERY = "geom from ("
            + "SELECT geom, species, label "
            + "FROM ( "
                + "SELECT o.userID, o.datasetKey, gt.parentFeatureID as featureID, "
                    + "COUNT(DISTINCT o.pTaxonVersionKey) AS species "
                + "FROM [dbo].[UserTaxonObservationData] o "
                + "INNER JOIN [dbo].[GridTree] gt ON gt.featureID = o.featureID "
                + "WHERE absence = 0 "
                + "AND datasetKey = '%s' "
                + "AND userID = %s "
                + "%s " //start year segment
                + "%s " //end year segment
                + "GROUP BY gt.parentFeatureID, o.datasetKey, o.userID "
            + ") AS a "
            + "INNER JOIN [dbo].FeatureData AS f ON f.id = a.featureID "
            + "WHERE resolutionID = %d"
        + ") AS foo USING UNIQUE label USING SRID=4326";
    
    
    @Autowired Properties properties;
    
    @MapService("{datasetKey}")
    public MapFileModel getDatasetMapModel(
            final User user,
            @ServiceURL String mapServiceURL,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @PathParam(key="datasetKey", validation="^[A-Z0-9]{8}$") final String key) {
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("layers", LAYERS);
        data.put("colourRamp", COLOUR_RAMP);
        data.put("buckets", BUCKETS);
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("layerGenerator", new ResolutionDataGenerator() {
                @Override
                public String getData(int resolution) {
                    return String.format(QUERY, key, user.getId(), 
                        MapHelper.createStartYearSegment(startYear),
                        MapHelper.createEndYearSegment(endYear),
                        resolution);
                }
        });
        return new MapFileModel("DatasetSpeciesDensity.map", data);
    }
    
}
