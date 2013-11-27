/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.WebResource;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ceh.dynamo.GridMap;
import uk.ac.ceh.dynamo.arguments.annotations.ServiceURL;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author paulbe
 */
@Controller
@Validated
@RequestMapping("SingleSpecies")
public class SingleSpeciesInspireMap {
    @Autowired WebResource resource;
    @Autowired Properties properties;
    
    @RequestMapping("{taxonVersionKey}/inspire")
    @GridMap(
        layers={
            @GridMap.GridLayer(name="10km",     layer="Species Distribution",        resolution=GridMap.Resolution.TEN_KM),
        },
        defaultLayer="10km",
        extents= {
            @GridMap.Extent(name="gbi",     epsgCode="EPSG:27700", extent={-250000, -50000, 750000, 1300000})
        }
    )
    public ModelAndView getSingleSpeciesInspireModel(
            final User user,
            GridMap gridMapDefinition,
            @ServiceURL String mapServiceURL,
            @PathVariable("taxonVersionKey") @Pattern(regexp="[A-Z][A-Z0-9]{15}") final String key,
            @RequestParam(value="REQUEST", required=false) String request
            ) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        
        
        data.put("layers", gridMapDefinition.layers());
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("layerGenerator", SingleSpeciesMap.getSingleSpeciesResolutionDataGenerator(key, user, null, null, null, false));
        return new ModelAndView("SingleSpecies.map",data);
    }

}
