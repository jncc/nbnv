package uk.gov.nbn.data.gis.providers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Annotations;
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
    public boolean isProviderFor(Class<?> clazz, Type type, Annotations annotations) {
        return annotations.containsKey(QueryParam.class);
    }

    @Override
    public Object provide(Class<?> returnType, Type type, MapServiceMethod method, HttpServletRequest request, Annotations annotations) throws ProviderException {
        QueryParam paramAnnot = annotations.get(QueryParam.class);
        String[] requestParams = getParameters(request, paramAnnot.key(), annotations.get(DefaultValue.class));
        if(requestParams != null) {
            if(returnType.equals(List.class)) {
                Class<?> elementType = (Class<?>)(((ParameterizedType)type).getActualTypeArguments()[0]);
                String[] toReturn = (paramAnnot.commaSeperated()) ? requestParams[0].split(",") : requestParams;
                return validateParameter(Arrays.asList(toReturn), paramAnnot, elementType);
            }
            else {
                return validateParameter(requestParams[0], paramAnnot, returnType);
            }
        }
        else {
            return null;
        }
    }
    
    private static String[] getParameters(HttpServletRequest request, String key, DefaultValue defaultValAnnot) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] userDefinedValue = parameterMap.get(key);
        return (userDefinedValue == null && defaultValAnnot != null) 
                ? new String[] {defaultValAnnot.value()} : userDefinedValue;
    }
    
    private static <T> T validateParameter(String toValidate, QueryParam annotation, Class<T> type) throws ProviderException {
        if(annotation.validation().equals(QueryParam.NO_VALIDATION) || toValidate.matches(annotation.validation())) {
            return instantiateClassFromString(type, toValidate);
        }
        else {
            throw new ProviderException("The queryparam " + toValidate + " was not in the correct format");
        }
    }
    
    private static <T> List<T> validateParameter(List<String> toValidate, QueryParam annotation, Class<T> type) throws ProviderException {
        List<T> toReturn = new ArrayList<T>();
        for(String currValue : toValidate) {
            if(!annotation.validation().equals(QueryParam.NO_VALIDATION) && !currValue.matches(annotation.validation())) {
                 throw new ProviderException("The queryparam " + currValue + " was not in the correct format");
            }
            toReturn.add(instantiateClassFromString(type, currValue));
        }
        return toReturn;
    }
    
    private static <T> T instantiateClassFromString(Class<T> type, String param) throws ProviderException {
        try {
            return type.getConstructor(String.class).newInstance(param);
        } catch(InstantiationException ex) {
            throw new ProviderException("An InstantiationException occured when creating constructing the param as a given class", ex);
        }catch (IllegalAccessException ex) {
            throw new ProviderException("An InstantiationException occured when creating constructing the param as a given class", ex);
        } catch (InvocationTargetException ex) {
            throw new ProviderException("An InstantiationException occured when creating constructing the param as a given class", ex);
        } catch (NoSuchMethodException ex) {
            throw new ProviderException("An InstantiationException occured when creating constructing the param as a given class", ex);
        }
    }
}
