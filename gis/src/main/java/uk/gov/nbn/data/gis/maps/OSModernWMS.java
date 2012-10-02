package uk.gov.nbn.data.gis.maps;

import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;

/**
 * The following represents a Map service for providing Ordinance Survey Maps
 * @author Christopher Johnson
 */
@Component
@MapContainer("OS-Modern")
public class OSModernWMS { 
    @Autowired Properties properties;
    
    @MapService
    public MapFileModel getOSMapModel(HttpServletRequest request) { 
        String referrer = request.getHeader("referer");
        if(referrer ==null || !referrer.startsWith(properties.getProperty("ordnanceSurveyAllowedReferrerPrefix"))) {
            throw new IllegalArgumentException("The ordnance survey map is restricted to use within the gateway");
        }
        return new MapFileModel("OS-Modern.map", properties);
    }
}
