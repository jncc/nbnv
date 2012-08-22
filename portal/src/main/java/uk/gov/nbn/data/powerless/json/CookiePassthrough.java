/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.gov.nbn.data.powerless.json;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Christopher Johnson
 */
public class CookiePassthrough {
    private final HttpServletRequest request;
    
    public CookiePassthrough(HttpServletRequest request) {
       this.request = request;
    }
    
    public HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        String cookie = request.getHeader("Cookie");
        if(cookie != null)
            conn.setRequestProperty("Cookie", cookie);
        return conn;
    }
}
