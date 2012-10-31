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
import uk.org.nbn.nbnv.api.model.SiteBoundaryDataset;

import static uk.gov.nbn.data.dao.jooq.Tables.*;
import org.jooq.util.sqlserver.SQLServerFactory;
/**
 * The following map service will make a call to the data api as defined in
 * the gis.properties to retrieve the most upto-date list of site boundary 
 * datasets. This will then be used to create the layers for the map file
 * @author Christopher Johnson
 */
@Component
@MapContainer("SiteBoundaryDatasets")
public class SiteBoundaryDatasetsMap {
    @Autowired Properties properties;
    @Autowired WebResource dataApi;
    private final LayerGenerator layerGenerator = new LayerGenerator();
    
    public static class LayerGenerator {
        public String getData(String siteBoundary) {
            SQLServerFactory create = new SQLServerFactory();
            return MapHelper.getMapData("geom", "id", create.
                    select(SITEBOUNDARYFEATUREDATA.GEOM, SITEBOUNDARYFEATUREDATA.ID)
                    .from(SITEBOUNDARYFEATUREDATA)
                    .join(SITEBOUNDARYDATA).on(SITEBOUNDARYDATA.FEATUREID.eq(SITEBOUNDARYFEATUREDATA.ID))
                    .where(SITEBOUNDARYDATA.SITEBOUNDARYDATASETKEY.eq(siteBoundary))
                , 4326);
        }
    }
    
    @MapService
    public MapFileModel getSiteBoundariesModel(@ServiceURL String mapServiceURL) {
        List<SiteBoundaryDataset> datasets = dataApi
                        .path("siteBoundaryDatasets")
                        .accept(MediaType.APPLICATION_JSON) 
                        .get(new GenericType<List<SiteBoundaryDataset>>() { });
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("layerGenerator", layerGenerator);
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("siteBoundaries", datasets);
        return new MapFileModel("SiteBoundaryDatasets.map",data);
    }
}
