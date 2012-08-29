package uk.gov.nbn.data.gis;

import edu.umn.gis.mapscript.OWSRequest;
import edu.umn.gis.mapscript.mapscript;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The following Servlet wraps up MapServer as a Servlet ready for requests to be
 * made
 * @author Christopher Johnson
 */
public class MapServerServlet extends HttpServlet {
    
    private MapServicePartFactory serviceFactory;
    
    @Override public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);
            serviceFactory = new MapServicePartFactory("uk.gov.nbn.data.gis.maps");
        } catch (InstantiationException ex) {
            throw new ServletException("Could not instanciate one of the map service classes", ex);
        } catch (IllegalAccessException ex) {
            throw new ServletException("Could not instanciate one of the map service classes", ex);
        }
    }
    
    @Override protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        try {
            MapServiceMethod mapMethod = serviceFactory.getMatchingPart(request.getPathInfo());
            
            ServletOutputStream out = response.getOutputStream();
            try {
                mapscript.msIO_installStdoutToBuffer(); //buffer the bytes of the map script

                int owsResult = mapMethod.createMapObject(request).OWSDispatch( createMapRequest(request) );
                if( owsResult != 0 ) {
                    throw new ServletException("OWSDispatch failed. (expect 0): " + owsResult);
                }

                response.setContentType(mapscript.msIO_stripStdoutBufferContentType()); //pass the content type

                out.write(mapscript.msIO_getStdoutBufferBytes()); //output the bytes to the end user
            }
            catch(Throwable mapEx) {
                out.write(("An exception occurred" + mapEx.getClass().getName()).getBytes());
                out.write(mapEx.getMessage().getBytes());
                //throw new ServletException("An error occured whilst generating the map server request", mapEx);
            }
            finally {
                out.close();
            }
        }
        catch(MapServiceUndefinedException msue) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Could not find: " + Arrays.toString(request.getPathInfo().substring(1).split("/")));
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
