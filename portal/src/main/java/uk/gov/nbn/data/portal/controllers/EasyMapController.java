/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.nbn.data.portal.exceptions.InvalidFeatureIdentifierException;
import uk.org.nbn.nbnv.api.model.BoundingBox;
import uk.org.nbn.nbnv.api.model.Feature;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithQueryStats;

/**
 *
 * @author paulbe
 */
@Controller
public class EasyMapController {
    @Autowired WebResource resource;
    private List<String> errors;
    private String viceCountyDataset = "GA000344";
    
    public EasyMapController() {
        errors = new ArrayList<String>();
    }
    
    @RequestMapping(value = "/EasyMap/css")
    @ResponseBody
    public void proxyCSS(HttpServletResponse response, 
                         @RequestParam(value="url") String css) throws IOException {
        //Open a connection to the host
        URLConnection connection = new URL(css).openConnection();
        //Set the headers from the host
        Map<String, List<String>> headers = new HashMap<String, List<String>>(connection.getHeaderFields());
        headers.put("Content-Type", Arrays.asList("text/css")); //Force a css content type
        for(Entry<String, List<String>> header : headers.entrySet()) {
            for(String value :header.getValue()) {
                response.addHeader(header.getKey(), value);
            }
        }
        //Copy the content
        InputStream in = connection.getInputStream();
        try {
            OutputStream out = response.getOutputStream();
            try {
                IOUtils.copyLarge(in, out);
            }
            finally {
                out.flush();
                out.close();
            }
        } finally {
            in.close();
        }
    }
    
    /**
     * Determine if the url is already https, if it is we can leave it as is. Else,
     * lets proxy it
     * @param cssUrl The external css to proxy
     * @return a https secured proxy or the original url
     */
    private String getCSSURL(String cssUrl) throws UnsupportedEncodingException {
        if(cssUrl.startsWith("https")) {
            return cssUrl;
        }
        else {
            return "/EasyMap/css?url=" + URLEncoder.encode(cssUrl, "UTF-8");
        }
    }
    
