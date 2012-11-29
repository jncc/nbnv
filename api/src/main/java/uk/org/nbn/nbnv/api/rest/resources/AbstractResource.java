package uk.org.nbn.nbnv.api.rest.resources;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The following object gets portal_url links for objects
 * @author cjohn
 */
public abstract class AbstractResource {
    @Autowired Properties properties;
    
    public String getPortalUrl() {
        return properties.getProperty("portal_url");
    }
}
