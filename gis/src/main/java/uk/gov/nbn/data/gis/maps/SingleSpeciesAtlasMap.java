package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ceh.dynamo.GridMap;
import uk.ac.ceh.dynamo.GridMap.Extent;
import uk.ac.ceh.dynamo.GridMap.GridLayer;
import uk.ac.ceh.dynamo.GridMap.Resolution;
import uk.ac.ceh.dynamo.arguments.annotations.ServiceURL;
import uk.gov.nbn.data.gis.config.TokenUserArgumentResolver;
import uk.gov.nbn.data.gis.validation.Datasets;
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import uk.org.nbn.nbnv.api.model.ProviderWithQueryStats;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following represents a Atlas Symbology Map service for SingleSpecies
 * 
 * It is configured to take the following filters :
 *  startyear
 *  endyear
 *  datasets
 *  outlinecolour
 *  fillcolour
 *  taxonVersionKey (As part of the url call)
 *  symbol (As part of the url call)
 * @author Christopher Johnson
 */
@Controller
@Validated
@RequestMapping("SingleSpecies")
public class SingleSpeciesAtlasMap {
    private static final String TEN_KM_LAYER_NAME = "Grid-10km";
    private static final String TWO_KM_LAYER_NAME = "Grid-2km";
    private static final String ONE_KM_LAYER_NAME = "Grid-1km";
    private static final String ONE_HUNDRED_M_LAYER_NAME = "Grid-100m";
    private static final int SYMBOLOGY_OUTLINE_WIDTH_DENOMINATOR = 10;
    
    private static final Field<?> GEOM_CENTROID = DSL.field("geom.STCentroid()").as("geom");
    
    private static final String[] LAYERS;
    private static final Map<String, Integer> LAYERSINFO;

    static {
        LAYERSINFO = new HashMap<String, Integer>();
        LAYERSINFO.put(TEN_KM_LAYER_NAME, 1);
        LAYERSINFO.put(TWO_KM_LAYER_NAME, 2);
        LAYERSINFO.put(ONE_KM_LAYER_NAME, 3);
        LAYERSINFO.put(ONE_HUNDRED_M_LAYER_NAME, 4);
    }
    
    @Autowired WebResource resource;
    @Autowired Properties properties;
    
    static {
        LAYERS = new String[]{TEN_KM_LAYER_NAME, TWO_KM_LAYER_NAME, ONE_KM_LAYER_NAME, ONE_HUNDRED_M_LAYER_NAME};
    }
    
    @RequestMapping("{taxonVersionKey}/atlas/{symbol}")
    @GridMap(
        layers={
            @GridLayer(name="10km",     layer=TEN_KM_LAYER_NAME,        resolution=Resolution.TEN_KM),
            @GridLayer(name="2km",      layer=TWO_KM_LAYER_NAME,        resolution=Resolution.TWO_KM),
            @GridLayer(name="1km",      layer=ONE_KM_LAYER_NAME,        resolution=Resolution.ONE_KM),
            @GridLayer(name="100m",     layer=ONE_HUNDRED_M_LAYER_NAME, resolution=Resolution.ONE_HUNDRED_METERS)
        },
        defaultLayer="10km",
        extents= {
            @Extent(name="gbi",     epsgCode="EPSG:27700", extent={-250000, -50000, 750000, 1300000})
        }
    )
    public ModelAndView getSingleSpeciesSymbologyModel(
            final User user,
            GridMap gridMapDefinition,
            @ServiceURL String mapServiceURL,
            @PathVariable("taxonVersionKey") @Pattern(regexp="[A-Z][A-Z0-9]{15}") final String key,
            @PathVariable("symbol") @Pattern(regexp="(circle)") String symbol,
            @RequestParam(value="datasets", required=false) @Datasets final List<String> datasetKeys,
            @RequestParam(value="startyear", required=false) @Pattern(regexp="[0-9]{4}") final String startYear,
            @RequestParam(value="endyear", required=false) @Pattern(regexp="[0-9]{4}") final String endYear,
            @RequestParam(value="fillcolour", required=false, defaultValue="000000") @Pattern(regexp="[0-9a-fA-F]{6}") String fillColour,
            @RequestParam(value="outlinecolour", required=false, defaultValue="000000") @Pattern(regexp="[0-9a-fA-F]{6}") String outlineColour,
            @RequestParam(value="REQUEST", required=false) String request
            ) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        
        /* Below is a HACK to get around when mapserver renders legends with
         * none pixel size units. These will extends outside of the legend 
         * @see https://github.com/mapserver/mapserver/issues/1147
         */
        data.put("legendSymbolHack", "GetLegendGraphic".equals(request));
        
