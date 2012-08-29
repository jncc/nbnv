package uk.gov.nbn.data.gis.maps;

import edu.umn.gis.mapscript.mapObj;
import javax.servlet.http.HttpServletRequest;
import uk.gov.nbn.data.gis.MapObject;
import uk.gov.nbn.data.gis.MapService;
import uk.gov.nbn.data.gis.Param;

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
