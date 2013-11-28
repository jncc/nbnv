package uk.gov.nbn.data.gis.maps;

import java.util.HashMap;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ceh.dynamo.arguments.annotations.ServiceURL;

/**
 * The following represents a Map service for providing Context layers 
 * @author Christopher Johnson
 */
@Controller
public class ContextMap { 
    @Autowired Properties properties;
    
    @RequestMapping("Context")
    public ModelAndView getOSMapModel(@ServiceURL String mapServiceURL) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("properties", properties);
        data.put("mapServiceURL", mapServiceURL);
        return new ModelAndView("Context.map", data);
    }
}
