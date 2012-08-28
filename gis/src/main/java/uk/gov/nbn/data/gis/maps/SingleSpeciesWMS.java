package uk.gov.nbn.data.gis.maps;

import edu.umn.gis.mapscript.mapObj;
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
    public mapObj getTaxonMap(@Param("taxonVersionKey") String key) {
        System.out.println(key);
        return null;
    }
}
