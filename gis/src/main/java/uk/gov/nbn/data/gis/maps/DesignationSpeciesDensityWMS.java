package uk.gov.nbn.data.gis.maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.org.nbn.nbnv.api.model.User;

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
@Component
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
                + "AND userKey = %s "
                + "%s " //placeholder for dataset filter
                + "%s " //startyear for dataset filter
                + "%s " //endyear for dataset filter
                + "GROUP BY gt.parentFeatureID, td.code, o.userKey"
            + ") a "
            + "INNER JOIN [dbo].[FeatureData] f ON f.featureID = a.featureID "
            + "WHERE resolutionID = %d "
        + ") AS foo USING UNIQUE label USING SRID=4326";
    
    @Autowired Properties properties;
     
    @MapObject("{designationKey}")
    public MapFileModel getDesignationMapModel(
            final User user,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") final List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @PathParam(key="designationKey", validation="^[A-Z0-9.()/_\\-]+$") final String key) {
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("properties", properties);
        data.put("layerGenerator", new ResolutionDataGenerator() {
                @Override
                public String getData(int resolution) {
                    return String.format(QUERY, key, user.getId(), 
                        MapHelper.createInDatasetsSegment(datasetKeys),
                        MapHelper.createStartYearSegment(startYear),
                        MapHelper.createEndYearSegment(endYear),
                        resolution);
                }
        });
        return new MapFileModel("DesignationSpeciesDensityWMS.map", data);
    }
}
