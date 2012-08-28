/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.gov.nbn.data.gis;

import edu.umn.gis.mapscript.OWSRequest;
import edu.umn.gis.mapscript.mapObj;
import edu.umn.gis.mapscript.mapscript;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException, TemplateException {
        OWSRequest owsRequest = createMapRequest(request);
        if(method.getReturnType().equals(mapObj.class)) { //process mapscript request
            processMapObj((mapObj)executeMapService(request),owsRequest,response);
        }
        else if(method.getReturnType().equals(String.class)) { //process real map
            File executeTemplateToTempFile = MapServerFreeMarkerProcessor.executeTemplateToTempFile((String)executeMapService(request));
            try {
                processMapObj(new mapObj(executeTemplateToTempFile.getAbsolutePath()),owsRequest,response);
            }
            finally {
                executeTemplateToTempFile.delete();
            }
        }
        else {
            throw new IllegalArgumentException("The map service method does not return a mapObj");
        }
    }
    
    private Object executeMapService(HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        
        Object[] parameters = new Object[parameterTypes.length];
        for(int i=0; i<parameters.length; i++) {
            parameters[i] = getParameter(request, parameterTypes[i], parameterAnnotations[i]);
        }
        return method.invoke(instance, parameters);
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
    
    private static void processMapObj(mapObj mapScriptObject, OWSRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        try {
            mapscript.msIO_installStdoutToBuffer(); //buffer the bytes of the map script
            
            int owsResult = mapScriptObject.OWSDispatch( request );
            if( owsResult != 0 ) {
                throw new ServletException("OWSDispatch failed. (expect 0): " + owsResult);
            }

            response.setContentType(mapscript.msIO_stripStdoutBufferContentType()); //pass the content type

            out.write(mapscript.msIO_getStdoutBufferBytes()); //output the bytes to the end user
        }
        finally {
            out.close();
        }
    }
    
    private static OWSRequest createMapRequest(HttpServletRequest request) {
        OWSRequest toReturn = new OWSRequest();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for(Map.Entry<String, String[]> currParam : parameterMap.entrySet()) {
            for(String currValue : currParam.getValue()) {
                toReturn.setParameter(currParam.getKey(), currValue);
            }
        }
        return toReturn;
    }
}
