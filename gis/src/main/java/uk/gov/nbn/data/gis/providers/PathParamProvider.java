package uk.gov.nbn.data.gis.providers;

import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;
import uk.gov.nbn.data.gis.processor.ProviderException;

/**
 * Provides the concrete value of a Param which was contained within the 
 * calling url
 * @author Chris Johnson
 */
@Component
public class PathParamProvider implements Provider {

    @Override
    public boolean isProviderFor(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) {
        return getAnnotation(annotations) != null;
    }

    @Override
    public String provide(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) throws ProviderException {
        PathParam paramAnnot = getAnnotation(annotations);
        String toReturn = method.getVariableValue(paramAnnot.key());
        if(paramAnnot.validation().equals(PathParam.NO_VALIDATION) || toReturn.matches(paramAnnot.validation())) {
            return toReturn;
        }
        else {
            throw new ProviderException("The param " + toReturn + " was not in the correct format");
        }
    }
    
    private static PathParam getAnnotation(List<Annotation> annotations) {
        for(Annotation currAnnotation : annotations) {
            if(currAnnotation instanceof PathParam) {
                return (PathParam)currAnnotation;
            }
        }
        return null;
    }
}
