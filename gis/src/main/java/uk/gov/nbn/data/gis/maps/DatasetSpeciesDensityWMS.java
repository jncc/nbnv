package uk.gov.nbn.data.gis.maps;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following represents a Map service for DatasetSpeciesDensitys
 * 
 * It is configured to take the following filters :
 *  startyear
 *  endyear
 *  datasetKey (As part of the url call)
 * @author Christopher Johnson
 */
@Component
@MapContainer("DatasetSpeciesDensity")
public class DatasetSpeciesDensityWMS {
    private static final String QUERY = "geom from ("
            + "SELECT geom, species, label "
            + "FROM ( "
                + "SELECT o.userKey, o.datasetKey, gt.parentFeatureID as featureID, "
                    + "COUNT(DISTINCT o.pTaxonVersionKey) AS species "
                + "FROM [dbo].[UserTaxonObservationData] o "
                + "INNER JOIN [dbo].[GridTree] gt ON gt.featureID = o.featureID "
                + "WHERE datasetKey = '%s' "
                + "AND userKey = %s "
                + "%s " //start year segment
                + "%s " //end year segment
                + "GROUP BY gt.parentFeatureID, o.datasetKey, o.userKey "
            + ") AS a "
            + "INNER JOIN [dbo].FeatureData AS f ON f.featureID = a.featureID "
            + "WHERE resolutionID = %d"
        + ") AS foo USING UNIQUE label USING SRID=4326";
    
    
    @Autowired Properties properties;
    
    @MapService("{datasetKey}")
    public MapFileModel getDatasetMapModel(
            final User user,
            @ServiceURL String mapServiceURL,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @PathParam(key="datasetKey", validation="^[A-Z0-9]{8}$") final String key) {
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("layerGenerator", new ResolutionDataGenerator() {
                @Override
                public String getData(int resolution) {
                    return String.format(QUERY, key, user.getId(), 
                        MapHelper.createStartYearSegment(startYear),
                        MapHelper.createEndYearSegment(endYear),
                        resolution);
                }
        });
        return new MapFileModel("DatasetSpeciesDensityWMS.map", data);
    }
}
