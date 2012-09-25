package uk.gov.nbn.data.gis.processor;

import freemarker.template.TemplateException;
import java.io.IOException;
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
    private final ProviderFactory providerFactory;
    private final Object instance;
    
    MapServiceMethod(MapServicePart part, String[] requestParts, ProviderFactory providerFactory) {
        this.instance = part.getMapServiceInstance();
        this.method = part.getAssociatedMethod();
        this.providerFactory = providerFactory;
        this.variableNamesMap = part.getVariableParameterMappings(requestParts);
    }
   
    /**
     * Obtains the map object model for a given request
     * @param request
     * @return The mapObj for this method for a given request
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ProviderException 
     */
    MapFileModel createMapModel(HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ProviderException, IOException, TemplateException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        
        Object[] parameters = new Object[parameterTypes.length];
        for(int i=0; i<parameters.length; i++) {
            parameters[i] = providerFactory.getProvidedForParameter(this, request, parameterTypes[i], Arrays.asList(parameterAnnotations[i]));
        }
        
        return (MapFileModel)method.invoke(instance, parameters);
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
