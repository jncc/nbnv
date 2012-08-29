/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.gov.nbn.data.gis;

import edu.umn.gis.mapscript.mapObj;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    
    MapServiceMethod(MapServicePart part, String[] requestParts) {
        this.instance = part.getMapServiceInstance();
        this.method = part.getAssociatedMethod();
        this.variableNamesMap = part.getVariableParameterMappings(requestParts);
    }
    
    public mapObj createMapObject(HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        
        Object[] parameters = new Object[parameterTypes.length];
        for(int i=0; i<parameters.length; i++) {
            parameters[i] = getParameter(request, parameterTypes[i], parameterAnnotations[i]);
        }
        return (mapObj)method.invoke(instance, parameters);
    }
    
    private Object getParameter(HttpServletRequest request, Class<?> toReturn, Annotation[] paramAnnotations) {
        if(toReturn.equals(HttpServletRequest.class)) {
            return request;
        }
        else {
            for(Annotation currAnnotation : paramAnnotations) {
                if(currAnnotation instanceof Param) {
                    return variableNamesMap.get(((Param)currAnnotation).value());
                }
            }
        }
        return null; //can't find a matching parameter
    }    
}