    @RequestMapping(value = "/EasyMap", method = RequestMethod.GET)
    public ModelAndView getCreatePage(
            HttpServletRequest request,
            @RequestParam(value="tvk") String tvk
            ,@RequestParam(value="ds", required=false) String datasets
            ,@RequestParam(value="res", required=false) String gridResolution
            ,@RequestParam(value="w", required=false) Integer mapWidth
            ,@RequestParam(value="h", required=false) Integer mapHeight
            ,@RequestParam(value="gd", required=false) String gridOverlay
            ,@RequestParam(value="bg", required=false) String mapBackground
            ,@RequestParam(value="vc", required=false) Integer viceCountyId
            ,@RequestParam(value="b0from", required=false) Integer band0StartDate
            ,@RequestParam(value="b0to", required=false) Integer band0EndDate
            ,@RequestParam(value="b0fill", required=false) String band0Fill
            ,@RequestParam(value="b0bord", required=false) String band0Border
            ,@RequestParam(value="b1from", required=false) Integer band1StartDate
            ,@RequestParam(value="b1to", required=false) Integer band1EndDate
            ,@RequestParam(value="b1fill", required=false) String band1Fill
            ,@RequestParam(value="b1bord", required=false) String band1Border
            ,@RequestParam(value="b2from", required=false) Integer band2StartDate
            ,@RequestParam(value="b2to", required=false) Integer band2EndDate
            ,@RequestParam(value="b2fill", required=false) String band2Fill
            ,@RequestParam(value="b2bord", required=false) String band2Border
            ,@RequestParam(value="zoom", required=false) String zoomLocation
            ,@RequestParam(value="title", required=false) String titleType
            ,@RequestParam(value="ref", required=false) Integer displayDatasets
            ,@RequestParam(value="maponly", required=false) Integer onlyDisplayMap
            ,@RequestParam(value="logo", required=false) Integer displayLogo
            ,@RequestParam(value="css", required=false) String css
            ,@RequestParam(value="terms", required=false) Integer displayTerms
            ,@RequestParam(value="link", required=false) String displayImtLink
            ,@RequestParam(value="bl", required=false) String bottomLeft
            ,@RequestParam(value="tr", required=false) String topRight
            ,@RequestParam(value="blCoord", required=false) String bottomLeftCoord
            ,@RequestParam(value="trCoord", required=false) String topRightCoord) throws UnsupportedEncodingException {
        
        Map<String, Object> model = new HashMap<String, Object>();
        errors = new ArrayList<String>();
        
        //check if tvk is valid and return error page.
        if (tvk == null || tvk.isEmpty()) {
            errors.add("You must supply a tvk");
            model.put("errors", errors);
            return new ModelAndView("easyMap", model);
        }
        
        model.put("tvk", tvk);
       
        
        //create wms query string
        String wmsParameters = "abundance=presence&FORMAT=image%2Fpng&TRANSPARENT=TRUE&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&SRS=EPSG%3A27700";
        
        if (!(datasets == null || datasets.isEmpty())) wmsParameters = wmsParameters + "&datasets=" + datasets;

        wmsParameters = wmsParameters + getMapSizeParam(mapWidth, mapHeight);
        
        boolean showBand1 = shouldShowBand(band1StartDate, band1EndDate);
        boolean showBand2 = shouldShowBand(band2StartDate, band2EndDate);
        boolean showFeatureLayer = (viceCountyId != null &&  viceCountyId > 0);
        
        wmsParameters = wmsParameters + getMapLayersParam(showFeatureLayer, showBand1, showBand2, mapBackground, gridResolution, gridOverlay);
       
        Feature vcFeature = null;
        if (showFeatureLayer) vcFeature = getViceCountyFeature(viceCountyId);
        
        if (viceCountyId != null && viceCountyId > 0 && vcFeature == null) {
            errors.add("Invalid vice county id");
        }

        wmsParameters = wmsParameters + getFeatureParam(vcFeature);
        
        //check if we have any matching datasets        
        Integer minDate = getMinDate(band0StartDate, band1StartDate, band2StartDate);
        //set mindate to 1AD if not provided
        minDate = (minDate == 0 ? 1 : minDate);
        
        Integer maxDate = getMaxDate(band0EndDate, band1EndDate, band2EndDate);        
       //set maxDate to current year if not provided
        maxDate = (maxDate == 0 ? Calendar.getInstance().get(Calendar.YEAR) : maxDate);
        
        String featureIdentifier = vcFeature != null ? vcFeature.getIdentifier() : null;
        TaxonDatasetWithQueryStats[] TaxonDatasetList = getDatasets(minDate, maxDate, tvk, featureIdentifier, datasets);
        if (TaxonDatasetList == null || TaxonDatasetList.length == 0 ){
            errors.add("No data returned for the criteria you have specified");
            model.put("errors", errors);
            return new ModelAndView("easyMap", model);
        }
        try {
            wmsParameters = wmsParameters + 
                    getBoundingBoxParam(vcFeature, zoomLocation, bottomLeft, 
                                        topRight, bottomLeftCoord, 
                                        topRightCoord);
        } catch (InvalidFeatureIdentifierException ex) {
            errors.add("Could not find a feature with the identifier " + ex.getIdentifier() + " :: " + ex.getLocalizedMessage());
            model.put("errors", errors);
            return new ModelAndView("easyMap", model);
        }
        
        String defaultBorderColour = "000000";
        String band0DefaultFill = "FF0000";
        String band1DefaultFill = "FF9900";
        String band2DefaultFill = "FFFF00";
        
        wmsParameters = wmsParameters + getBandingParameters(
                (band0StartDate != null && band0StartDate > 0) ? band0StartDate : 1,
                (band0EndDate != null && band0EndDate > 0) ? band0EndDate : Calendar.getInstance().get(Calendar.YEAR),
                (band0Border != null && !band0Border.isEmpty()) ? band0Border : defaultBorderColour,
                (band0Fill != null && !band0Fill.isEmpty()) ? band0Fill : band0DefaultFill
        );
        
        if (showBand1) {
            wmsParameters = wmsParameters + getBandingParameters(
                (band1StartDate != null && band1StartDate > 0) ? band1StartDate : 1,
                (band1EndDate != null && band1EndDate > 0) ? band1EndDate : Calendar.getInstance().get(Calendar.YEAR),
                (band1Border != null && !band1Border.isEmpty()) ? band1Border : defaultBorderColour,
                (band1Fill != null && !band1Fill.isEmpty()) ? band1Fill : band1DefaultFill
            );
        }
        
        if (showBand2) {
            wmsParameters = wmsParameters + getBandingParameters(
                (band2StartDate != null && band2StartDate > 0) ? band2StartDate : 1,
                (band2EndDate != null && band2EndDate > 0) ? band2EndDate : Calendar.getInstance().get(Calendar.YEAR),
                (band2Border != null && !band2Border.isEmpty()) ? band2Border : defaultBorderColour,
                (band2Fill != null && !band2Fill.isEmpty()) ? band2Fill : band2DefaultFill
            );
        }

        model.put("wmsParameters",wmsParameters);
        
        if (css != null && !css.isEmpty()) model.put("css", getCSSURL(css));
        
        if (onlyDisplayMap == null || onlyDisplayMap == 0) {

            //get datasets list
            if (displayDatasets == null || displayDatasets == 1) {

                List<String> datasetList = getDatasetList(TaxonDatasetList);
                model.put("datasets", datasetList); 
            } else if (displayDatasets != 0 ) {
                errors.add("Invalid value for ref parameter");
            }

            //Get page title
            String pageTitle = getPageTitle(titleType, tvk);
            if (! pageTitle.isEmpty()) model.put("pageTitle", pageTitle);
            
            //show logo
            if (displayLogo == null || displayLogo == 1) {
                model.put("showLogo", "1");
            } 
            
            if (displayTerms == null || !"0".equals(displayTerms)) {
                model.put("showTC", "1");
            }
            
            //get parameters for link to imt
            if (displayImtLink == null || !"0".equals(displayImtLink)) {
                String imtLinkParams = getImtLinkParams(tvk, request);
                model.put("imtLinkParms", imtLinkParams);
            }
        
        } else if (onlyDisplayMap > 1) {
            errors.add("Invalid value for maponly");
        }
        
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

    private String getMapLayersParam(boolean showFeatureLayer, boolean showBand1, boolean showBand2, String mapBackground, String gridResolution, String gridOverlay) {
        String p = "&LAYERS=";
        
        //Always show country outlines
        if (gridOverlay == null || gridOverlay.isEmpty()) {
            p = p + "gb-coast,ireland-coast";
        } else if ("10km".equalsIgnoreCase(gridOverlay)) {
            p = p + "GB-Ten-km-Grid,gb-coast,ireland-coast,Ireland-Ten-km-Grid";
        } else if ("100km".equalsIgnoreCase(gridOverlay)) {
            p = p + "GB-Coast-with-Hundred-km-Grid,Ireland-Coast-with-Hundred-km-Grid";
        } else if ("10km_100km".equalsIgnoreCase(gridOverlay)) {
            p = p + "GB-Coast-with-Hundred-km-Grid,Ireland-Coast-with-Hundred-km-Grid,GB-Ten-km-Grid,Ireland-Ten-km-Grid";
        } else {
            errors.add("Invalid grid overlay resolution");
        }
        
        
        
        if (mapBackground != null) {
            if ("os".equalsIgnoreCase(mapBackground)) {
                p = p + ",OS-Scale-Dependent";
            } else if ("vc".equalsIgnoreCase(mapBackground)) {
                p = p + ",Vice-counties";
            } else {
                errors.add("Invalid map background specified");
            }
        }
        
        String resolutionPrefix = "";
        if (gridResolution == null || gridResolution.isEmpty()) {
            resolutionPrefix = "Grid-10km";
        } else if ("100m".equalsIgnoreCase(gridResolution)) {
            resolutionPrefix = "Grid-100m";
        } else if ("1km".equalsIgnoreCase(gridResolution)) {
            resolutionPrefix = "Grid-1km";
        } else if ("2km".equalsIgnoreCase(gridResolution)) {
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

    private String getBoundingBoxParam(Feature vcFeature, String zoomLocation, 
            String bottomLeft, String topRight, String bottomLeftCoords, 
            String topRightCoords) throws InvalidFeatureIdentifierException {
        String p = "&BBOX=";
        
        if (vcFeature != null) {
            BoundingBox bbox = vcFeature.getNativeBoundingBox();
            p = p + bbox.getMinX().toString();
            p = p + "," + bbox.getMinY().toString();
            p = p + "," + bbox.getMaxX().toString();
            p = p + "," + bbox.getMaxY().toString();
        } else if (zoomLocation != null && !zoomLocation.isEmpty()) {
            p = p + getCustomBoundingBox(zoomLocation);
        } else if (StringUtils.hasText(bottomLeft) && StringUtils.hasText(topRight)) {
            Feature bottom = getFeatureByIdentifier(bottomLeft);
            Feature top = getFeatureByIdentifier(topRight);
            
            p = p + bottom.getNativeBoundingBox().getMinX() + ","
                    + bottom.getNativeBoundingBox().getMinY() + ","
                    + top.getNativeBoundingBox().getMaxX() + ","
                    + top.getNativeBoundingBox().getMaxY();
        } else if (bottomLeftCoords != null && !bottomLeftCoords.isEmpty() && topRightCoords != null && !topRightCoords.isEmpty()) {
            p = p + bottomLeftCoords + "," + topRightCoords;
        } else {
            p = p + "-200000,-200000,1070000,1070000";
        }
        
        return p;
    }
    
    private Feature getFeatureByIdentifier(String identifier) throws InvalidFeatureIdentifierException {
        try {
            String path = "/features/" + identifier;

            return resource.path(path)
                .accept(MediaType.APPLICATION_JSON)
                .get(Feature.class);
        } catch (UniformInterfaceException ex) {
            InvalidFeatureIdentifierException invalid = new InvalidFeatureIdentifierException(ex.getLocalizedMessage());
            invalid.setIdentifier(identifier);
            throw invalid;
        }
    }

    private Feature getViceCountyFeature(Integer viceCountyId) {
        String path = "/features/" + viceCountyDataset + viceCountyId.toString();
        
        return resource.path(path)
            .accept(MediaType.APPLICATION_JSON)
            .get(Feature.class);
    }

    private String getCustomBoundingBox(String location) {
        String b = "";
        
        //NB. all SRS 27700
        if ("england".equals(location)) {
            b = "80000,0,660000,660000";
        } else if ("scotland".equalsIgnoreCase(location)) {
            b = "50000,520000,480000,1230000";
        } else if ("wales".equalsIgnoreCase(location)) {
            b = "160000,160000,360000,400000";
        } else if ("highland".equalsIgnoreCase(location)) {
            b = "100000,730000,350000,990000";
        } else if ("sco-mainland".equalsIgnoreCase(location)) {
            b = "110000,525000,420000,980000";
        } else if ("outer-heb".equalsIgnoreCase(location)) {
            b = "0,770000,160000,970000";
        } else {
            errors.add("unknown zoom location");
        }
        
        return b;
    }

    private String getBandingParameters(Integer startDate, Integer endDate, String borderColour, String fillColour) {
        return "&band=" + String.format("%04d", startDate) + "-" + String.format("%04d", endDate) + "," + fillColour + "," + borderColour;        
    }

    private boolean shouldShowBand(Integer StartDate, Integer EndDate) {
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

    private TaxonDatasetWithQueryStats[] getDatasets(Integer startYear, Integer endYear, String tvk, String featureIdentifier, String datasets) {
        if (tvk == null || tvk.isEmpty()) return null; //need a tvk.
        
        WebResource localResource = resource.path("/taxonObservations/datasets")
            .queryParam("ptvk", tvk)
            .queryParam("startYear", String.format("%04d", startYear))
            .queryParam("endYear", String.format("%04d", endYear))
            .queryParam("featureID", (featureIdentifier != null ? featureIdentifier : ""));
        
        // Handle list of datasets
        if (StringUtils.hasText(datasets)) {
            String[] datasetList = StringUtils.commaDelimitedListToStringArray(datasets);
            for (String ds : datasetList) {
                localResource = localResource.queryParam("datasetKey", ds);
            }
        }
        
        TaxonDatasetWithQueryStats[] datasetList = localResource
            .accept(MediaType.APPLICATION_JSON)
            .get(TaxonDatasetWithQueryStats[].class);
        
        return datasetList;
    }
    
    private List<String> getDatasetList(TaxonDatasetWithQueryStats[] datasetList ) {                
        
        ArrayList<String> results = new ArrayList<String>();
        
        for(TaxonDatasetWithQueryStats datasetAndStats : datasetList) {
            TaxonDataset dataset = datasetAndStats.getTaxonDataset();
            
            String datasetTitle = dataset.getTitle();
            
            String orgName = dataset.getOrganisationName();
            
            results.add(orgName + " - " + datasetTitle);
        }
        
        return results;
    }
    
    private String getPageTitle(String titleType, String tvk) {
        String title = "";
        
        
        if (titleType != null && "0".equals(titleType)) { 
            return title;
        }
         
        Taxon taxon = resource.path("/taxa/" + tvk)
            .accept(MediaType.APPLICATION_JSON)
            .get(Taxon.class);

        if (titleType == null || titleType.isEmpty() || "sci".equalsIgnoreCase(titleType)) {
            title = taxon.getName();
        } else if ("com".equalsIgnoreCase(titleType)) {
            title = taxon.getCommonName();
        } else {
            errors.add("Unknown value for title parameter");
        }
        
        return title;
    }

    private String getImtLinkParams(String tvk, HttpServletRequest request) {
        String params = "mode=SPECIES&species=" + tvk; 
        for(Entry<String, String[]> entry : ((Map<String, String[]>)request.getParameterMap()).entrySet()) {
            if (entry.getKey().startsWith("link_")) {
                String param = entry.getKey().substring(5);
                params = params + "&" + param + "=";

                for (String value : entry.getValue()) {
                    params = params + value + ",";
                }
                
                params = params.substring(0,(params.length() - 1));
            }
        }
        return params;
    }

    private String getFeatureParam(Feature vcFeature) {
        String param = "";
        if (vcFeature != null) param = "&feature=" + vcFeature.getIdentifier();
        return param;
    }


}
