package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.WebResource;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.DefaultValue;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;
import uk.org.nbn.nbnv.api.model.BoundingBox;
import uk.org.nbn.nbnv.api.model.Feature;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following map container will create two map services. The first being a 
 * map which contains a os background layer with a single feature highlighted
 * 
 * The second being the same as the first but with the intersected/overlapped 
 * features displayed
 * @author Christopher Johnson
 */
@Component
@MapContainer("SiteReport")
public class SiteReportMap {
    @Autowired WebResource resource;
    private static final String WITHIN_QUERY = "SELECT containedFeatureID "
            + "FROM FeatureContains "
            + "WHERE featureID = %s";
    
    private static final String OVERLAPS_QUERY = "SELECT overlappedFeatureID "
            + "FROM FeatureOverlaps "
            + "WHERE featureID = %s";
    
    private static final String QUERY = "geom from ("
            + "SELECT o.featureID , fd.geom, resolutionID, o.absence "
            + "FROM [dbo].[UserTaxonObservationData] o "
            + "INNER JOIN [dbo].[FeatureData] fd ON fd.id = o.featureID "
            + "WHERE pTaxonVersionKey = '%s' "
            + "AND o.featureID in (%s) "
            + "AND userID = %s "
            + "%s " //place for dataset filter
            + "%s " //place for start year filter
            + "%s " //place for end year filter
        + ") AS foo USING UNIQUE featureID USING SRID=4326";
    
    @Autowired Properties properties;
    
    @MapService("{featureID}")
    public MapFileModel getFeatureMap(
            @ServiceURL String mapServiceURL,
            @PathParam(key="featureID", validation="[0-9]*") String featureID) {        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("extent", getNativeBoundingBox(featureID));
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("featureID", featureID);
        return new MapFileModel("SiteReport.map",data);
    }
    
    @MapService("{featureID}/{taxonVersionKey}")
    public MapFileModel getSiteReport(
            User user,
            @ServiceURL String mapServiceURL,
            @PathParam(key="featureID", validation="[0-9]*") String featureID,
            @PathParam(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") String taxonKey,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") String endYear,
            @QueryParam(key="spatialRelationship", validation="(overlap)|(within)") @DefaultValue("overlap") String spatialRelation) {
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        
        String spatialRelationSelect = String.format(spatialRelation.equals("overlap") 
                ? OVERLAPS_QUERY : WITHIN_QUERY, featureID);
        
        data.put("extent", getNativeBoundingBox(featureID));
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("featureID", featureID);
        data.put("taxonVersionKey", taxonKey);
        data.put("recordsData", String.format(QUERY, taxonKey, spatialRelationSelect, user.getId(), 
                        MapHelper.createInDatasetsSegment(datasetKeys),
                        MapHelper.createStartYearSegment(startYear),
                        MapHelper.createEndYearSegment(endYear)));
        return new MapFileModel("SiteReport.map",data);
    }
    
    private BoundingBox getNativeBoundingBox(String featureId) {
        return resource
                    .path("features")
                    .path(featureId)
                    .get(Feature.class)
                    .getNativeBoundingBox();
    }
}
