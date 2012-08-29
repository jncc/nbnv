package uk.gov.nbn.data.gis.providers;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;

/**
 *
 * @author Chris Johnson
 */
public class ParamProvider implements Provider<Param, String> {

    @Override
    public Param providesFor(MapServiceMethod method, HttpServletRequest request, Class<?> clazz, List<Annotation> annotations) {
        for(Annotation currAnnotation : annotations) {
            if(currAnnotation instanceof Param) {
                return (Param)currAnnotation;
            }
        }
        return null;
    }

    @Override
    public String process(Param providesForResponse, MapServiceMethod method, HttpServletRequest request, Class<?> clazz, List<Annotation> annotations) {
        return method.getVariableValue(providesForResponse.value());
    }
    
}
