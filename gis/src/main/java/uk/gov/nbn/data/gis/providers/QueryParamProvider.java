package uk.gov.nbn.data.gis.providers;

import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;
import uk.gov.nbn.data.gis.processor.ProviderException;
import uk.gov.nbn.data.gis.providers.annotations.DefaultValue;

/**
 * Provides Map Service Methods with the value of a query parameter.
 * If the Annotated type was a List, the value will be assumed to be comma
 * separated and each value will be validated against a given validation regex if
 * supplied
 * @author Chris Johnson
 */
@Component
public class QueryParamProvider implements Provider {

    @Override
    public boolean isProviderFor(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) {
        return getAnnotation(annotations, QueryParam.class) != null;
    }

    @Override
    public Object provide(Class<?> returnType, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) throws ProviderException {
        QueryParam paramAnnot = getAnnotation(annotations, QueryParam.class);
        String toReturn = getParameter(request, paramAnnot.key(), getAnnotation(annotations, DefaultValue.class));
        if(toReturn != null) {
            if(returnType.equals(List.class)) {
                return validateParameter(Arrays.asList(toReturn.split(",")), paramAnnot);
            }
            else {
                return validateParameter(toReturn, paramAnnot);
            }
        }
        else {
            return null;
        }
    }
    
    private static String getParameter(HttpServletRequest request, String key, DefaultValue defaultValAnnot) {
        String defaultVal = (defaultValAnnot != null) ? defaultValAnnot.value() : null;
        String userDefinedValue = request.getParameter(key);
        return (userDefinedValue == null) ? defaultVal : userDefinedValue;
    }
    
    private static String validateParameter(String toValidate, QueryParam annotation) throws ProviderException {
        if(annotation.validation().equals(QueryParam.NO_VALIDATION) || toValidate.matches(annotation.validation())) {
            return toValidate;
        }
        else {
            throw new ProviderException("The queryparam " + toValidate + " was not in the correct format");
        }
    }
    
    private static List<String> validateParameter(List<String> toValidate, QueryParam annotation) throws ProviderException {
        if(!annotation.validation().equals(QueryParam.NO_VALIDATION)) {
            for(String currValue : toValidate) {
                if(!currValue.matches(annotation.validation())) {
                     throw new ProviderException("The queryparam " + currValue + " was not in the correct format");
                }
            }
        }
        return toValidate;
    }
    
    private static <A extends Annotation> A getAnnotation(List<Annotation> annotations, Class<A> type) {
        for(Annotation currAnnotation : annotations) {
            if(type.equals(currAnnotation.annotationType())) {
                return (A)currAnnotation;
            }
        }
        return null;
    }
}
