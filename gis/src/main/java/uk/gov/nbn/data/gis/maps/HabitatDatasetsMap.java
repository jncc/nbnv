package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;
import uk.org.nbn.nbnv.api.model.HabitatDataset;

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
    private final LayerGenerator layerGenerator = new LayerGenerator();
    
    public static class LayerGenerator {
        private static final String DATA = "geom from ("
            + "SELECT geom, hffd.id "
            + "FROM HabitatFeatureFeatureData hffd "
            + "INNER JOIN HabitatFeatureData hfd ON hfd.featureID = hffd.id "
            + "WHERE habitatDatasetKey = '%s'"
        + ") AS foo USING UNIQUE id USING SRID=4326";
    
        public String getData(String habitat) {
            return String.format(DATA, habitat);
        }
    }
    
    @MapService
    public MapFileModel getSiteBoundariesModel(@ServiceURL String mapServiceURL) {
        List<HabitatDataset> datasets = dataApi
                        .path("habitatDatasets")
                        .accept(MediaType.APPLICATION_JSON) 
                        .get(new GenericType<List<HabitatDataset>>() { });
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("layerGenerator", layerGenerator);
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("habitats", datasets);
        return new MapFileModel("HabitatDatasets.map",data);
    }
}
