package uk.gov.nbn.data.gis.maps;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The following represents a Map service for providing Ordinance Survey Maps
 * @author Christopher Johnson
 */
@Controller
public class OSModernMap { 
    private Map<String, String> propertiesHash;
    private AntPathMatcher matcher;
    
    @Autowired
    public OSModernMap(Properties properties) {
        matcher = new AntPathMatcher();
        propertiesHash = new HashMap<String, String>();
        for(String key: properties.stringPropertyNames()) {
            propertiesHash.put(key, properties.getProperty(key));
        }
    }
    
    @RequestMapping("OS-Modern")
    public ModelAndView getOSMapModel(HttpServletRequest request) {
        if(!isValidRequest(request)) {
            throw new IllegalArgumentException("The ordnance survey map is restricted to use within the gateway");
        }
        return new ModelAndView("OS-Modern.map", propertiesHash);
    }
    
    public boolean isValidRequest(HttpServletRequest request) {        
        try {
            return isValidRequest(request.getHeader("referer"), request.getServerName());
        } catch (MalformedURLException ex) {
            return false;
        } 
    }
    
    public boolean isValidRequest(String referrer, String serverName) throws MalformedURLException {
        if("localhost".equalsIgnoreCase(serverName)) {
            return true; //If the server is runnint locally, allow requests
        }
        else if(referrer != null) { 
            //if the referer is present check if valid for nbn request
            //or valid bars request
            String referrerHost = new URL(referrer).getHost();
            return matcher.match("*.nbn.org.uk", referrerHost) || 
                        isValidBarsRequest(referrerHost, serverName);
        }
        else {
            return false;
        }
    }
    
    //Bars requests must be sent to bars-gis.nbn.org.uk with referers 
    //  *.ukbars.org.uk
    //  ukbars.defra.gov.uk
    private boolean isValidBarsRequest(String referrerHost, String requestPath) {
        return "bars-gis.nbn.org.uk".equalsIgnoreCase(requestPath) && (
                matcher.match("*.ukbars.org.uk", referrerHost) || 
                "ukbars.defra.gov.uk".equalsIgnoreCase(referrerHost));
    }
}
