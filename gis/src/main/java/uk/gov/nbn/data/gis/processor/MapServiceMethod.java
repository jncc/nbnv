/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.gov.nbn.data.gis.processor;

import uk.gov.nbn.data.gis.providers.annotations.Param;
import edu.umn.gis.mapscript.mapObj;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Christopher Johnson
 */
public class MapServiceMethod {
    private final Method method;
    private final Map<String,String> variableNamesMap;
    private final Object instance;
    private final MapServicePartFactory creator;
    
    MapServiceMethod(MapServicePart part, String[] requestParts, MapServicePartFactory creator) {
        this.instance = part.getMapServiceInstance();
        this.method = part.getAssociatedMethod();
        this.creator = creator;
        this.variableNamesMap = part.getVariableParameterMappings(requestParts);
    }
    
    public mapObj createMapObject(HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ProviderException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        
        Object[] parameters = new Object[parameterTypes.length];
        for(int i=0; i<parameters.length; i++) {
            parameters[i] = creator.getProvidedForParameter(this, request, parameterTypes[i], Arrays.asList(parameterAnnotations[i]));
        }
        return (mapObj)method.invoke(instance, parameters);
    }
   
    public String getVariableValue(String key) {
        return variableNamesMap.get(key);
    }
}
