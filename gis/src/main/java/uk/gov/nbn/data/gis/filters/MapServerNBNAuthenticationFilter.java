package uk.gov.nbn.data.gis.filters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Christopher Johnson
 */
public class MapServerNBNAuthenticationFilter implements Filter {
    private static final String TOKEN_ID = "userKey";
    private static final String AUTHENTICATION_ADDRESS = "http://staging.testnbn.net/api/user";
    
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        try {
            chain.doFilter(new SanitizedUserKeyServletRequest(httpRequest), response);
        }
        catch(JSONException jsonex) {
            throw new ServletException("An exception occured when trying to read the authentication user", jsonex);
        }
    }

    private static class SanitizedUserKeyServletRequest extends HttpServletRequestWrapper {
        private final String userID;
        SanitizedUserKeyServletRequest(HttpServletRequest request) throws IOException, JSONException {
            super(request);
            this.userID = Integer.toString(getUserID(request));
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
        
        private static int getUserID(HttpServletRequest request) throws IOException, JSONException {
            HttpURLConnection conn = (HttpURLConnection)new URL(AUTHENTICATION_ADDRESS).openConnection();
            try {
                String cookie = request.getHeader("Cookie");
                if(cookie != null) {
                    conn.setRequestProperty("Cookie", cookie);
                }

                Reader inputStreamReader = new InputStreamReader(conn.getInputStream());
                try {
                    JSONObject user = new JSONObject(new JSONTokener(inputStreamReader));
                    return user.getInt("id");
                }
                finally {
                    inputStreamReader.close();
                }
            }
            finally {
                conn.disconnect();
            }
        }
    }
    
    @Override public void init(FilterConfig fc) throws ServletException {}
    @Override public void destroy() {}
}
