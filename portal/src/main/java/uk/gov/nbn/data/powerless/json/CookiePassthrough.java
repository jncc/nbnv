/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.gov.nbn.data.powerless.json;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Christopher Johnson
 */
public class CookiePassthrough {
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    
    public CookiePassthrough(HttpServletRequest request, HttpServletResponse response) {
       this.request = request;
       this.response = response;
    }
    
    public HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestProperty("Cookie", request.getHeader("Cookie"));
        response.addHeader("Set-Cookie", conn.getHeaderField("Set-Cookie"));
        return conn;
    }
}
