/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.*;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.gov.nbn.data.powerless.json.JSONReaderForFreeMarker;

/**
 *
 * @author Administrator
 */
public class PowerlessServlet extends FreemarkerServlet{
    private static final String FREEMARKER_GLOBAL_VARIABLES = "powerless-global.properties";
    private static final String POWERLESS_URL_PARAMETERSATION_KEY = "URLParameters";
    
    @Override public void init() throws ServletException {
        super.init();
        try {
            Configuration config = getConfiguration();
            for(Map.Entry currEntry : PropertiesReader.getEffectiveProperties(FREEMARKER_GLOBAL_VARIABLES).entrySet()){
                config.setSharedVariable((String)currEntry.getKey(), currEntry.getValue());
            }
            config.setSharedVariable("markdown", new MarkDownDirectiveModel());
            config.setSharedVariable("json", new JSONReaderForFreeMarker());
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (TemplateModelException ex) {
            throw new ServletException(ex);
        } catch (IOException io) {
            throw new ServletException(io);
        }
    }
    
    /**
     * The following method add the URL Parameters which were found by a 
     * PowerlessTemplateURLParameterisationFilter filter and will add them to
     * the global namespace under the hash POWERLESS_URL_PARAMETERSATION_KEY
     * @param wrapper
     * @param servletContext
     * @param request
     * @param response
     * @return A templateModel as created by the FreeMarkerServlet with added 
     * parameters from the URL in the key POWERLESS_URL_PARAMETERSATION_KEY
     * @throws TemplateModelException 
     */
    @Override protected TemplateModel createModel(ObjectWrapper wrapper,
                                        ServletContext servletContext,
                                        final HttpServletRequest request,
                                        final HttpServletResponse response) throws TemplateModelException {
        SimpleHash toReturn = (SimpleHash)super.createModel(wrapper, servletContext, request, response);
        toReturn.put(POWERLESS_URL_PARAMETERSATION_KEY, request.getAttribute(PowerlessTemplateURLParameterisationFilter.POWERLESS_URL_PARAMETERS_ATTRIBUTE));
        return toReturn;
    }
}
