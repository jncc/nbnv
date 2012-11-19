package uk.gov.nbn.data.powerless;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.template.*;
import java.io.File;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import uk.gov.nbn.data.powerless.json.CookiePassthrough;
import uk.gov.nbn.data.powerless.json.JSONReaderForFreeMarker;
import uk.gov.nbn.data.powerless.request.TraditionalHttpRequestParametersHashModel;

/**
 * The following servlet handles simple resources in a powerless fashion. For 
 * more complex functionality see Spring MVC.
 * @author Christopher Johnson
 */
public class PowerlessServlet extends FreemarkerServlet{
    private static final String POWERLESS_URL_PARAMETERSATION_KEY = "URLParameters";
    
    @Override public void init() throws ServletException {
        super.init();
        try {
            Configuration config = getConfiguration();
            AutowireCapableBeanFactory beanFactory = WebApplicationContextUtils
                                    .getWebApplicationContext(getServletContext())
                                    .getAutowireCapableBeanFactory();
            PowerlessHelper.setUpConfiguration(
                    config, 
                    beanFactory.getBean("properties", Properties.class), 
                    new File(getServletContext().getRealPath(PowerlessHelper.DEFAULT_FREEMARKER_TEMPLATE_LIBRARIES)), 
                    PowerlessHelper.DEFAULT_FREEMARKER_TEMPLATE_LIBRARIES);
        } catch (TemplateModelException ex) {
            throw new ServletException(ex);
        }
    }
    
    /**
     * The following method add the URL Parameters which were found by a 
     * PowerlessTemplateURLParameterisationFilter filter and will add them to
     * the global namespace under the hash POWERLESS_URL_PARAMETERSATION_KEY.
     * 
     * Also adds a Cookie forwarding json reader
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
        toReturn.put("json", new JSONReaderForFreeMarker(new CookiePassthrough(request, response)));
        return toReturn;
    }
    
    @Override
    protected HttpRequestParametersHashModel createRequestParametersHashModel(javax.servlet.http.HttpServletRequest request) {
        return new TraditionalHttpRequestParametersHashModel(request);
    }
}
