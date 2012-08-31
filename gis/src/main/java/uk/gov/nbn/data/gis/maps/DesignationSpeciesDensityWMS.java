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
 * The following represents a Map service for DesignationSpeciesDensity
 * 
 * It is configured to take the following filters :
 *  startyear
 *  endyear
 *  datasets
 *  designationKey (As part of the url call)
 * @author Christopher Johnson
 */
@MapService("DesignationSpeciesDensity")
public class DesignationSpeciesDensityWMS {
    private static final String QUERY = "geom from ("
            + "SELECT geom, species, label "
            + "FROM ("
                + "SELECT o.userKey, td.code, gt.parentFeatureID AS featureID, "
                    + "COUNT(DISTINCT o.pTaxonVersionKey) AS species "
                + "FROM [dbo].[UserTaxonObservationData] o " 
                + "INNER JOIN [dbo].[GridTree] gt ON gt.featureID = o.featureID "
                + "INNER JOIN [dbo].[DesignationTaxonData] td ON td.pTaxonVersionKey = o.pTaxonVersionKey "
                + "WHERE code = '%s' "
                + "AND userKey = '%s' "
                + "AND resolutionID = %d "
                + "%s " //placeholder for dataset filter
                + "%s " //startyear for dataset filter
                + "%s " //endyear for dataset filter
                + "GROUP BY gt.parentFeatureID, td.code, o.userKey"
            + ") a "
            + "INNER JOIN [dbo].[FeatureData] f ON f.featureID = a.featureID"
        + ") AS foo USING UNIQUE label USING SRID=4326";
    
 
    @MapObject("{designationKey}")
    public mapObj getTaxonMap(
            @MapFile("DesignationSpeciesDensityWMS.map") String mapFile,
            @QueryParam(key="userKey") String userKey,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") String endYear,
            @Param(key="designationKey", validation="^[A-Z0-9.()/_\\-]+$") String key) {
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
