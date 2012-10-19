/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.gis.interceptors;

import com.sun.jersey.api.client.WebResource;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.org.nbn.nbnv.api.model.BoundingBox;
import uk.org.nbn.nbnv.api.model.Feature;

/**
 * The following factory will generate grid map requests for a given boundingbox
 * resolution and imageSize
 * @author Christopher Johnson
 */
@Component
public class GridMapRequestFactory {
    public static final int ZOOM_LEVELS = 15;
    private static final int MAP_SERVER_IMAGE_MAXIMUM_DIMENSION_VALUE = 2048;
    private static final int MINX = 0, MINY = 1, MAXX = 2, MAXY=3; //definition of where bbox values live
    
    @Autowired WebResource resource;
    
    /**
     * The following method utilises the data api to create a bounding box to focus
     * on.
     * @param featureId A featureId to focus on. If null the gridMapProperties default will be used
     * @param gridMapProperties The Grid Map properties which will be used if featureId is null
     * @return A bounding box to focus on
     */
    public BoundingBox getFeatureToFocusOn(String featureId, GridMap gridMapProperties) {
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
    
    /**
     * Creates a grid map request for the given feature a resolution and a given image size.
     * 
     * Note that this method will not throw an exception if the request is not 
     * valid. You must check this by calling the method GridMapRequest#isValidRequest
     * @param toFocusOn The feature which will be the focus of the request
     * @param resolution The resolution of the grid layer which is requested
     * @param imageSize The imagesize which this request should be made
     * @return A GridMapRequest populated with the parameters to send to the map server
     */
    public GridMapRequest getGridMapRequest(BoundingBox toFocusOn, int resolution, int imageSize) {
        return new GridMapRequest(toFocusOn, resolution, imageSize);
    }
    
    public static class GridMapRequest {
        private final int[] griddedBBox;
        private final int amountOfSquaresX, amountOfSquaresY, amountOfPixelsForGrid;
        
        /**
         * Performs all the calculations required for creating a grid map request
         */
        private GridMapRequest(BoundingBox toFocusOn, int resolution, int imageSize) {
            this.griddedBBox = getFeatureBoundingBoxFixedToGrid(toFocusOn, resolution);
            this.amountOfSquaresX = getAmountOfSquaresInDimension(griddedBBox[MAXX], griddedBBox[MINX], resolution);
            this.amountOfSquaresY = getAmountOfSquaresInDimension(griddedBBox[MAXY], griddedBBox[MINY], resolution);
        
            this.amountOfPixelsForGrid = Math.min(   getMaximumPixelsForGridSquare(amountOfSquaresX, imageSize),
                                                     getMaximumPixelsForGridSquare(amountOfSquaresY, imageSize));
        }
        
        /**
         * The following method will determine if this request object is indeed valid. 
         * Certain combinations of feature, resolution and imagesize will result in
         * request objects which cannot create grid map requests with solid
         * @return Weather or not the request is valid
         */
        public boolean isValidRequest() {
            return amountOfPixelsForGrid != 0;
        }
        
        /**
         * Generates a bounding box string which is in the correct form for WMS
         * requests
         * @return A Comma seperated list. BBOX for wms request
         */
        public String getBBox() {
            return new StringBuilder()
                .append(Integer.toString(griddedBBox[MINX])).append(",")
                .append(Integer.toString(griddedBBox[MINY])).append(",")
                .append(Integer.toString(griddedBBox[MAXX])).append(",")
                .append(Integer.toString(griddedBBox[MAXY])).toString();
        }
        
        public int getHeight() {
            return amountOfPixelsForGrid * amountOfSquaresY;
        }
        
        public int getWidth() {
            return amountOfPixelsForGrid * amountOfSquaresX;
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
            return (MAP_SERVER_IMAGE_MAXIMUM_DIMENSION_VALUE * imageSize)
                                /
                   (ZOOM_LEVELS * gridSquares);
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
    }
}
