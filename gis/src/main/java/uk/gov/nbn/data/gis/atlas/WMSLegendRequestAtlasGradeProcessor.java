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
@AtlasGrade( Type.LEGEND )
public class WMSLegendRequestAtlasGradeProcessor implements AtlasGradeProcessor{

    @Override
    public Map<String, String[]> processRequestParameters(MapServiceMethod method, Map<String, String[]> query) {
        Map<String, String[]> toReturn = new HashMap<String, String[]>();
        toReturn.put("SERVICE", new String[]{"WMS"});
        toReturn.put("VERSION", new String[]{"1.1.1"});
        toReturn.put("REQUEST", new String[]{"GetLegendGraphic"});
        toReturn.put("TRANSPARENT", new String[]{"true"});
        toReturn.put("FORMAT", new String[]{"image/png"});
        toReturn.put("LAYER", new String[]{
            StringUtils.arrayToCommaDelimitedString(method.getAtlasGradeAnnotation().layers())
        });
        return toReturn;
    }
}
