package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.core.MediaType;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import uk.org.nbn.nbnv.api.model.SiteBoundaryDataset;
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ceh.dynamo.arguments.annotations.ServiceURL;
import uk.gov.nbn.data.gis.maps.colour.ColourHelper;
/**
 * The following map service will make a call to the data api as defined in
 * the gis.properties to retrieve the most upto-date list of site boundary 
 * datasets. This will then be used to create the layers for the map file
 * @author Christopher Johnson
 */
@Controller
public class SiteBoundaryDatasetsMap {
    @Autowired Properties properties;
    @Autowired WebResource dataApi;
    @Autowired ColourHelper colours;
    private final LayerGenerator layerGenerator = new LayerGenerator();
    
    public static class LayerGenerator {
        public String getData(String siteBoundary) {
            DSLContext create = MapHelper.getContext();
            return MapHelper.getMapData(SITEBOUNDARYFEATUREDATA.GEOM, SITEBOUNDARYFEATUREDATA.IDENTIFIER, 4326, create.
                select(SITEBOUNDARYFEATUREDATA.GEOM, SITEBOUNDARYFEATUREDATA.IDENTIFIER)
                .from(SITEBOUNDARYFEATUREDATA)
                .join(SITEBOUNDARYDATA).on(SITEBOUNDARYDATA.FEATUREID.eq(SITEBOUNDARYFEATUREDATA.ID))
                .where(SITEBOUNDARYDATA.SITEBOUNDARYDATASETKEY.eq(siteBoundary))
            );
        }
    }
    
    
    @RequestMapping("SiteBoundaryDatasets")
    public ModelAndView getSiteBoundariesModel(
            @ServiceURL String mapServiceURL) {
        List<SiteBoundaryDataset> datasets = dataApi
                        .path("siteBoundaryDatasets")
                        .accept(MediaType.APPLICATION_JSON) 
                        .get(new GenericType<List<SiteBoundaryDataset>>() { });
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("layerGenerator", layerGenerator);
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("siteBoundaries", datasets);
        data.put("colours", colours);

        return new ModelAndView("SiteBoundaryDatasets.map",data);
    }
}
