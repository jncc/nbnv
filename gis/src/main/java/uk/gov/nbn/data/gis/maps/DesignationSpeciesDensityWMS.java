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
@MapService("DesignationSpeciesDensity")
public class DesignationSpeciesDensityWMS {
    private static final String QUERY = "geom from (SELECT geom, species, gridRef "
            + "FROM vw_DesignationSpeciesDensityMap "
            + "WHERE code = '%s' "
            + "AND userKey = '%d' "
            + "AND resolutionID = %d) "
            + "AS foo USING UNIQUE gridRef USING SRID=4326";

    @MapObject("{designationKey}")
    public mapObj getTaxonMap(@Param("designationKey") String key,  HttpServletRequest request) {
        mapObj toReturn = new mapObj(request.getRealPath("WEB-INF\\maps\\DesignationSpeciesDensityWMS.map"));
        for(int i=0; i<toReturn.getNumlayers(); i++) {
            layerObj layer = toReturn.getLayer(i);
            layer.setData(String.format(QUERY, key, 0, i+1));
            
        }
        return toReturn;
    }
}
