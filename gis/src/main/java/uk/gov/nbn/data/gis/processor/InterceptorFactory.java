package uk.gov.nbn.data.gis.processor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapServiceMethod.Type;

/**
 *
 * @author Administrator
 */
@Component
public class InterceptorFactory {
    @Autowired ApplicationContext context;
    @Autowired ProviderFactory providerFactory;
    @Autowired MapServiceMethodFactory serviceFactory;
    
    private EnumMap<Type, Map<Method, Object>> responseManipulatingInterceptors;
    private EnumMap<Type, Map<Method, Object>> queryManipulatingInterceptors;
    
    @PostConstruct void init() {
        queryManipulatingInterceptors = createLookupMap();
        responseManipulatingInterceptors = createLookupMap();
        for(Object currInterceptor: context.getBeansWithAnnotation(Interceptor.class).values()) {
            for(Method currMethod : currInterceptor.getClass().getMethods()) {
                Intercepts intercepts = currMethod.getAnnotation(Intercepts.class);
                if(intercepts != null) {
                    Class<?> returnType = currMethod.getReturnType();
                    for(Type mapMethodTypeToIntercept : intercepts.value()) {
                        if(returnType.equals(Map.class)) {
                            queryManipulatingInterceptors.get(mapMethodTypeToIntercept).put(currMethod, currInterceptor);
                        }
                        else if(returnType.equals(Response.class)) {
                            responseManipulatingInterceptors.get(mapMethodTypeToIntercept).put(currMethod, currInterceptor);
                        }
                        else {
                            throw new IllegalArgumentException("The interceptor returns a type which is not "
                                    + "handled. Found " + returnType + " I can cope with " + Map.class + " "
                                    + Response.class );
                        }
                    }
                }
            }
        }
    }
    
    public Response getResponse(File mapFile, MapServiceMethod mapMethod, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ProviderException, IOException {
        for(Entry<Method, Object> queryInterceptor : responseManipulatingInterceptors.get(mapMethod.getType()).entrySet()) {
            Response response = (Response)providerFactory.provideForMethodAndExecute(
                                                            queryInterceptor.getValue(), 
                                                            queryInterceptor.getKey(), 
                                                            mapMethod, request);
            if(response!=null) {
                return response;
            }
        }
        URL mapServerURL = serviceFactory.getMapServiceURL(mapFile, getMapServerRequest(mapMethod, request));
        HttpURLConnection openConnection = (HttpURLConnection)mapServerURL.openConnection();
        return new Response(openConnection.getContentType(),openConnection.getInputStream());
    }
    
    private Map<String, String[]> getMapServerRequest(MapServiceMethod mapMethod, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ProviderException {
        Map<String, String[]> toReturn = new HashMap<String, String[]>(request.getParameterMap());
        for(Entry<Method, Object> queryInterceptor : queryManipulatingInterceptors.get(mapMethod.getType()).entrySet()) {
            Object partialQueryMap = providerFactory.provideForMethodAndExecute(
                                                            queryInterceptor.getValue(), 
                                                            queryInterceptor.getKey(), 
                                                            mapMethod, request);
            toReturn.putAll((Map<String, String[]>)partialQueryMap);
        }
        return toReturn;
    }
    
    private static EnumMap<Type, Map<Method, Object>> createLookupMap() {
        EnumMap<Type, Map<Method, Object>> toReturn = new EnumMap<Type, Map<Method, Object>>(Type.class);
        for(Type currMapMethodServiceType : Type.values()) {
            toReturn.put(currMapMethodServiceType, new HashMap<Method, Object>());
        }
        return toReturn;
    }
}
