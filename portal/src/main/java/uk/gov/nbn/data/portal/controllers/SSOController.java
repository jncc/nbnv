package uk.gov.nbn.data.portal.controllers;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.gov.nbn.data.powerless.PropertiesReader;

/**
 * The following servlet will attempt a login to the NBN api and if successful
 * forward the cookie token to the browser
 * @author Christopher Johnson
 */
public class SSOController extends HttpServlet {
    private Properties properties; 
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            properties = PropertiesReader.getEffectiveProperties("powerless-global.properties");
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = properties.getProperty("api") + "/user/login?username=" + 
                request.getParameter("username") + "&password=" +
                request.getParameter("password");
        
        HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
        try {
            String header = conn.getHeaderField("Set-Cookie");
            if(header != null) { //success
                response.addHeader("Set-Cookie", header);
                response.sendRedirect("/");
            }
            else {
                request.getRequestDispatcher("/User/SSO/").forward(request, response);
            }
        }
        finally {
            conn.disconnect();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet which wraps data api login and handles redirects";
    }
}
