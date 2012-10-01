package uk.gov.nbn.data.gis.atlas;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.atlas.AtlasGrade;
import uk.gov.nbn.data.gis.processor.atlas.AtlasGradeProcessor;
import uk.gov.nbn.data.gis.processor.atlas.Type;

/**
 *
 * @author Christopher Johnson
 */
@Component
@AtlasGrade({Type.MAP })
public class WMSMapAtlasGradeProcessor implements AtlasGradeProcessor{
    private static final Map<String, String> FORMATS;
    
    static {
        FORMATS = new HashMap<String, String>();
        FORMATS.put("png", "image/png");
        FORMATS.put("gif", "image/gif");
        FORMATS.put("jpeg", "image/jpeg");
    }
    
    @Override
    public Map<String, String[]> processRequestParameters(MapServiceMethod method, Map<String, String[]> query) {
        Map<String, String[]> toReturn = new HashMap<String, String[]>();
        toReturn.put("SERVICE", new String[]{"WMS"});
        toReturn.put("VERSION", new String[]{"1.1.1"});
        toReturn.put("REQUEST", new String[]{"GetMap"});
        toReturn.put("STYLES", new String[]{""});
        toReturn.put("LAYERS", new String[]{
            StringUtils.arrayToCommaDelimitedString(method.getAtlasGradeAnnotation().layers())
        });
        toReturn.put("TRANSPARENT", new String[]{"true"});
        toReturn.put("FORMAT", new String[]{ checkAndGetValue(FORMATS, getValue(query, "format", "png")) });
        return toReturn;
    }
    
    private static String checkAndGetValue(Map<String, String> map, String toGet) {
        if(!map.containsKey(toGet)) {
            throw new IllegalArgumentException("I don't understand " + toGet + 
                    " valid values are " + map.keySet());
        }
        return map.get(toGet);
    }
    
    private static String getValue(Map<String, String[]> query, String toGet, String defaultVal) {
        return (query.containsKey(toGet)) ? query.get(toGet)[0] : defaultVal;
    }
}
