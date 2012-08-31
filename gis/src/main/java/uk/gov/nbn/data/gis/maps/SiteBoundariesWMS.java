package uk.gov.nbn.data.gis.maps;

import edu.umn.gis.mapscript.mapObj;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.MapFile;
import uk.gov.nbn.data.gis.providers.annotations.Param;

/**
 *
 * @author Christopher Johnson
 */
@MapService("SiteBoundaries")
public class SiteBoundariesWMS {

    private static final String DATA = "geom from ("
            + "SELECT geom, sbfd.featureID "
            + "FROM SiteBoundaryFeatureData sbfd "
            + "INNER JOIN SiteBoundaryData sbd ON sbd.featureID = sbfd.featureID "
            + "WHERE siteBoundaryDatasetKey = '%s'"
        + ") AS foo USING UNIQUE featureID USING SRID=4326";
    
    @MapObject("{siteBoundaryKey}")
    public mapObj getTaxonMap(@MapFile("SiteBoundariesWMS.map") String mapFile,
        @Param(key="siteBoundaryKey") String siteBoundaryKey) {       
        
        mapObj toReturn = new mapObj(mapFile);
        toReturn.getLayer(0).setData(String.format(DATA, siteBoundaryKey));
        
        return toReturn;
    }
}
