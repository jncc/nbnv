package uk.gov.nbn.data.gis.maps;

import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.mapObj;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.MapFile;
import uk.gov.nbn.data.gis.providers.annotations.Param;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;

/**
 * The following represents a Map service for DatasetSpeciesDensitys
 * 
 * It is configured to take the following filters :
 *  startyear
 *  endyear
 *  datasetKey (As part of the url call)
 * @author Christopher Johnson
 */
@MapService("DatasetSpeciesDensity")
public class DatasetSpeciesDensityWMS {
    private static final String QUERY = "geom from ("
            + "SELECT geom, species, label "
            + "FROM ( "
                + "SELECT o.userKey, o.datasetKey, gt.parentFeatureID as featureID, "
                    + "COUNT(DISTINCT o.pTaxonVersionKey) AS species "
                + "FROM [dbo].[UserTaxonObservationData] o "
                + "INNER JOIN [dbo].[GridTree] gt ON gt.featureID = o.featureID "
                + "WHERE datasetKey = '%s' "
                + "AND userKey = '%s' "
                + "%s " //start year segment
                + "%s " //end year segment
                + "GROUP BY gt.parentFeatureID, o.datasetKey, o.userKey "
            + ") AS a "
            + "INNER JOIN [dbo].FeatureData AS f ON f.featureID = a.featureID "
            + "WHERE resolutionID = %d"
        + ") AS foo USING UNIQUE label USING SRID=4326";
    
    @MapObject("{datasetKey}")
    public mapObj getTaxonMap(
            @MapFile("DatasetSpeciesDensityWMS.map") String mapFile,
            @QueryParam(key="userKey") String userKey,
            @QueryParam(key="startyear", validation="[0-9]{4}") String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") String endYear,
            @Param(key="datasetKey", validation="^[A-Z0-9]{8}$") String key) {
        
        mapObj toReturn = new mapObj(mapFile);
        for(int i=0; i<toReturn.getNumlayers(); i++) {
            layerObj layer = toReturn.getLayer(i);
            layer.setData(String.format(QUERY, key, userKey, 
                MapHelper.createStartYearSegment(startYear),
                MapHelper.createEndYearSegment(endYear),
                i+1)); //resolution id
        }
        return toReturn;
    }
}
