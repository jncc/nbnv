package uk.gov.nbn.data.gis.processor;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * The following class represents a qualified map service method for a real path
 * that is with its variable request parameters resolved to concrete names
 * @author Christopher Johnson
 */
public class MapServiceMethod {
    private final ProviderFactory providerFactory;
    private final MapServicePart part;
    
    public enum Type {
        STANDARD(null, null), LEGEND("legend", GridMap.class), MAP("map", GridMap.class), 
        ACKNOWLEDGMENT("acknowledgement", Acknowledgement.class), RESOLUTIONS("resolutions", GridMap.class);
        private Class<? extends Annotation> annotationFlag;
        private String request;

        private Type(String request, Class<? extends Annotation> annotationFlag) {
            this.request = request;
            this.annotationFlag = annotationFlag;
        }

        public String getRequest() {
            return request;
        }
        
        public static EnumSet<Type> getTypesValidForMethod(Method mapServiceMethod) {
            EnumSet<Type> toReturn = EnumSet.complementOf(EnumSet.of(Type.STANDARD));
            for ( Iterator<Type> typeI = toReturn.iterator(); typeI.hasNext(); ) {
                if(mapServiceMethod.getAnnotation(typeI.next().annotationFlag) == null) {
                    typeI.remove();
                }
            }
            return toReturn;
        }       
    }

    
    public class Call {
        private final Map<String,String> variableNamesMap;
        private final HttpServletRequest request;
        
        private Call(HttpServletRequest request) {
            this.request = request;
            this.variableNamesMap = part.getVariableParameterMappings(
                    request.getPathInfo().substring(1).split("/"));
        }
        
        /**
         * Returns the original request object which was used to map this map
         * Service call
         * @return The original httpservletrequest
         */
        public HttpServletRequest getRequest() {
            return request;
        }
        
        /**
        * Obtains the concrete value for a given variable key
        * @param key The key to look up
        * @return The concrete value of the parameter for a given key
        */
        public String getVariableValue(String key) {
            return variableNamesMap.get(key);
        }
        
        /**
         * Obtains the map object model for a given request
         *
         * @param request
         * @return The mapObj for this method for a given request
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         * @throws ProviderException
         */
        public MapFileModel createMapModel() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ProviderException, IOException, TemplateException {
            return (MapFileModel) providerFactory.provideForMethodAndExecute(
                    part.getMapServiceInstance(),
                    part.getAssociatedMethod(),
                    this);
        }
    
        public MapServiceMethod getMethod() {
            return MapServiceMethod.this;
        }
    }
    
    MapServiceMethod(MapServicePart part,  ProviderFactory providerFactory) {
        this.part = part;
        this.providerFactory = providerFactory;
    }

    public Call call(HttpServletRequest request) {
        return new Call(request);
    }
    
    public Method getUnderlyingMapMethod() {
        return part.getAssociatedMethod();
    }
    
    public Object getUnderlyingInstance() {
        return part.getMapServiceInstance();
    }
    
    public Type getType() {
        return part.getMapServiceType();
    }
    
    public String getPath() {
        return part.getPath();
    }
    
    @Override public String toString() {
        return getPath();
    }
}
