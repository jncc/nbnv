package uk.gov.nbn.data.gis.maps;

import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.mapObj;
import java.util.Iterator;
import java.util.List;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.MapFile;
import uk.gov.nbn.data.gis.providers.annotations.Param;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;

/**
 *
 * @author Christopher Johnson
 */
@MapService("SingleSpecies")
public class SingleSpeciesWMS {
    private static final String QUERY = "geom from (SELECT geom, observationID, label "
                    + "FROM vw_SingleSpeciesMap WHERE pTaxonVersionKey = '%s' "
                    + "AND userKey = '%s' "
                    + "AND resolutionID = %d "
                    + "%s"
                    + ") "
                    + "AS foo USING UNIQUE observationID USING SRID=4326";
    
    @MapObject("{taxonVersionKey}")
    public mapObj getTaxonMap(
            @MapFile("SingleSpeciesWMS.map") String mapFile,
            @QueryParam(key="userKey") String userKey,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") List<String> datasetKeys,
            @Param(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") String key ) {
        
        mapObj toReturn = new mapObj(mapFile);
        for(int i=0; i<toReturn.getNumlayers(); i++) {
            layerObj layer = toReturn.getLayer(i);
            layer.setData(String.format(QUERY, key, userKey, i+1, MapHelper.createInDatasetsSegment(datasetKeys)));
        }
        return toReturn;
    }
    
}
