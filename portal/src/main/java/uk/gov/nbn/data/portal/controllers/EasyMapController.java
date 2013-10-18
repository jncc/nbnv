/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.api.model.BoundingBox;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Feature;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationAccessRequest;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithQueryStats;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserAccessRequest;

/**
 *
 * @author paulbe
 */
@Controller
public class EasyMapController {
    @Autowired WebResource resource;
    private List<String> errors;
    
    public EasyMapController() {
        errors = new ArrayList<String>();
    }
    
    @RequestMapping(value = "/EasyMap", method = RequestMethod.GET)
    public ModelAndView getCreatePage(
            @QueryParam("tvk") String tvk
            ,@QueryParam("ds") String datasets
            ,@QueryParam("res") String gridResolution
            ,@QueryParam("w") Integer mapWidth
            ,@QueryParam("h") Integer mapHeight
            ,@QueryParam("gd") String gridOverlay
            ,@QueryParam("bg") String mapBackground
            ,@QueryParam("vc") Integer viceCountyId
            ,@QueryParam("b0from") Integer band0StartDate
            ,@QueryParam("b0to") Integer band0EndDate
            ,@QueryParam("b0fill") String band0Fill
            ,@QueryParam("b0bord") String band0Border
            ,@QueryParam("b1from") Integer band1StartDate
            ,@QueryParam("b1to") Integer band1EndDate
            ,@QueryParam("b1fill") String band1Fill
            ,@QueryParam("b1bord") String band1Border
            ,@QueryParam("b2from") Integer band2StartDate
            ,@QueryParam("b2to") Integer band2EndDate
            ,@QueryParam("b2fill") String band2Fill
            ,@QueryParam("b2bord") String band2Border
            ,@QueryParam("zoom") String zoomLocation) {
        
        Map<String, Object> model = new HashMap<String, Object>();
        
        //check if tvk is valid and return error page.
        if (tvk == null || tvk.isEmpty()) {
            errors.add("You must supply a tvk");
        } else {
            model.put("tvk", tvk);
        }
        
        //create wms query string
        String wmsParameters = "abundance=presence&FORMAT=image%2Fpng&TRANSPARENT=TRUE&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&SRS=EPSG%3A27700";
        
        wmsParameters = wmsParameters + getMapSizeParam(mapWidth, mapHeight);
        
        boolean showBand1 = showBand(band1StartDate, band1EndDate);
        boolean showBand2 = showBand(band2StartDate, band2EndDate);
        boolean showFeatureLayer = (viceCountyId != null &&  viceCountyId > 0);
        
        wmsParameters = wmsParameters + getMapLayersParam(showFeatureLayer, showBand1, showBand2, mapBackground, gridResolution);
       
        Feature vcFeature = null;
        if (showFeatureLayer) vcFeature = getViceCountyFeature(viceCountyId);
        
        wmsParameters = wmsParameters + getBoundingBoxParam(vcFeature, zoomLocation);
        
        String defaultBorderColour = "000000";
        String band0DefaultFill = "FF0000";
        String band1DefaultFill = "FF9900";
        String band2DefaultFill = "FFFF00";
        
        wmsParameters = wmsParameters + getBandingParameters(
                (band0StartDate != null && band0StartDate > 0) ? band0StartDate : 1,
                (band0EndDate != null && band0EndDate > 0) ? band0StartDate : Calendar.getInstance().get(Calendar.YEAR),
                (band0Border != null && !band0Border.isEmpty()) ? band0Border : defaultBorderColour,
                (band0Fill != null && !band0Fill.isEmpty()) ? band0Fill : band0DefaultFill
        );
        
        if (showBand1) {
            wmsParameters = wmsParameters + getBandingParameters(
                (band1StartDate != null && band1StartDate > 0) ? band1StartDate : 1,
                (band1EndDate != null && band1EndDate > 0) ? band1StartDate : Calendar.getInstance().get(Calendar.YEAR),
                (band1Border != null && !"".equals(band1Border)) ? band1Border : defaultBorderColour,
                (band1Fill != null && !"".equals(band1Fill)) ? band1Fill : band1DefaultFill
            );
        }
        
        if (showBand2) {
            wmsParameters = wmsParameters + getBandingParameters(
                (band2StartDate != null && band2StartDate > 0) ? band2StartDate : 1,
                (band2EndDate != null && band2EndDate > 0) ? band2StartDate : Calendar.getInstance().get(Calendar.YEAR),
                (band2Border != null && !"".equals(band2Border)) ? band2Border : defaultBorderColour,
                (band2Fill != null && !"".equals(band2Fill)) ? band2Fill : band2DefaultFill
            );
        }
        
        model.put("wmsParameters",wmsParameters);
        
        Integer minDate = getMinDate(band0StartDate, band1StartDate, band2StartDate);
        Integer maxDate = getMaxDate(band0StartDate, band1StartDate, band2StartDate);        
        
        //set mindate to 1AD if not provided
        minDate = (minDate == 0 ? 1 : minDate);
        
        //set maxDate to current year if not provided
        maxDate = (maxDate == 0 ? Calendar.getInstance().get(Calendar.YEAR) : maxDate);
        
        List<String> datasetList = getDatasets(minDate, maxDate, tvk, viceCountyId, datasets);
        
        model.put("datasets", datasetList);
        
        //add errors to model if any    
        if (! errors.isEmpty()){
            model.put("errors", errors);
        }
        //call page
        return new ModelAndView("easyMap", model);
    }

