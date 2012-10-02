package uk.gov.nbn.data.gis.processor;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * The following class represents a qualified map service method for a real path
 * that is with its variable request parameters resolved to concrete names
 * @author Christopher Johnson
 */
public class MapServiceMethod {
    private final Map<String,String> variableNamesMap;
    private final ProviderFactory providerFactory;
    private final MapServicePart part;
    
    public enum Type {
        STANDARD(null), LEGEND("legend"), MAP("map"), ACKNOWLEDGMENT("acknowledgement");
        private String request;

        private Type(String request) {
            this.request = request;
        }

        public String getRequest() {
            return request;
        }
    }

    MapServiceMethod(MapServicePart part, String[] requestParts, ProviderFactory providerFactory) {
        this.part = part;
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
        return (MapFileModel)providerFactory.provideForMethodAndExecute(
                part.getMapServiceInstance(), 
                part.getAssociatedMethod(), 
                this, request);
    }
    
    public Method getUnderlyingMapMethod() {
        return part.getAssociatedMethod();
    }
    
    public Type getType() {
        return part.getMapServiceType();
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
