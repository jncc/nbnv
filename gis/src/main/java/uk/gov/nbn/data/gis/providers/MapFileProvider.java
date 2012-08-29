package uk.gov.nbn.data.gis.providers;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;
import uk.gov.nbn.data.gis.providers.annotations.MapFile;

/**
 *
 * @author Chris Johnson
 */
public class MapFileProvider implements Provider {

    @Override
    public boolean isProviderFor(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) {
        return getAnnotation(annotations) != null;
    }

    @Override
    public String provide(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) {
        MapFile annotation = getAnnotation(annotations);
        return request.getRealPath("WEB-INF\\maps\\" + annotation.value());
    }
    
    private static MapFile getAnnotation(List<Annotation> annotations) {
        for(Annotation currAnnotation : annotations) {
            if(currAnnotation instanceof MapFile) {
                return (MapFile)currAnnotation;
            }
        }
        return null;
    }
}
