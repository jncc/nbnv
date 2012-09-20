package uk.gov.nbn.data.powerless;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import uk.gov.nbn.data.powerless.json.JSONReaderStatusException;

/**
 * The following Filter enables error pages to be configured in the web.xml
 * Based on error codes received from a call to the api
 * @author Christopher Johnson
 */
public class PowerlessJSONReaderStatusExceptionFilter implements Filter {
    @Override public void doFilter(ServletRequest request, ServletResponse resIn, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse)resIn;
        try {
            chain.doFilter(request, response);
        }
        catch(ServletException se) {
            Throwable servletExceptionCause = se.getCause();
            if(servletExceptionCause instanceof JSONReaderStatusException && servletExceptionCause != null) {
                JSONReaderStatusException remoteEx = (JSONReaderStatusException)servletExceptionCause;
                response.sendError(remoteEx.getStatusCode(), remoteEx.getMessage());
                return;
            }
            throw se; //Re throw the servlet exception which is not a template exception
        }
    }
    
    @Override public void init(FilterConfig filterConfig) {};
    @Override public void destroy() {}   
}