        data.put("symbol", symbol);
        data.put("layers", gridMapDefinition.layers());
        data.put("osRequiredLayers", LAYERS);
        data.put("fillColour", new Color(Integer.parseInt(fillColour, 16)));
        data.put("outlineColour", new Color(Integer.parseInt(outlineColour, 16)));
        data.put("outlineWidthDenominator", SYMBOLOGY_OUTLINE_WIDTH_DENOMINATOR);
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("layerGenerator", getSingleSpeciesAtlasResolutionDataGenerator(GEOM_CENTROID, key, user, datasetKeys, startYear, endYear, false));
        return new ModelAndView("SingleSpeciesSymbology.map",data);
    }
    
    @RequestMapping("{taxonVersionKey}/atlas/{symbol}/acknowledgement")
    public ModelAndView getDatasetProviders(
            @ServiceURL String url,
            HttpServletRequest request,
            @PathVariable("taxonVersionKey") @Pattern(regexp="[A-Z][A-Z0-9]{15}") final String key,
            @RequestParam(value="datasets", required=false) @Datasets final List<String> datasetKeys,
            @RequestParam(value="startyear", required=false) @Pattern(regexp="[0-9]{4}") final String startYear,
            @RequestParam(value="endyear", required=false) @Pattern(regexp="[0-9]{4}") final String endYear,
            @RequestParam(value="css", required=false) String css) {
        
        WebResource ackResource = resource
                .path("taxonObservations")
                .path("providers")
                .queryParam("ptvk", key);
        
        if(startYear != null) {
            ackResource = ackResource.queryParam("startYear", startYear);
        }
        
        if(endYear != null) {
            ackResource = ackResource.queryParam("endYear", startYear);
        }
        
        // Iterate around the dataset keys and add to the query
        if(datasetKeys != null) {
            for(String currDatasetKey : datasetKeys) {
                ackResource = ackResource.queryParam("datasetKey", currDatasetKey);
            }
        }
        
        GenericType<List<ProviderWithQueryStats>> type = new GenericType<List<ProviderWithQueryStats>>() {};
        
        
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("externalCss", css);
        data.put("url", url);
        data.put("properties", properties);
        data.put("providers", TokenUserArgumentResolver.getTokenUserWebResourceBuilder(ackResource, request).get(type));
        
        return new ModelAndView("acknowledgement", data);
    }
    
    static MapHelper.ResolutionDataGenerator getSingleSpeciesAtlasResolutionDataGenerator(
            final Field<?> geometry,
            final String taxonKey, 
            final User user, 
            final List<String> datasetKeys, 
            final String startYear, 
            final String endYear,
            final boolean absence) {
        return new MapHelper.ResolutionDataGenerator() {
            @Override public String getData(String layerName) {
                return getSQL(geometry, taxonKey, user, datasetKeys, startYear, endYear, absence, layerName);
            }
        };
    }      
    
    private static String getSQL(   Field<?> geometry,
                                    String taxonKey, User user, 
                                    List<String> datasetKeys, 
                                    String startYear, String endYear, 
                                    boolean absence, String layerName) {
        DSLContext create = MapHelper.getContext();
        Condition publicCondition = TAXONTREE.NODEPTVK.eq(taxonKey)
                .and(MAPPINGDATAPUBLIC.ABSENCE.eq(absence))
                .and(MAPPINGDATAPUBLIC.RESOLUTIONID.eq(LAYERSINFO.get(layerName)));
        publicCondition = MapHelper.createTemporalSegment(publicCondition, startYear, endYear, MAPPINGDATAPUBLIC.STARTDATE, MAPPINGDATAPUBLIC.ENDDATE);
        publicCondition = MapHelper.createInDatasetsSegment(publicCondition, MAPPINGDATAPUBLIC.DATASETKEY, datasetKeys);

        Condition enhancedCondition = TAXONTREE.NODEPTVK.eq(taxonKey)
                .and(USERTAXONOBSERVATIONID.USERID.eq(user.getId()))
                .and(MAPPINGDATAENHANCED.ABSENCE.eq(absence))
                .and(MAPPINGDATAENHANCED.RESOLUTIONID.eq(LAYERSINFO.get(layerName)));
        enhancedCondition = MapHelper.createTemporalSegment(enhancedCondition, startYear, endYear, MAPPINGDATAENHANCED.STARTDATE, MAPPINGDATAENHANCED.ENDDATE);
        enhancedCondition = MapHelper.createInDatasetsSegment(enhancedCondition, MAPPINGDATAENHANCED.DATASETKEY, datasetKeys);

        Select<Record1<Integer>> nested = create
                    .select(MAPPINGDATAPUBLIC.FEATUREID.as("FEATUREID"))
                    .from(MAPPINGDATAPUBLIC)
                    .join(TAXONTREE).on(TAXONTREE.CHILDPTVK.eq(MAPPINGDATAPUBLIC.PTAXONVERSIONKEY))
                    .where(publicCondition);
        
        
                if (User.PUBLIC_USER != user) {
                    nested.unionAll(create
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
        
        return MapHelper.getMapData(geometry, FEATURE.IDENTIFIER, 4326 ,create
            .select(geometry, FEATURE.IDENTIFIER)
            .from(FEATURE)
            .join(dNested).on(FEATURE.ID.eq((Field<Integer>)dNested.field(0))));
           
    }

}
