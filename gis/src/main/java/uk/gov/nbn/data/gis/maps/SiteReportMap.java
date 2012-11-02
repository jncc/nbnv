package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.WebResource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.jooq.Condition;
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
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import org.jooq.util.sqlserver.SQLServerFactory;

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
    @Autowired Properties properties;
    
    @MapService("{featureID}")
    public MapFileModel getFeatureMap(
            @ServiceURL String mapServiceURL,
            @PathParam(key="featureID") String featureID) {        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("extent", getNativeBoundingBox(featureID));
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("featureData", MapHelper.getSelectedFeatureData(featureID));
        data.put("featureID", featureID);
        return new MapFileModel("SiteReport.map",data);
    }
    
    @MapService("{featureID}/{taxonVersionKey}")
    public MapFileModel getSiteReport(
            User user,
            @ServiceURL String mapServiceURL,
            @PathParam(key="featureID") String featureID,
            @PathParam(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") String taxonKey,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") String endYear,
            @QueryParam(key="spatialRelationship", validation="(overlap)|(within)") @DefaultValue("overlap") String spatialRelation) {
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        
        SQLServerFactory create = new SQLServerFactory();
        Condition condition = USERTAXONOBSERVATIONDATA.PTAXONVERSIONKEY.eq(taxonKey)
                .and(USERTAXONOBSERVATIONDATA.USERID.eq(user.getId()))
                .and(USERTAXONOBSERVATIONDATA.FEATUREID.in(
                    spatialRelation.equals("within") ? create
                        .select(FEATURECONTAINS.CONTAINEDFEATUREID)
                        .from(FEATURECONTAINS).join(FEATUREDATA).on(FEATUREDATA.ID.eq(FEATURECONTAINS.FEATUREID))
                        .where(FEATUREDATA.IDENTIFIER.eq(featureID))
                    : create
                        .select(FEATUREOVERLAPS.OVERLAPPEDFEATUREID)
                        .from(FEATUREOVERLAPS).join(FEATUREDATA).on(FEATUREDATA.ID.eq(FEATUREOVERLAPS.FEATUREID))
                        .where(FEATUREDATA.IDENTIFIER.eq(featureID))
                ));
                
        condition = MapHelper.createTemporalSegment(condition, startYear, endYear);
        condition = MapHelper.createInDatasetsSegment(condition, datasetKeys);
        
        data.put("extent", getNativeBoundingBox(featureID));
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("featureData", MapHelper.getSelectedFeatureData(featureID));
        data.put("featureID", featureID);
        data.put("taxonVersionKey", taxonKey);
        data.put("recordsData", MapHelper.getMapData(FEATUREDATA.GEOM, USERTAXONOBSERVATIONDATA.FEATUREID, 4326, create.
            select(USERTAXONOBSERVATIONDATA.FEATUREID, FEATUREDATA.GEOM, FEATUREDATA.RESOLUTIONID, USERTAXONOBSERVATIONDATA.ABSENCE)
            .from(USERTAXONOBSERVATIONDATA)
            .join(FEATUREDATA).on(FEATUREDATA.ID.eq(USERTAXONOBSERVATIONDATA.FEATUREID))
            .where(condition)
        ));
        return new MapFileModel("SiteReport.map",data);
    }
    
    
    private BoundingBox getNativeBoundingBox(String featureId) {
        return getBufferedBoundingBox(
                resource
                    .path("features")
                    .path(featureId)
                    .get(Feature.class)
                    .getNativeBoundingBox(), 0.05);
    }
    
    private static BoundingBox getBufferedBoundingBox(BoundingBox buffer, double bufferFactor) {
        double xDistance = buffer.getMaxX().subtract(buffer.getMinX()).abs().doubleValue();
        double yDistance = buffer.getMaxY().subtract(buffer.getMinY()).abs().doubleValue();
        BigDecimal xBuffer = new BigDecimal(xDistance*bufferFactor);
        BigDecimal yBuffer = new BigDecimal(yDistance*bufferFactor);
        return new BoundingBox(
                buffer.getEpsgCode(), 
                buffer.getMinX().subtract(xBuffer), 
                buffer.getMinY().subtract(yBuffer), 
                buffer.getMaxX().add(xBuffer), 
                buffer.getMaxY().add(yBuffer));
    }
}
