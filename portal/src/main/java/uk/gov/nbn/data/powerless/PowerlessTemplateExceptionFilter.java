package uk.gov.nbn.data.powerless;

import freemarker.template.TemplateException;
import java.io.IOException;
import javax.servlet.*;

/**
 * The following Filter enables error pages to be configured in the web.xml
 * The cause exception will be available to the template which is to be rendered
 * @author Christopher Johnson
 */
public class PowerlessTemplateExceptionFilter implements Filter {
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        }
        catch(ServletException se) {
            Throwable servletExceptionCause = se.getCause();
            if(servletExceptionCause instanceof TemplateException && servletExceptionCause != null) {
                Throwable templateExceptionCause = servletExceptionCause.getCause();
                if(templateExceptionCause != null) {
                    throw new ServletException(templateExceptionCause);
                }
            }
            throw se; //Re throw the servlet exception which is not a template exception
        }
    }
    
    @Override public void init(FilterConfig filterConfig) {};
    @Override public void destroy() {}   
}
