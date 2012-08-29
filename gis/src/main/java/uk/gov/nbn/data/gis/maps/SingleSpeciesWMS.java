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
    private static final String QUERY = "geom from (SELECT geom, observationID, label "
                    + "FROM vw_SingleSpeciesMap WHERE pTaxonVersionKey = '%s' "
                    + "AND userKey = '%d' "
                    + "AND resolutionID = %d ) "
                    + "AS foo USING UNIQUE observationID USING SRID=4326";
    
    @MapObject("{taxonVersionKey}")
    public mapObj getTaxonMap(@Param("taxonVersionKey") String key,  HttpServletRequest request) {
        mapObj toReturn = new mapObj(request.getRealPath("WEB-INF\\maps\\SingleSpeciesWMS.map"));
        for(int i=0; i<toReturn.getNumlayers(); i++) {
            layerObj layer = toReturn.getLayer(i);
            layer.setData(String.format(QUERY, key, 0, i+1));
        }
        return toReturn;
    }
}
