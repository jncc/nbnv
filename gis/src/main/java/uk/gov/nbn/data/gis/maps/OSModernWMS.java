package uk.gov.nbn.data.gis.maps;

import edu.umn.gis.mapscript.mapObj;
import javax.servlet.http.HttpServletRequest;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.Param;

/**
 *
 * @author Christopher Johnson
 */
@MapService("OS-Modern")
public class OSModernWMS {   
    @MapObject
    public mapObj getTaxonMap(@Param("taxonVersionKey") String key,  HttpServletRequest request) {       
        return new mapObj(request.getRealPath("WEB-INF\\maps\\OS-Modern.map"));
    }
}
