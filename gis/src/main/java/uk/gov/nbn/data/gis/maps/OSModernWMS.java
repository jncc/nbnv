package uk.gov.nbn.data.gis.maps;

import edu.umn.gis.mapscript.mapObj;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.MapFile;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;

/**
 * The following represents a Map service for providing Ordinance Survey Maps
 * @author Christopher Johnson
 */
@MapService("OS-Modern")
public class OSModernWMS {   
    @MapObject
    public mapObj getTaxonMap(
            @PathParam(key="taxonVersionKey") String key, 
            @MapFile("OS-Modern.map") String mapFile) {       
        return new mapObj(mapFile);
    }
}
