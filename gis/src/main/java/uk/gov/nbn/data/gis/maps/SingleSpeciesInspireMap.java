/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.WebResource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import static uk.gov.nbn.data.dao.jooq.Tables.FEATURE;
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
/*    @GridMap(
        layers={
            @GridMap.GridLayer(name="10km",     layer="Species Distribution",        resolution=GridMap.Resolution.TEN_KM),
        },
        defaultLayer="10km",
        extents= {
            @GridMap.Extent(name="gbi",     epsgCode="EPSG:27700", extent={-250000, -50000, 750000, 1300000})
        }
    ) */
    public ModelAndView getSingleSpeciesInspireModel(
            final User user,
            GridMap gridMapDefinition,
            @ServiceURL String mapServiceURL,
            @PathVariable("taxonVersionKey") @Pattern(regexp="[A-Z][A-Z0-9]{15}") final String key,
            @RequestParam(value="REQUEST", required=false) String request
            ) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("title", "Fluffy Bunnyius" + " " + "CJ 2300" + " - Available Species Distribution for the UK");
        data.put("speciesCode", "FluffyBunny");
        data.put("speciesName", "Fluffy Bunnyius");
        data.put("resourceURL", "https://data.nbn.org.uk/api/taxa/" + key + "/inspire");
        data.put("date", sdf.format(new Date()));
	data.put("data", SingleSpeciesMap.getSingleSpeciesResolutionDataGenerator(FEATURE.GEOM, key, user, null, null, null, false, SingleSpeciesMap.DEFAULT_VERIFICATION_KEYS).getData("Grid-10km"));
        return new ModelAndView("SingleSpeciesInspire.map",data);
    }

}
