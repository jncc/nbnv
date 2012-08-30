package uk.gov.nbn.data.gis.processor;

import edu.umn.gis.mapscript.mapObj;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * The following class represents a qualified map service method for a real path
 * that is with its variable request parameters resolved to concrete names
 * @author Christopher Johnson
 */
public class MapServiceMethod {
    private final Method method;
    private final Map<String,String> variableNamesMap;
    private final Object instance;
    private final MapServiceMethodFactory creator;
    
    MapServiceMethod(MapServicePart part, String[] requestParts, MapServiceMethodFactory creator) {
        this.instance = part.getMapServiceInstance();
        this.method = part.getAssociatedMethod();
        this.creator = creator;
        this.variableNamesMap = part.getVariableParameterMappings(requestParts);
    }
    
    /**
     * Obtains the map object for a given request
     * @param request
     * @return The mapObj for this method for a given request
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ProviderException 
     */
    public mapObj createMapObject(HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ProviderException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        
        Object[] parameters = new Object[parameterTypes.length];
        for(int i=0; i<parameters.length; i++) {
            parameters[i] = creator.getProvidedForParameter(this, request, parameterTypes[i], Arrays.asList(parameterAnnotations[i]));
        }
        return (mapObj)method.invoke(instance, parameters);
    }
   
    /**
     * Obtains the concrete value for a given variable key
     * @param key The key to look up
     * @return The concrete value of the parameter for a given key
     */
    public String getVariableValue(String key) {
        return variableNamesMap.get(key);
    }
}
