package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.core.MediaType;
import org.jooq.util.sqlserver.SQLServerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;
import uk.org.nbn.nbnv.api.model.HabitatDataset;
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import uk.gov.nbn.data.gis.maps.cache.ShapefileStore;
import uk.gov.nbn.data.gis.maps.colour.ColourHelper;
import uk.gov.nbn.data.gis.providers.annotations.DefaultValue;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;

/**
 * The following map service will make a call to the data api as defined in
 * the gis.properties to retrieve the most upto-date list of habitat 
 * datasets. This will then be used to create the layers for the map file
 * @author Christopher Johnson
 */
@Component
@MapContainer("HabitatDatasets")
public class HabitatDatasetsMap {
    @Autowired Properties properties;
    @Autowired WebResource dataApi;
    @Autowired ShapefileStore shapes;
    @Autowired ColourHelper colours;
    private final LayerGenerator layerGenerator = new LayerGenerator();
    
    public static class LayerGenerator {
        public String getData(String habitat) {
            SQLServerFactory create = new SQLServerFactory();
            return MapHelper.getMapData(HABITATFEATUREFEATUREDATA.GEOM, HABITATFEATUREFEATUREDATA.IDENTIFIER, 4326, create.
                select(HABITATFEATUREFEATUREDATA.GEOM, HABITATFEATUREFEATUREDATA.IDENTIFIER)
                .from(HABITATFEATUREFEATUREDATA)
                .join(HABITATFEATUREDATA).on(HABITATFEATUREDATA.FEATUREID.eq(HABITATFEATUREFEATUREDATA.ID))
                .where(HABITATFEATUREDATA.HABITATDATASETKEY.eq(habitat))
            );
        }
    }
    
    @MapService
    public MapFileModel getSiteBoundariesModel(@ServiceURL String mapServiceURL, 
            @QueryParam(key="SRS") @DefaultValue("EPSG:4326") String srs) {
        List<HabitatDataset> datasets = dataApi
                        .path("habitatDatasets")
                        .accept(MediaType.APPLICATION_JSON) 
                        .get(new GenericType<List<HabitatDataset>>() { });
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("layerGenerator", layerGenerator);
        data.put("srs", srs);
        data.put("shapes", shapes);
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("habitats", datasets);
        data.put("colours", colours);
        return new MapFileModel("HabitatDatasets.map",data);
    }
}