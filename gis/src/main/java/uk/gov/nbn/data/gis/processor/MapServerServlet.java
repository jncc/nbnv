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
    
    MapServerRequestProcessor processor;
    MapFileGenerator mapFileGenerator;
    
    @Override public void init(ServletConfig config) throws ServletException {
        super.init(config);        
        ServletContext servletContext = config.getServletContext();
        AutowireCapableBeanFactory beanFactory = WebApplicationContextUtils
                                    .getWebApplicationContext(servletContext)
                                    .getAutowireCapableBeanFactory();
        processor = beanFactory.getBean(MapServerRequestProcessor.class);
        mapFileGenerator = beanFactory.getBean(MapFileGenerator.class);
        try {
            mapFileGenerator.setMapTemplateDirectory(new File(config.getServletContext().getRealPath("WEB-INF\\maps")));
        } catch(IOException io) {
            throw new ServletException(io);
        }
    }
    
    @Override protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        processor.processRequest(request, response);
    }
    
    @Override protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        processor.processRequest(request, response);
    }
}
