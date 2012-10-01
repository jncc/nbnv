package uk.gov.nbn.data.gis.processor;

import uk.gov.nbn.data.gis.processor.atlas.AtlasGradeProcessor;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import uk.gov.nbn.data.gis.processor.atlas.EnableAtlasGrade;

/**
 * The following class represents a qualified map service method for a real path
 * that is with its variable request parameters resolved to concrete names
 * @author Christopher Johnson
 */
public class MapServiceMethod {
    private final Map<String,String> variableNamesMap;
    private final ProviderFactory providerFactory;
    private final MapServicePart part;
    
    MapServiceMethod(MapServicePart part, String[] requestParts, ProviderFactory providerFactory) {
        this.part = part;
        this.providerFactory = providerFactory;
        this.variableNamesMap = part.getVariableParameterMappings(requestParts);
    }
   
    public boolean isAtlasGrade() {
        return part.isAtlasGrade();
    }
    
    public EnableAtlasGrade getAtlasGradeAnnotation() {
        return part.getAtlasGradeAnnotation();
    }
    
    public List<AtlasGradeProcessor> getAtlasGradeProcessors() {
        return part.getAtlasGradeProcessors();
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
        Method method = part.getAssociatedMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        
        Object[] parameters = new Object[parameterTypes.length];
        for(int i=0; i<parameters.length; i++) {
            parameters[i] = providerFactory.getProvidedForParameter(this, request, parameterTypes[i], Arrays.asList(parameterAnnotations[i]));
        }
        
        return (MapFileModel)method.invoke(part.getMapServiceInstance(), parameters);
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
