package uk.gov.nbn.data.gis.providers;

import uk.gov.nbn.data.gis.providers.annotations.Param;
import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;
import uk.gov.nbn.data.gis.processor.ProviderException;

/**
 *
 * @author Chris Johnson
 */
public class ParamProvider implements Provider {

    @Override
    public boolean isProviderFor(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) {
        return getAnnotation(annotations) != null;
    }

    @Override
    public String provide(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) throws ProviderException {
        Param paramAnnot = getAnnotation(annotations);
        String toReturn = method.getVariableValue(paramAnnot.key());
        if(paramAnnot.validation().equals(Param.NO_VALIDATION) || toReturn.matches(paramAnnot.validation())) {
            return toReturn;
        }
        else {
            throw new ProviderException("The param " + toReturn + " was not in the correct format");
        }
    }
    
    private static Param getAnnotation(List<Annotation> annotations) {
        for(Annotation currAnnotation : annotations) {
            if(currAnnotation instanceof Param) {
                return (Param)currAnnotation;
            }
        }
        return null;
    }
}
