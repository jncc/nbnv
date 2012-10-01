package uk.gov.nbn.data.gis.atlas;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.atlas.AtlasGrade;
import uk.gov.nbn.data.gis.processor.atlas.AtlasGradeProcessor;
import uk.gov.nbn.data.gis.processor.atlas.Type;

@Component
@AtlasGrade({Type.MAP })
public class ImageSizeBboxHelper implements AtlasGradeProcessor{
    //This is a good zoom to include Ireland and a bit of the North Sea
    private static final int BBOX_BOTTOM = -50000;
    private static final int BBOX_LEFT = -250000;
    private static final int BBOX_TOP = 1300000;
    private static final int BBOX_RIGHT = 750000;
    private static final int DEFAULT_IMAGE_SIZE = 10;
    private static final String PARAMETER_KEY_IMAGE_SIZE = "imagesize";

    @Override
    public Map<String, String[]> processRequestParameters(MapServiceMethod method, Map<String, String[]> query) {
        Map<String, String[]> toReturn = new HashMap<String,String[]>();
        int imageSize = getImageSize(query);
        
        toReturn.put("BBOX", new String[] {
            new StringBuilder()
                .append(BBOX_LEFT).append(",")
                .append(BBOX_BOTTOM).append(",")
                .append(BBOX_RIGHT).append(",")
                .append(BBOX_TOP).toString()
        });
        toReturn.put("HEIGHT", new String[] {Integer.toString(getImageHeight(imageSize))});
        toReturn.put("WIDTH", new String[] {Integer.toString(getImageWidth(imageSize))});
        toReturn.put("SRS", new String[]{"EPSG:27700"});
        return toReturn;
    }
    
    public static int getImageWidth(int imageSize){
        return imageSize * getBboxWidth()/10000;
    }

    public static int getImageHeight(int imageSize){
        return imageSize * getBboxHeight()/10000;
    }

    private static int getBboxWidth(){
        return BBOX_RIGHT - BBOX_LEFT;
    }

    private static int getBboxHeight(){
        return BBOX_TOP - BBOX_BOTTOM;
    }
    
    private static int getImageSize(Map<String, String[]> query) {
        if(!query.containsKey(PARAMETER_KEY_IMAGE_SIZE)){
            return DEFAULT_IMAGE_SIZE;
        }else{
            return Integer.parseInt(query.get(PARAMETER_KEY_IMAGE_SIZE)[0]);
        }
    }
}
