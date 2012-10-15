package uk.gov.nbn.data.gis.interceptors;

import com.sun.jersey.api.client.WebResource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.gov.nbn.data.gis.processor.Interceptor;
import uk.gov.nbn.data.gis.processor.Intercepts;
import uk.gov.nbn.data.gis.processor.MapServiceMethod.Type;
import uk.gov.nbn.data.gis.providers.annotations.DefaultValue;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.org.nbn.nbnv.api.model.BoundingBox;
import uk.org.nbn.nbnv.api.model.Feature;

@Component
@Interceptor
public class GridMapMapViewportInterceptor {
    private static int MINX = 0, MINY = 1, MAXX = 2, MAXY=3; //definition of where bbox values live
    private static int MAP_SERVER_IMAGE_MAXIMUM_DIMENSION_VALUE = 2048;
    private static int ZOOM_LEVELS = 15;

    @Autowired WebResource resource;
    
    @Intercepts(Type.MAP)
    public Map<String, String[]> processRequestParameters(
                    GridMap gridMapProperties, 
                    GridMap.GridLayer layer,
                    @QueryParam(key="imagesize", validation="1[0-5]|[1-9]") @DefaultValue("10") String imagesizeStr,
                    @QueryParam(key="feature") String featureId) {
        Map<String, String[]> toReturn = new HashMap<String,String[]>();
        int[] orderedResolutions = getOrderedResolutions(layer.resolutions());
        int imageSize = Integer.parseInt(imagesizeStr), gcd = gcd(orderedResolutions); //calculate the greatest common divisor for the resolution of the map
        BoundingBox featureToFocusOn = getFeatureToFocusOn(featureId, gridMapProperties);
        int[] griddedBBox = getFeatureBoundingBoxFixedToGrid(featureToFocusOn, orderedResolutions[orderedResolutions.length-1]); //fix to the largest resolution used
        
        int amountOfSquaresX = getAmountOfSquaresInDimension(griddedBBox[MAXX], griddedBBox[MINX], gcd);
        int amountOfSquaresY = getAmountOfSquaresInDimension(griddedBBox[MAXY], griddedBBox[MINY], gcd);
        
        int amountOfPixelsForGrid = Math.min(   getMaximumPixelsForGridSquare(amountOfSquaresX, imageSize),
                                                getMaximumPixelsForGridSquare(amountOfSquaresY, imageSize));
        toReturn.put("SRS",     new String[]{ featureToFocusOn.getEpsgCode() });
        toReturn.put("HEIGHT",  new String[]{ Integer.toString(amountOfPixelsForGrid * amountOfSquaresY) });
        toReturn.put("WIDTH",   new String[]{ Integer.toString(amountOfPixelsForGrid * amountOfSquaresX) });
        toReturn.put("BBOX", new String[] {
            new StringBuilder()
                .append(Integer.toString(griddedBBox[MINX])).append(",")
                .append(Integer.toString(griddedBBox[MINY])).append(",")
                .append(Integer.toString(griddedBBox[MAXX])).append(",")
                .append(Integer.toString(griddedBBox[MAXY])).toString()
        });
        return toReturn;
    }
    
    private BoundingBox getFeatureToFocusOn(String featureId, GridMap gridMapProperties) {
        if(featureId != null) {
            //Interact with the data api to find the bounding box which should be 
            //used for the viewport from the passed in feature
            return resource
                    .path("features")
                    .path(featureId)
                    .get(Feature.class)
                    .getNativeBoundingBox();
        }
        else {
            //No feature has been defined. Zoom to the bounding box as specfied 
            //in the grid map annotation
            int[] defaultExtent = gridMapProperties.defaultExtent();
            return new BoundingBox(gridMapProperties.epsgCode(), 
                    BigDecimal.valueOf(defaultExtent[MINX]),
                    BigDecimal.valueOf(defaultExtent[MINY]),
                    BigDecimal.valueOf(defaultExtent[MAXX]),
                    BigDecimal.valueOf(defaultExtent[MAXY]));
        }
    }
    
    private static int[] getFeatureBoundingBoxFixedToGrid(BoundingBox featureBox, int resolution) {
        int minX = featureBox.getMinX().intValue(), 
            minY = featureBox.getMinY().intValue(),
            maxX = featureBox.getMaxX().intValue(),
            maxY = featureBox.getMaxY().intValue();
        //Calculate the gridded bounding box which the passed in feature fully sits in
        return new int[]{
            minX - modulo(minX, resolution),
            minY - modulo(minY, resolution),
            maxX - modulo(maxX, -resolution),
            maxY - modulo(maxY, -resolution)
        };
    }
    
    private static int getAmountOfSquaresInDimension(int viewportDimensionMin, int viewportDimensionMax, int resolution) {
        return Math.abs(viewportDimensionMin - viewportDimensionMax)/resolution;
    }
    
    private static int getMaximumPixelsForGridSquare(int gridSquares, int imageSize) {      
        int pixelsForGridSquare =   (MAP_SERVER_IMAGE_MAXIMUM_DIMENSION_VALUE * imageSize)
                                                            /
                                    (ZOOM_LEVELS * gridSquares);
        
        if(pixelsForGridSquare == 0) {
            throw new IllegalArgumentException("It is not possible to create an image for the given parameters.");
        }
        return pixelsForGridSquare;
    }
    
    /** The following is an implementation of modulo based around javas 
     * remainder operation **/
    private static int modulo(int i, int j) {
        int rem = i % j;
        if ((j < 0 && rem > 0) || (j > 0 && rem < 0)) {
            return rem + j;
        }
        return rem;
    }
    
    /** Calculate the Greatest common divisor for an array of resolutions.
     * @param values A sorted array of ints to find the greatest common divisor of. 
     *  There must be at least one value in the array
     * @return The greatest common divisor of the array of ints
     **/
    private static int gcd(int[] values) {
        int i = 0, gcd = values[0];
        while(++i < values.length) {
            gcd = gcd(gcd, values[i]);
        }
        return gcd;
    }
    
    /** 
     * Calculates the greatest common divisor between two integers. Where a < b
     * Using the Euclidean Algorithm.
     */
    private static int gcd(int a, int b) {
        while(b != 0) {
            int m = a % b;
            a = b;
            b = m;
        }
        return a;
    }
    
    /** Simple method to validate and order the resolutions array */
    private static int[] getOrderedResolutions(int[] resolutions) {
        if(resolutions.length == 0) {
            throw new IllegalArgumentException("At least one resolution must be defined for the layer");
        }
        else {
            int[] toReturn = Arrays.copyOf(resolutions, resolutions.length); //get the resolutions
            Arrays.sort(toReturn); //sort the resolutions
            return toReturn;
        }
    }
}
