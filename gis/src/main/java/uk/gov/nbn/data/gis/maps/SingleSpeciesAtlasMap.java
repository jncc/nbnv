package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Acknowledgement;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.gov.nbn.data.gis.processor.GridMap.GridLayer;
import uk.gov.nbn.data.gis.processor.GridMap.Resolution;
import uk.gov.nbn.data.gis.providers.TokenUserProvider;
import uk.gov.nbn.data.gis.providers.annotations.DefaultValue;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;
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
@Component
@MapContainer("SingleSpecies")
public class SingleSpeciesAtlasMap {
    private static final int SYMBOLOGY_OUTLINE_WIDTH_DENOMINATOR = 10;
    
    @Autowired WebResource resource;
    @Autowired Properties properties;
    
    @MapService("{taxonVersionKey}/atlas/{symbol}")
    @GridMap(
        layers={
            @GridLayer(name="10km",     layer="Grid-10km",  resolution=Resolution.TEN_KM),
            @GridLayer(name="2km",      layer="Grid-2km",   resolution=Resolution.TWO_KM),
            @GridLayer(name="1km",      layer="Grid-1km",   resolution=Resolution.ONE_KM),
            @GridLayer(name="100m",     layer="Grid-100m",  resolution=Resolution.ONE_HUNDRED_METERS)
        },
        defaultLayer="10km"
    )    
    @Acknowledgement(method="getDatasetProviders")
    public MapFileModel getSingleSpeciesSymbologyModel(
            final User user,
            GridMap gridMapDefinition,
            @ServiceURL String mapServiceURL,
            @PathParam(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") final String key,
            @PathParam(key="symbol", validation="(circle)") String symbol,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") final List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @QueryParam(key="fillcolour", validation="[0-9a-fA-F]{6}") @DefaultValue("000000") String fillColour,
            @QueryParam(key="outlinecolour", validation="[0-9a-fA-F]{6}") @DefaultValue("000000") String outlineColour,
            @QueryParam(key="REQUEST") String request
            ) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        
        /* Below is a HACK to get around when mapserver renders legends with
         * none pixel size units. These will extends outside of the legend 
         * @see https://github.com/mapserver/mapserver/issues/1147
         */
        data.put("legendSymbolHack", "GetLegendGraphic".equals(request));
        
        data.put("symbol", symbol);
        data.put("layers", gridMapDefinition.layers());
        data.put("fillColour", new Color(Integer.parseInt(fillColour, 16)));
        data.put("outlineColour", new Color(Integer.parseInt(outlineColour, 16)));
        data.put("outlineWidthDenominator", SYMBOLOGY_OUTLINE_WIDTH_DENOMINATOR);
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("layerGenerator", SingleSpeciesMap.getSingleSpeciesResolutionDataGenerator(key, user, datasetKeys, startYear, endYear));
        return new MapFileModel("SingleSpeciesSymbology.map",data);
    }
    
    
    public List<ProviderWithQueryStats> getDatasetProviders(
            HttpServletRequest request,
            @PathParam(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") final String key,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") final List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear) {
        
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
        return TokenUserProvider.getTokenUserWebResourceBuilder(ackResource, request).get(type);
    }
}
