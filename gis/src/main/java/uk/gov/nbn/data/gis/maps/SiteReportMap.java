package uk.gov.nbn.data.gis.maps;

import java.util.HashMap;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;

/**
 * The following map container will create two map services. The first being a 
 * map which contains a os background layer with a single feature highlighted
 * 
 * The second being the same as the first but with the intersected/overlapped 
 * features displayed
 * @author Christopher Johnson
 */
@Component
@MapContainer("SiteReport")
public class SiteReportMap {
    @Autowired Properties properties;
    
    @MapService("{featureID}")
    public MapFileModel getFeatureMap(
            @ServiceURL String mapServiceURL,
            @PathParam(key="featureID", validation="[0-9]*") String featureID) {        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("featureID", featureID);
        return new MapFileModel("SiteReport.map",data);
    }
    
    @MapService("{featureID}/{taxonVersionKey}")
    public MapFileModel getSiteReport(
            @ServiceURL String mapServiceURL,
            @PathParam(key="featureID", validation="[0-9]*") String featureID,
            @PathParam(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") String taxonKey) {
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("mapServiceURL", mapServiceURL);
        data.put("properties", properties);
        data.put("featureID", featureID);
        data.put("taxonVersionKey", taxonKey);
        return new MapFileModel("SiteReport.map",data);
    }
}
