package uk.gov.nbn.data.gis.maps;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.validation.constraints.Pattern;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ceh.dynamo.BoundingBox;
import uk.org.nbn.nbnv.api.model.User;
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ceh.dynamo.FeatureResolver;
import uk.ac.ceh.dynamo.arguments.annotations.ServiceURL;
import uk.gov.nbn.data.gis.validation.Datasets;

/**
 * The following map container will create two map services. The first being a 
 * map which contains a os background layer with a single feature highlighted
 * 
 * The second being the same as the first but with the intersected/overlapped 
 * features displayed
 * @author Christopher Johnson
 */
@Controller
@Validated
@RequestMapping("SiteReport")
public class SiteReportMap {
    @Autowired FeatureResolver resolver;
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
            @PathVariable("taxonVersionKey") @Pattern(regexp="[A-Z][A-Z0-9]{15}") String taxonKey,
            @RequestParam(value="datasets", required=false) @Datasets List<String> datasetKeys,
            @RequestParam(value="startyear", required=false) @Pattern(regexp="[0-9]{4}") String startYear,
            @RequestParam(value="endyear", required=false) @Pattern(regexp="[0-9]{4}") String endYear,
            @RequestParam(value="spatialRelationship", required=false, defaultValue="overlap") @Pattern(regexp="(overlap)|(within)") String spatialRelation) {
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        
        DSLContext create = MapHelper.getContext();
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
        data.put("recordsData", MapHelper.getMapData(create.
            select(USERTAXONOBSERVATIONDATA.FEATUREID, FEATUREDATA.GEOM, FEATUREDATA.RESOLUTIONID, USERTAXONOBSERVATIONDATA.ABSENCE)
            .from(USERTAXONOBSERVATIONDATA)
            .join(FEATUREDATA).on(FEATUREDATA.ID.eq(USERTAXONOBSERVATIONDATA.FEATUREID))
            .where(condition)
        ));
        return new ModelAndView("SiteReport.map",data);
    }
    
    private BoundingBox getNativeBoundingBox(String featureId) {
        return resolver.getFeature(featureId).getBufferedBoundingBox(0.05);
    }
}
