/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.gov.nbn.data.powerless.json;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        if(conn.getResponseCode() != 200) {
            throw new JSONReaderStatusException(conn.getResponseCode(), conn.getResponseMessage());
        }
        return conn.getInputStream();
    }
}
