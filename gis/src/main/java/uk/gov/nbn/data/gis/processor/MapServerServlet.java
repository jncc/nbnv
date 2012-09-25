package uk.gov.nbn.data.gis.processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The following Servlet wraps up MapServer as a Servlet ready for requests to be
 * made
 * @author Christopher Johnson
 */
public class MapServerServlet extends HttpServlet {
    
    MapServiceMethodFactory serviceFactory;
    MapFileGenerator mapFileGenerator;
    
    @Override public void init(ServletConfig config) throws ServletException {
        super.init(config);        
        ServletContext servletContext = config.getServletContext();
        AutowireCapableBeanFactory beanFactory = WebApplicationContextUtils
                                    .getWebApplicationContext(servletContext)
                                    .getAutowireCapableBeanFactory();
        serviceFactory = beanFactory.getBean(MapServiceMethodFactory.class);
        mapFileGenerator = beanFactory.getBean(MapFileGenerator.class);
        try {
            mapFileGenerator.setMapTemplateDirectory(new File(config.getServletContext().getRealPath("WEB-INF\\maps")));
        } catch(IOException io) {
            throw new ServletException(io);
        }
    }
    
    @Override protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        try {
            MapServiceMethod mapMethod = serviceFactory.getMatchingPart(request.getPathInfo());
            
            ServletOutputStream out = response.getOutputStream();
            try {
                File toSubmitToMapServer = mapFileGenerator.getMapFile(request, mapMethod);
                try {
                    URL mapServerURL = serviceFactory.getMapServiceURL(toSubmitToMapServer, request.getQueryString());
                    HttpURLConnection openConnection = (HttpURLConnection)mapServerURL.openConnection();
                    response.setContentType(openConnection.getContentType());    
                    InputStream in = openConnection.getInputStream();
                    IOUtils.copy(in, out);
                    in.close();
                }
                finally {
                    toSubmitToMapServer.delete();
                }
            }
            catch(InvocationTargetException ite) {
                handleMapServiceException(ite.getTargetException(), response);
            }
            catch(Throwable mapEx) {
                throw new ServletException(mapEx);
            }
            finally {
                out.close();
            }
        }
        catch(MapServiceUndefinedException msue) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Could not find: " + Arrays.toString(request.getPathInfo().substring(1).split("/")));
        }
    }
    
    /*Handle execptions which were thrown during the construction of map services*/
    private static void handleMapServiceException(Throwable e, HttpServletResponse response) throws IOException {
        if(e instanceof IllegalArgumentException) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getCause().getMessage());
        }
    }
}
