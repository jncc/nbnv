package uk.gov.nbn.data.gis.filters;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.core.MediaType;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following HttpFilter will capture requests that may contain a token cookie
 * and pass on a request to the NBN Authentication server to obtain the current
 * requests corresponding user. The userKey for the current user will be set as
 * a Query Parameter to be used without the need for validation in subsequent
 * servlet request handlers.
 * @author Christopher Johnson
 */
public class MapServerNBNAuthenticationFilter implements Filter {
    private static final String TOKEN_ID = "userKey";
    private static final String AUTHENTICATION_ADDRESS = "user";
    
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        chain.doFilter(new SanitizedUserKeyServletRequest(httpRequest), response);
    }

    /**Wrap up a HttpServletRequest with a new key added to the Parameter Map (userKey)*/
    private static class SanitizedUserKeyServletRequest extends HttpServletRequestWrapper {
        private final String userID;
        SanitizedUserKeyServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            this.userID = Integer.toString(getUser(request).getId());
        }
        
        @Override public Map<String, String[]> getParameterMap() {
            Map<String, String[]> toReturn = new HashMap<String,String[]>(super.getParameterMap());
            toReturn.put(TOKEN_ID, new String[]{userID});
            return toReturn;
        }
        
        @Override public String getParameter(String name) {
            Map<String, String[]> parameters = getParameterMap();
            if(parameters.containsKey(name)) {
                return parameters.get(name)[0];
            }
            return null;
        }
        
        @Override public Enumeration<String> getParameterNames() {
            return Collections.enumeration(getParameterMap().keySet());
        }
        
        @Override public String[] getParameterValues(String name) {
            return getParameterMap().get(name);
        }
        
        private static User getUser(HttpServletRequest request) throws IOException {
            DefaultClientConfig config = new DefaultClientConfig();
            config.getFeatures()
                .put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client = Client.create(config);
            WebResource resource = client.resource("http://staging.testnbn.net/api/");

            User user = resource
                .path(AUTHENTICATION_ADDRESS)
                .header("Cookie", request.getHeader("Cookie"))
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);
            return user;
        }
    }
    
    @Override public void init(FilterConfig fc) throws ServletException {}
    @Override public void destroy() {}
}
