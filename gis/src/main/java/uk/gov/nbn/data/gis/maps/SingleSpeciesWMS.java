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
 * The following represents a Map service for SingleSpecies
 * 
 * It is configured to take the following filters :
 *  startyear
 *  endyear
 *  datasets
 *  taxonVersionKey (As part of the url call)
 * @author Christopher Johnson
 */
@MapService("SingleSpecies")
public class SingleSpeciesWMS {
    private static final String QUERY = "geom from ("
            + "SELECT f.geom, o.observationID, f.label "
            + "FROM [dbo].[UserTaxonObservationData] o "
            + "INNER JOIN [dbo].[GridTree] gt ON gt.featureID = o.featureID "
            + "INNER JOIN [dbo].[FeatureData] f ON f.featureID = gt.parentFeatureID "
            + "WHERE pTaxonVersionKey = '%s' "
            + "AND userKey = '%s' "
            + "AND resolutionID = %d "
            + "%s " //place for dataset filter
            + "%s " //place for start year filter
            + "%s " //place for end year filter
        + ") AS foo USING UNIQUE observationID USING SRID=4326";
    
    @MapObject("{taxonVersionKey}")
    public mapObj getTaxonMap(
            @MapFile("SingleSpeciesWMS.map") String mapFile,
            @Param(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") String key,
            @QueryParam(key="userKey") String userKey,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") String endYear
            ) {
        
        mapObj toReturn = new mapObj(mapFile);
        for(int i=0; i<toReturn.getNumlayers(); i++) {
            layerObj layer = toReturn.getLayer(i);
            layer.setData(String.format(QUERY, key, userKey, i+1, 
                MapHelper.createInDatasetsSegment(datasetKeys),
                MapHelper.createStartYearSegment(startYear),
                MapHelper.createEndYearSegment(endYear)));
        }
        return toReturn;
    }
    
}