    private String getMapSizeParam(Integer mapWidth, Integer mapHeight) {
        String p = "&WIDTH=";
        if (mapWidth != null && mapWidth > 0) {
            p = p + mapWidth.toString();
        } else {
            p = p + "350";
        }
        
        p = p + "&HEIGHT=";
        if (mapHeight != null && mapHeight > 0) {
            p = p + mapHeight.toString();
        } else {
            p = p + "350";
        }
                
        return p;
    }

    private String getMapLayersParam(boolean showFeatureLayer, boolean showBand1, boolean showBand2, String mapBackground, String gridResolution) {
        String p = "&LAYERS=";
        
        //Always show country outlines
        p = p + "gb-coast,ireland-coast";
        
        if (mapBackground != null) {
            if (mapBackground == "os") {
                p = p + ",OS-Scale-Dependent";
            } else if (mapBackground == "vc") {
                p = p + ",Vice-counties";
            } else {
                errors.add("Invalid map background specified");
            }
        }
        
        String resolutionPrefix = "";
        if (gridResolution == null || gridResolution.isEmpty()) {
            resolutionPrefix = "Grid-10km";
        } else if (gridResolution == "100m") {
            resolutionPrefix = "Grid-100m";
        } else if (gridResolution == "1km") {
            resolutionPrefix = "Grid-1km";
        } else if (gridResolution == "2km") {
            resolutionPrefix = "Grid-2km";
        } else {
            errors.add("Invalid grid resolution has been specified");
            return "";
        }
        
        //always show band 0
        p = p + "," + resolutionPrefix + "_Band_0";
        if (showBand1) p = p + "," + resolutionPrefix + "_Band_1";
        if (showBand2) p = p + "," + resolutionPrefix + "_Band_2";
        
        if (showFeatureLayer) p = p + ",Selected-Feature";
        
        return p;
    }

    private String getBoundingBoxParam(Feature vcFeature, String zoomLocation) {
        String p = "&BBOX=";
        
        if (vcFeature != null) {
            BoundingBox bbox = vcFeature.getNativeBoundingBox();
            p = p + bbox.getMinX().toString();
            p = p + "," + bbox.getMinY().toString();
            p = p + "," + bbox.getMaxX().toString();
            p = p + "," + bbox.getMaxY().toString();
        } else if (zoomLocation != null && !zoomLocation.isEmpty()) {
            p = p + getCustomBoundingBox(zoomLocation);
        } else {
            p = p + "-200000,-200000,1070000,1070000";
        }
        
        return p;
    }

    private Feature getViceCountyFeature(Integer viceCountyId) {
        String path = "/features/" + viceCountyId.toString();
        
        return resource.path(path)
            .accept(MediaType.APPLICATION_JSON)
            .get(Feature.class);
    }

    private String getCustomBoundingBox(String location) {
        String b = "";
        
        //NB. all SRS 27700
        if ("england".equals(location)) {
            b = "80000,0,660000,660000";
        } else if ("scotland".equals(location)) {
            b = "50000,520000,480000,1230000";
        } else if ("wales".equals(location)) {
            b = "160000,160000,360000,400000";
        } else if ("highland".equals(location)) {
            b = "100000,730000,350000,990000";
        } else if ("sco-mainland".equals(location)) {
            b = "110000,525000,420000,980000";
        } else if ("outer-heb".equals(location)) {
            b = "0,770000,160000,970000";
        } else {
            errors.add("unknown zoom location");
        }
        
        return b;
    }

    private String getBandingParameters(Integer startDate, Integer endDate, String borderColour, String fillColour) {
        return "&band=" + String.format("%04d", startDate) + "-" + String.format("%04d", endDate) + "," + fillColour + "," + borderColour;        
    }

    private boolean showBand(Integer StartDate, Integer EndDate) {
        if (StartDate != null && StartDate > 0) return true;
        if (EndDate != null && EndDate > 0) return true;
        return false;
    }

    private Integer getMinDate(Integer band0StartDate, Integer band1StartDate, Integer band2StartDate) {
        int a = band0StartDate == null ? 0 : band0StartDate;
        int b = band1StartDate == null ? 0 : band1StartDate;
        int c = band2StartDate == null ? 0 : band2StartDate;
        
        return Math.min(a, Math.min(b,c));
    }

    private Integer getMaxDate(Integer band0StartDate, Integer band1StartDate, Integer band2StartDate) {
        int a = band0StartDate == null ? 0 : band0StartDate;
        int b = band1StartDate == null ? 0 : band1StartDate;
        int c = band2StartDate == null ? 0 : band2StartDate;
        
        return Math.max(a, Math.max(b,c));
    }

    private List<String> getDatasets(Integer startYear, Integer endYear, String tvk, Integer featureId, String datasets) {
        if (tvk == null || tvk.isEmpty()) return null; //need a tvk.
                
        TaxonDatasetWithQueryStats[] datasetList = resource.path("/taxonObservations/datasets")
            .queryParam("ptvk", tvk)
            .queryParam("startYear", String.format("%04d", startYear))
            .queryParam("endYear", String.format("%04d", endYear))
            .queryParam("featureID", (featureId != null ? featureId.toString() : ""))
            .queryParam("datasetKey", (datasets != null ? datasets : ""))
            .accept(MediaType.APPLICATION_JSON)
            .get(TaxonDatasetWithQueryStats[].class);
        
        ArrayList<String> results = new ArrayList<String>();
        
        for(TaxonDatasetWithQueryStats datasetAndStats : datasetList) {
            TaxonDataset dataset = datasetAndStats.getTaxonDataset();
            
            String datasetTitle = dataset.getTitle();
            
            String orgName = dataset.getOrganisationName();
            
            results.add(orgName + " - " + datasetTitle);
        }
        
        return results;
    }
}
