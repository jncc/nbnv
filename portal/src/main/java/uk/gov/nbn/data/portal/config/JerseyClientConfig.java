package uk.gov.nbn.data.portal.config;

import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * The following is a Feature POJO Enabled ClientConfig which has been designed
 * for testing. Ideally this would have been configured purely in spring. But
 * that is not known how to be done.
 * @author Christopher Johnson
 */
public class JerseyClientConfig extends DefaultClientConfig {
    public JerseyClientConfig() {
        getFeatures()
            .put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    }
}
