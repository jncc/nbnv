package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.junit.Before;
import org.springframework.util.ReflectionUtils;
import uk.ac.ceh.dynamo.GridMap;
import uk.ac.ceh.dynamo.GridMapImageBuilder;

/**
 *
 * @author Christopher Johnson
 */
public class AbstractGridMapIT {
    
    protected WebResource gis;
    
    @Before
    public void createResource() {
        gis = Client.create().resource(System.getProperty("test.server", "https://dev-gis.nbn.org.uk"));
    }
    
    public GridMapImageBuilder gridMap(WebResource resource, GridMap gridMap) {
        return new GridMapImageBuilder(resource, gridMap);
    }
    
    public GridMap getGridMap(Class<?> clazz, String method) {
        return ReflectionUtils.findMethod(clazz, method, (Class<?>[]) null)
                              .getAnnotation(GridMap.class);
    }
}
