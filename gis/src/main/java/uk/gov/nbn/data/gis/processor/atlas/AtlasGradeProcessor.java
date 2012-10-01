package uk.gov.nbn.data.gis.processor.atlas;

import java.util.Map;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;

public interface AtlasGradeProcessor {
    Map<String, String[]> processRequestParameters(MapServiceMethod method, Map<String, String[]> query);
}

