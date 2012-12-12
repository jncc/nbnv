/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.gov.nbn.data.powerless.json;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Christopher Johnson
 */
public class CookiePassthrough {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    
    public CookiePassthrough(HttpServletRequest request, HttpServletResponse response) {
       this.request = request;
       this.response = response;
    }
    
    public HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        String cookie = request.getHeader("Cookie");
        if(cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        return conn;
    }
    
    public InputStream getInputStream(HttpURLConnection conn) throws IOException, JSONReaderStatusException {
        String newCookie = conn.getHeaderField("Set-Cookie");
        if(newCookie != null) {
            response.addHeader("Set-Cookie", newCookie);
        }
        switch(conn.getResponseCode()) {
            case 200: return conn.getInputStream();
            //deliberatly fall into the json reader status exception. This will stop processing of stream
            case 401: response.sendRedirect("/User/SSO/Unauthorized?redirect=" + URLEncoder.encode(request.getRequestURL().toString()));
            default: throw new JSONReaderStatusException(conn.getResponseCode(), conn.getResponseMessage());
        }
    }
    
}
