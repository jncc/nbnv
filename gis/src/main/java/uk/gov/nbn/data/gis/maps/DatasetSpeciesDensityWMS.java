package uk.gov.nbn.data.gis.maps;

import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.mapObj;
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
@MapService("DatasetSpeciesDensity")
public class DatasetSpeciesDensityWMS {
    private static final String QUERY = "geom from (SELECT geom, species, label "
                    + "FROM vw_DatasetSpeciesDensityMap "
                    + "WHERE datasetKey = '%s' "
                    + "AND userKey = '%s' "
                    + "AND resolutionID = %d "
                    + "%s"
                    + ") "
                    + "AS foo USING UNIQUE label USING SRID=4326";
    
    @MapObject("{datasetKey}")
    public mapObj getTaxonMap(
            @MapFile("DatasetSpeciesDensityWMS.map") String mapFile,
            @QueryParam(key="userKey") String userKey,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") List<String> datasetKeys,
            @Param(key="datasetKey", validation="^[A-Z0-9]{8}$") String key) {
        mapObj toReturn = new mapObj(mapFile);
        for(int i=0; i<toReturn.getNumlayers(); i++) {
            layerObj layer = toReturn.getLayer(i);
            layer.setData(String.format(QUERY, key, userKey, i+1, MapHelper.createInDatasetsSegment(datasetKeys)));            
        }
        return toReturn;
    }
}
