package uk.gov.nbn.data.gis.maps;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The following represents a Map service for providing Ordinance Survey Maps
 * @author Christopher Johnson
 */
@Controller
public class OSModernMap { 
    private Map<String, String> propertiesHash;
    
    @Autowired
    public OSModernMap(Properties properties) {
        propertiesHash = new HashMap<String, String>();
        for(String key: properties.stringPropertyNames()) {
            propertiesHash.put(key, properties.getProperty(key));
        }
    }
    
    @RequestMapping("OS-Modern")
    public ModelAndView getOSMapModel(HttpServletRequest request) { 
        String referrer = request.getHeader("referer");
        if(referrer ==null || !referrer.startsWith(propertiesHash.get("ordnanceSurveyAllowedReferrerPrefix"))) {
            throw new IllegalArgumentException("The ordnance survey map is restricted to use within the gateway");
        }
        return new ModelAndView("OS-Modern.map", propertiesHash);
    }
}
