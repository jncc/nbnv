package uk.gov.nbn.data.gis.maps;

import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.mapObj;
import javax.servlet.http.HttpServletRequest;
import uk.gov.nbn.data.gis.MapObject;
import uk.gov.nbn.data.gis.MapService;
import uk.gov.nbn.data.gis.Param;

/**
 *
 * @author Christopher Johnson
 */
@MapService("SingleSpecies")
public class SingleSpeciesWMS {
    
    @MapObject("{taxonVersionKey}")
    public mapObj getTaxonMap(@Param("taxonVersionKey") String key,  HttpServletRequest request) {
        mapObj toReturn = new mapObj(request.getRealPath("WEB-INF\\maps\\SingleSpeciesWMS.map"));
        for(int i=0; i<toReturn.getNumlayers(); i++) {
            layerObj layer = toReturn.getLayer(i);
            layer.setData("geom from (SELECT geom, observationId, gridRef "
                    + "FROM vw_SingleSpeciesMap WHERE resolutionID = " + (i+1)
                    + "AND taxonVersionKey = '"+ key +"') "
                    + "AS foo USING UNIQUE observationId USING SRID=4326");
            
        }
        return toReturn;
    }
    
    
}
