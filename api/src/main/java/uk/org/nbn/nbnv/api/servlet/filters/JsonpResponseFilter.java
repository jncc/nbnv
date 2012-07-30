package uk.org.nbn.nbnv.api.servlet.filters;


import java.io.IOException;
import javax.servlet.*;

/**
 * The following class will wrap up any response (hopefully a json one) which 
 * contains a callback parameter and present it in a JSONP form.
 * 
 * Implemented as a servlet filter. It can be registered into a web.xml by:
 * <code>
 *     <filter>
 *          <filter-name>JsonPWrapperFilter</filter-name>
 *          <filter-class>uk.org.nbn.nbnv.api.rest.filters.JsonpResponseFilter</filter-class>
 *      </filter>
 *
 *      <filter-mapping>
 *          <filter-name>JsonPWrapperFilter</filter-name>
 *          <url-pattern>/*</url-pattern>
 *      </filter-mapping>
 * </code>
 * 
 * 
 * TODO: Only peform wrapping on json content
 * @author Christopher Johnson
 */
public class JsonpResponseFilter implements Filter {
    private static final String DEFAULT_JSONP_CALLBACK_PARAMETER = "callback";
    private static final String JSONP_CALLBACK_INIT_PARAMETER_NAME = "jsonp.callback.parameter";
    private String callbackParameterName;
    
    
    @Override public void init(FilterConfig config) {
        callbackParameterName = config.getInitParameter(JSONP_CALLBACK_INIT_PARAMETER_NAME);
        if(callbackParameterName==null || callbackParameterName.isEmpty()) {
            callbackParameterName = DEFAULT_JSONP_CALLBACK_PARAMETER;
        }
    }

    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String callbackMethod = request.getParameter(callbackParameterName);
        
        if(isJSONPRequest(callbackMethod)) {
            ServletOutputStream out = response.getOutputStream();

            out.print(callbackMethod);
            out.print("(");
            chain.doFilter(request, response);
            out.print(");");
            response.setContentType("text/javascript");
        } 
        else {
            chain.doFilter(request, response);
        }
    }

    private boolean isJSONPRequest(String callbackMethod) {
        return (callbackMethod != null && !callbackMethod.isEmpty());
    }

    @Override public void destroy() {}
}