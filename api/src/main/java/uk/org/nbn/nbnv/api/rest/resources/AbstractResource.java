package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * The following object gets portal_url links for objects
 * @author cjohn
 */
public abstract class AbstractResource {
    @Autowired Properties properties;
    
    public String getPortalUrl() {
        return properties.getProperty("portal_url");
    }
    
    protected boolean listHasAtLeastOneText(List<String> input) {       
        for (String item : input) {
            if (StringUtils.hasText(item)) {
                return true;
            }
        }
        
        return false;
    }  
}
