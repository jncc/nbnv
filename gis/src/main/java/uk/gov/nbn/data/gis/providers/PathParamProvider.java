package uk.gov.nbn.data.gis.providers;

import java.lang.reflect.Type;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Annotations;
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
    public boolean isProviderFor(Class<?> clazz, Type type, Annotations annotations) {
        return annotations.containsKey(PathParam.class);
    }

    @Override
    public String provide(Class<?> clazz, Type type, MapServiceMethod method, HttpServletRequest request, Annotations annotations) throws ProviderException {
        PathParam paramAnnot = annotations.get(PathParam.class);
        String toReturn = method.getVariableValue(paramAnnot.key());
        if(paramAnnot.validation().equals(PathParam.NO_VALIDATION) || toReturn.matches(paramAnnot.validation())) {
            return toReturn;
        }
        else {
            throw new ProviderException("The param " + toReturn + " was not in the correct format");
        }
    }
}
