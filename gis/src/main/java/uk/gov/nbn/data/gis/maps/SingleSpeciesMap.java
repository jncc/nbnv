package uk.gov.nbn.data.gis.maps;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.maps.MapHelper.ResolutionDataGenerator;
import uk.gov.nbn.data.gis.maps.colour.Band;
import uk.gov.nbn.data.gis.maps.colour.Bucket;
import uk.gov.nbn.data.gis.maps.colour.ColourHelper;
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
    private static final int SYMBOLOGY_OUTLINE_WIDTH_DENOMINATOR = 10;
    private static final String TEN_KM_LAYER_NAME = "Grid-10km";
    private static final String TWO_KM_LAYER_NAME = "Grid-2km";
    private static final String ONE_KM_LAYER_NAME = "Grid-1km";
    private static final String ONE_HUNDRED_M_LAYER_NAME = "Grid-100m";
    
    private static final Map<String,Color> COLOURS;
    private static final String[] LAYERS;
    
    static {
        COLOURS = new HashMap<String, Color>();
        COLOURS.put(TEN_KM_LAYER_NAME, new Color(255, 255, 0));
        COLOURS.put(TWO_KM_LAYER_NAME, new Color(0, 255, 0));
        COLOURS.put(ONE_KM_LAYER_NAME, new Color(0, 255, 255));
        COLOURS.put(ONE_HUNDRED_M_LAYER_NAME, new Color(255, 0, 0));
        LAYERS = new String[]{TEN_KM_LAYER_NAME, TWO_KM_LAYER_NAME, ONE_KM_LAYER_NAME, ONE_HUNDRED_M_LAYER_NAME};
    }
    
    private static final String QUERY = "geom from ("
            + "SELECT f.geom, o.featureID, f.label, o.startDate, o.endDate, o.absence "
            + "FROM [dbo].[UserTaxonObservationData] o "
            + "INNER JOIN [dbo].[GridTree] gt ON gt.featureID = o.featureID "
            + "INNER JOIN [dbo].[FeatureData] f ON f.id = gt.parentFeatureID "
            + "WHERE pTaxonVersionKey = '%s' "
            + "AND userID = %s "
            + "AND resolutionID = %d "
            + "%s " //place for dataset filter
            + "%s " //place for start year filter
            + "%s " //place for end year filter
        + ") AS foo USING UNIQUE featureID USING SRID=4326";
    @Autowired Properties properties;
    
    @MapService("{taxonVersionKey}")
    @GridMap(
        layers={
            @GridLayer(name="10km",     layer=TEN_KM_LAYER_NAME,        resolutions=Resolution.TEN_KM),
            @GridLayer(name="2km",      layer=TWO_KM_LAYER_NAME,        resolutions=Resolution.TWO_KM),
            @GridLayer(name="1km",      layer=ONE_KM_LAYER_NAME,        resolutions=Resolution.ONE_KM),
            @GridLayer(name="100m",     layer=ONE_HUNDRED_M_LAYER_NAME, resolutions=Resolution.ONE_HUNDRED_METERS)
        },
        defaultLayer="10km",
        backgrounds=@Layer(name="os", layer="OS-Scale-Dependent" ),
        overlays=@Layer(name="feature", layer="Selected-Feature" )
    )
    public MapFileModel getSingleSpeciesModel(
            final User user,
            @ServiceURL String mapServiceURL,
            @PathParam(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") final String key,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") final List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @QueryParam(key="abundance", validation="(all)|(presence)|(absence)") @DefaultValue("presence") String abundance,
            @QueryParam(key="feature", validation="[0-9]*") String featureID,
            @QueryParam(key="band", commaSeperated=false) List<Band> bands
            ) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("layers", LAYERS);
        data.put("colours", COLOURS);
        data.put("enableAbsence", abundance.equals("all") || abundance.equals("absence"));
        data.put("enablePresence", abundance.equals("all") || abundance.equals("presence"));
        data.put("bands", bands);
        data.put("mapServiceURL", mapServiceURL);
        data.put("featureID", featureID);
        data.put("properties", properties);
        data.put("layerGenerator", new ResolutionDataGenerator() {
                @Override
                public String getData(int resolution) {
                    return String.format(QUERY, key, user.getId(), resolution, 
                        MapHelper.createInDatasetsSegment(datasetKeys),
                        MapHelper.createStartYearSegment(startYear),
                        MapHelper.createEndYearSegment(endYear));
                }
        });
        return new MapFileModel("SingleSpecies.map",data);
    }
    
    @MapService("symbology/{taxonVersionKey}")
    @GridMap(
        layers={
            @GridLayer(name="10km",     layer=TEN_KM_LAYER_NAME,        resolutions=Resolution.TEN_KM),
            @GridLayer(name="2km",      layer=TWO_KM_LAYER_NAME,        resolutions=Resolution.TWO_KM),
            @GridLayer(name="1km",      layer=ONE_KM_LAYER_NAME,        resolutions=Resolution.ONE_KM),
            @GridLayer(name="100m",     layer=ONE_HUNDRED_M_LAYER_NAME, resolutions=Resolution.ONE_HUNDRED_METERS)
        },
        defaultLayer="10km"
    )
    public MapFileModel getSingleSpeciesSymbologyModel(
            final User user,
            GridMap gridMapDefinition,
            @ServiceURL String mapServiceURL,
            @PathParam(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") final String key,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") final List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @QueryParam(key="fillcolour", validation="[0-9a-fA-F]{6}") @DefaultValue("000000") String fillColour,
            @QueryParam(key="outlinecolour", validation="[0-9a-fA-F]{6}") @DefaultValue("000000") String outlineColour
            ) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("layers", gridMapDefinition.layers());
        data.put("fillColour", new Color(Integer.parseInt(fillColour, 16)));
        data.put("outlineColour", new Color(Integer.parseInt(outlineColour, 16)));
        data.put("outlineWidthDenominator", SYMBOLOGY_OUTLINE_WIDTH_DENOMINATOR);
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("layerGenerator", new ResolutionDataGenerator() {
                @Override
                public String getData(int resolution) {
                    return String.format(QUERY, key, user.getId(), resolution, 
                        MapHelper.createInDatasetsSegment(datasetKeys),
                        MapHelper.createStartYearSegment(startYear),
                        MapHelper.createEndYearSegment(endYear));
                }
        });
        return new MapFileModel("SingleSpeciesSymbology.map",data);
    }
}
