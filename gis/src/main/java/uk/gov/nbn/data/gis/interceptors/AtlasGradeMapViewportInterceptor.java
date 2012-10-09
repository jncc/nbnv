package uk.gov.nbn.data.gis.interceptors;

import com.sun.jersey.api.client.WebResource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.AtlasGrade;
import uk.gov.nbn.data.gis.processor.Interceptor;
import uk.gov.nbn.data.gis.processor.Intercepts;
import uk.gov.nbn.data.gis.processor.MapServiceMethod.Type;
import uk.gov.nbn.data.gis.providers.annotations.DefaultValue;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.org.nbn.nbnv.api.model.BoundingBox;
import uk.org.nbn.nbnv.api.model.Feature;

@Component
@Interceptor
public class AtlasGradeMapViewportInterceptor {
    private static int MINX = 0, MINY = 1, MAXX = 2, MAXY=3; //definition of where bbox values live
    private static int MAP_SERVER_IMAGE_MAXIMUM_DIMENSION_VALUE = 2048;
    private static int ZOOM_LEVELS = 15;

    @Autowired WebResource resource;
    
    @Intercepts(Type.MAP)
    public Map<String, String[]> processRequestParameters(
                    AtlasGrade atlasGradeProperties, 
                    AtlasGrade.GridLayer layer,
                    @QueryParam(key="imagesize", validation="1[0-5]|[1-9]") @DefaultValue("10") String imagesizeStr,
                    @QueryParam(key="feature") String featureId) {
        Map<String, String[]> toReturn = new HashMap<String,String[]>();
        int imageSize = Integer.parseInt(imagesizeStr), lcmResolution = layer.lcmResolution();
        BoundingBox featureToFocusOn = getFeatureToFocusOn(featureId, atlasGradeProperties);
        int[] griddedBBox = getFeatureBoundingBoxFixedToGrid(featureToFocusOn, layer.snapToResolution());
        
        int amountOfSquaresX = getAmountOfSquaresInDimension(griddedBBox[MAXX], griddedBBox[MINX], lcmResolution);
        int amountOfSquaresY = getAmountOfSquaresInDimension(griddedBBox[MAXY], griddedBBox[MINY], lcmResolution);
        
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
    
    private BoundingBox getFeatureToFocusOn(String featureId, AtlasGrade atlasGradeProperties) {
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
            //in the atlas grade map annotation
            int[] defaultExtent = atlasGradeProperties.defaultExtent();
            return new BoundingBox(atlasGradeProperties.epsgCode(), 
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
}
