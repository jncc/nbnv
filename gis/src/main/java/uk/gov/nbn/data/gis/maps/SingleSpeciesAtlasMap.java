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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ceh.dynamo.DynamoMap;
import uk.ac.ceh.dynamo.DynamoMap.Extent;
import uk.ac.ceh.dynamo.DynamoMap.GridLayer;
import uk.ac.ceh.dynamo.DynamoMap.Resolution;
import uk.ac.ceh.dynamo.arguments.annotations.ServiceURL;
import uk.gov.nbn.data.gis.config.TokenUserArgumentResolver;
import uk.gov.nbn.data.gis.validation.Datasets;
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
    
    private static final String[] LAYERS;
    
    @Autowired WebResource resource;
    @Autowired Properties properties;
    
    static {
        LAYERS = new String[]{TEN_KM_LAYER_NAME, TWO_KM_LAYER_NAME, ONE_KM_LAYER_NAME, ONE_HUNDRED_M_LAYER_NAME};
    }
    
    @RequestMapping("{taxonVersionKey}/atlas/{symbol}")
    @DynamoMap(
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
            DynamoMap gridMapDefinition,
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
        data.put("layerGenerator", SingleSpeciesMap.getSingleSpeciesResolutionDataGenerator(key, user, datasetKeys, startYear, endYear, false));
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
}
