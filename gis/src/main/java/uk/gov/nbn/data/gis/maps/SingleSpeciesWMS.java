package uk.gov.nbn.data.gis.maps;

import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.mapObj;
import javax.servlet.http.HttpServletRequest;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.MapFile;
import uk.gov.nbn.data.gis.providers.annotations.Param;

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
    public mapObj getTaxonMap(
            @MapFile("SingleSpeciesWMS.map") String mapFile,
            @Param(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") String key ) {
        
        mapObj toReturn = new mapObj(mapFile);
        for(int i=0; i<toReturn.getNumlayers(); i++) {
            layerObj layer = toReturn.getLayer(i);
            layer.setData(String.format(QUERY, key, 0, i+1));
        }
        return toReturn;
    }
}
