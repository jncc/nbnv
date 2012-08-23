/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.gissecurity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Christopher 
 */
public class GISSecurityProxy extends HttpServlet {
    private static final String TOKEN_ID = "userKey";
    private static final String PROXY = "nbngis-a.nerc-lancaster.ac.uk";
    private static final String AUTHENTICATION_ADDRESS = "http://nbnstaging.nerc-lancaster.ac.uk/api/user";
    private static final int PROXY_PORT = 9999;

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpHost proxy = new HttpHost(PROXY, PROXY_PORT);

            HttpGet httpGet = new HttpGet(getProxyResource(request));
            HttpResponse proxyResponse = httpclient.execute(proxy, httpGet);
            HttpEntity entity = proxyResponse.getEntity();

            for(Header currHeader : proxyResponse.getAllHeaders()) {
                response.addHeader(currHeader.getName(), currHeader.getValue());
            }
            response.setStatus(proxyResponse.getStatusLine().getStatusCode());
            ServletOutputStream responseStream = response.getOutputStream();
            try {
                IOUtils.copy(entity.getContent(), responseStream);
            }
            finally {
                responseStream.close();
            }
            EntityUtils.consume(entity);
        } catch (JSONException ex) {
            throw new ServletException("Unable to obtain the user details for the current request", ex);
        }
    }
    
    private static String getProxyResource(HttpServletRequest request) throws IOException, JSONException {
        /**Replace any occurrence of the old token id with the new one*/
        Map<String, String[]> parameterMap = new HashMap<String,String[]>(request.getParameterMap());
        parameterMap.put(TOKEN_ID, new String[]{Integer.toString(getUserID(request))});
        
        StringBuilder toReturn = new StringBuilder(request.getPathInfo()).append("?");
        for(Map.Entry<String,String[]> currQueryParameter : parameterMap.entrySet()) {
            for(String currVal : currQueryParameter.getValue()) {
                toReturn.append(currQueryParameter.getKey());
                toReturn.append("=");
                toReturn.append(currVal);
                toReturn.append("&");
            }
        }
        return toReturn.substring(0, toReturn.length()-1); //remove final ampersand
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
    
    @Override
    public String getServletInfo() {
        return "A simple proxy which adds the the userid to the query string for the current request";
    }
}
