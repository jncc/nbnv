package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.WebResource;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
    
    private static final Map<String,Color> COLOURS;
    private static final String[] LAYERS;
    
    @Autowired WebResource resource;
    @Autowired Properties properties;
    
    static {
        COLOURS = new HashMap<String, Color>();
        COLOURS.put(TEN_KM_LAYER_NAME, new Color(255, 255, 0));
        COLOURS.put(TWO_KM_LAYER_NAME, new Color(0, 255, 0));
        COLOURS.put(ONE_KM_LAYER_NAME, new Color(0, 255, 255));
        COLOURS.put(ONE_HUNDRED_M_LAYER_NAME, new Color(255, 0, 0));
        LAYERS = new String[]{TEN_KM_LAYER_NAME, TWO_KM_LAYER_NAME, ONE_KM_LAYER_NAME, ONE_HUNDRED_M_LAYER_NAME};
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
            @PathParam(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") final String key,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") final List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @QueryParam(key="abundance", validation="(all)|(presence)|(absence)") @DefaultValue("presence") String abundance,
            @QueryParam(key="feature") String featureID,
            @QueryParam(key="band", commaSeperated=false) List<Band> bands
            ) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("layers", LAYERS);
        data.put("colours", COLOURS);
        data.put("enableAbsence", abundance.equals("all") || abundance.equals("absence"));
        data.put("enablePresence", abundance.equals("all") || abundance.equals("presence"));
        data.put("bands", bands);
        data.put("mapServiceURL", mapServiceURL);
        data.put("featureData", MapHelper.getSelectedFeatureData(featureID));
        data.put("properties", properties);
        data.put("layerGenerator", getSingleSpeciesResolutionDataGenerator(key, user, datasetKeys, startYear, endYear));
        return new MapFileModel("SingleSpecies.map",data);
    }
    
    //Factored out the single species resolution data generator so that it can be used by the atlas map
    static ResolutionDataGenerator getSingleSpeciesResolutionDataGenerator(
            final String taxonKey, 
            final User user, 
            final List<String> datasetKeys, 
            final String startYear, 
            final String endYear) {
        return new ResolutionDataGenerator() {
                @Override
                public String getData(int resolution) {
                    SQLServerFactory create = new SQLServerFactory();
                    Condition condition = 
                            USERTAXONOBSERVATIONDATA.PTAXONVERSIONKEY.eq(taxonKey)
                            .and(USERTAXONOBSERVATIONDATA.USERID.eq(user.getId())
                            .and(FEATUREDATA.RESOLUTIONID.eq(resolution)));
                    condition = MapHelper.createTemporalSegment(condition, startYear, endYear);
                    condition = MapHelper.createInDatasetsSegment(condition, datasetKeys);
                    
                    return MapHelper.getMapData(FEATUREDATA.GEOM, FEATUREDATA.ID, 4326 ,create
                        .select(
                            FEATUREDATA.GEOM,
                            FEATUREDATA.ID,
                            FEATUREDATA.LABEL,
                            USERTAXONOBSERVATIONDATA.STARTDATE,
                            USERTAXONOBSERVATIONDATA.ENDDATE,
                            USERTAXONOBSERVATIONDATA.ABSENCE)
                        .from(USERTAXONOBSERVATIONDATA)
                        .join(GRIDTREE).on(GRIDTREE.FEATUREID.eq(USERTAXONOBSERVATIONDATA.FEATUREID))
                        .join(FEATUREDATA).on(FEATUREDATA.ID.eq(GRIDTREE.PARENTFEATUREID))
                        .where(condition)
                    );
                }
        };
    }
}
