package uk.gov.nbn.data.gis.maps;

import java.util.HashMap;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;

/**
 * The following represents a Map service for providing Context layers 
 * @author Christopher Johnson
 */
@Component
@MapContainer("Context")
public class ContextMap { 
    @Autowired Properties properties;
    
    @MapService
    public MapFileModel getOSMapModel(@ServiceURL String mapServiceURL) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("properties", properties);
        data.put("mapServiceURL", mapServiceURL);
        return new MapFileModel("Context.map", data);
    }
}
