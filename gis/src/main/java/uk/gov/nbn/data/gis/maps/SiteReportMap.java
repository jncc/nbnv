package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.WebResource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import uk.org.nbn.nbnv.api.model.BoundingBox;
import uk.org.nbn.nbnv.api.model.Feature;
import uk.org.nbn.nbnv.api.model.User;
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import org.jooq.util.sqlserver.SQLServerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ceh.dynamo.arguments.annotations.ServiceURL;

/**
 * The following map container will create two map services. The first being a 
 * map which contains a os background layer with a single feature highlighted
 * 
 * The second being the same as the first but with the intersected/overlapped 
 * features displayed
 * @author Christopher Johnson
 */
@Controller
@RequestMapping("SiteReport")
public class SiteReportMap {
    @Autowired WebResource resource;    
    @Autowired Properties properties;
    
    @RequestMapping("{featureID}")
    public ModelAndView getFeatureMap(
            @ServiceURL String mapServiceURL,
            @PathVariable("featureID") String featureID) {        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("extent", getNativeBoundingBox(featureID));
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("featureData", MapHelper.getSelectedFeatureData(featureID));
        data.put("featureID", featureID);
        return new ModelAndView("SiteReport.map",data);
    }
    
    @RequestMapping("{featureID}/{taxonVersionKey}")
    public ModelAndView getSiteReport(
            User user,
            @ServiceURL String mapServiceURL,
            @PathVariable("featureID") String featureID,
            @PathVariable("taxonVersionKey")/*, validation="[A-Z][A-Z0-9]{15}")*/ String taxonKey,
            @RequestParam(value="datasets", required=false)/*validation="^[A-Z0-9]{8}$")*/ List<String> datasetKeys,
            @RequestParam(value="startyear", required=false)/*validation="[0-9]{4}")*/ String startYear,
            @RequestParam(value="endyear", required=false)/*validation="[0-9]{4}") */String endYear,
            @RequestParam(value="spatialRelationship", required=false, defaultValue="overlap")/*validation="(overlap)|(within)") */ String spatialRelation) {
        
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
                
        condition = MapHelper.createTemporalSegment(condition, startYear, endYear, USERTAXONOBSERVATIONDATA.STARTDATE, USERTAXONOBSERVATIONDATA.ENDDATE);
        condition = MapHelper.createInDatasetsSegment(condition, USERTAXONOBSERVATIONDATA.DATASETKEY, datasetKeys);
        
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
        return new ModelAndView("SiteReport.map",data);
    }
    
    
    private BoundingBox getNativeBoundingBox(String featureId) {
        return getBufferedBoundingBox(
                resource
                    .path("features")
                    .path(featureId)
                    .get(Feature.class)
                    .getNativeBoundingBox(), 0.05);
    }
    
    /**
     * The following method will buffer a given bounding box in all directions by
     * a factor of bufferFactor.
     * @param buffer The boundingbox to buffer
     * @param bufferFactor The factor to buffer in all directions. 0.05 will will 
     *  buffer 5% for a given dimension in all directions
     * @return A buffered bounding box
     */
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
