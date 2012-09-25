package uk.gov.nbn.data.gis.maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.org.nbn.nbnv.api.model.User;

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
@Component
@MapService("SingleSpecies")
public class SingleSpeciesWMS {
    private static final String QUERY = "geom from ("
            + "SELECT f.geom, o.observationID, f.label "
            + "FROM [dbo].[UserTaxonObservationData] o "
            + "INNER JOIN [dbo].[GridTree] gt ON gt.featureID = o.featureID "
            + "INNER JOIN [dbo].[FeatureData] f ON f.featureID = gt.parentFeatureID "
            + "WHERE pTaxonVersionKey = '%s' "
            + "AND userKey = %s "
            + "AND resolutionID = %d "
            + "%s " //place for dataset filter
            + "%s " //place for start year filter
            + "%s " //place for end year filter
        + ") AS foo USING UNIQUE observationID USING SRID=4326";
    
    @Autowired Properties properties;
    
    @MapObject(path="{taxonVersionKey}", map="SingleSpeciesWMS.map")
    public Map<String, Object> getSingleSpeciesModel(
            final User user,
            @PathParam(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") final String key,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") final List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear
            ) {
        
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("properties", properties);
        data.put("layerGenerator", new ResolutionDataGenerator() {
                @Override
                public String getData(int resolution) {
                    return String.format(QUERY, key, user.getId(), resolution, 
                        MapHelper.createInDatasetsSegment(datasetKeys),
                        MapHelper.createStartYearSegment(startYear),
                        MapHelper.createEndYearSegment(endYear));
                }
        });
        return data;
    }
}
