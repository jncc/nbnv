package uk.gov.nbn.data.gis.maps;

import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.mapObj;
import javax.servlet.http.HttpServletRequest;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.Param;

/**
 *
 * @author Christopher Johnson
 */
@MapService("DatasetSpeciesDensity")
public class DatasetSpeciesDensityWMS {
    private static final String QUERY = "geom from (SELECT geom, species, label "
                    + "FROM vw_DatasetSpeciesDensityMap "
                    + "WHERE datasetKey = '%s' "
                    + "AND userKey = '%d' "
                    + "AND resolutionID = %d ) "
                    + "AS foo USING UNIQUE label USING SRID=4326";
    
    @MapObject("{datasetKey}")
    public mapObj getTaxonMap(@Param("datasetKey") String key,  HttpServletRequest request) {
        mapObj toReturn = new mapObj(request.getRealPath("WEB-INF\\maps\\DatasetSpeciesDensityWMS.map"));
        for(int i=0; i<toReturn.getNumlayers(); i++) {
            layerObj layer = toReturn.getLayer(i);
            //todo validate key
            layer.setData(String.format(QUERY, key, 0, i+1));            
        }
        return toReturn;
    }
}
