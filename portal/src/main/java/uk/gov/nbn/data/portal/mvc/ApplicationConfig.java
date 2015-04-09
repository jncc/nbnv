/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.mvc;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import uk.gov.nbn.data.portal.jersey.JerseyCookieClientFilter;
import uk.gov.nbn.data.portal.jersey.JerseyCookieClientFilter.JerseyCookieClientHandler;
import uk.gov.nbn.data.properties.PropertiesReader;

/**
 *
 * @author CJOHN
 */
@Configuration
public class ApplicationConfig {
    @Value("${api}") String apiAddress;
    
    @Bean
    @Scope(value = "request", proxyMode= ScopedProxyMode.TARGET_CLASS)
    public JerseyCookieClientHandler cookieFilter() {
        return new JerseyCookieClientHandler();
    }
    
    @Bean
    public WebResource client() {
        final DefaultClientConfig config = new DefaultClientConfig();
        config.getFeatures()
                .put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(config);
        client.setChunkedEncodingSize(1024);
        client.addFilter(new JerseyCookieClientFilter(cookieFilter()));
        return client.resource(apiAddress);     
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException {
        PropertySourcesPlaceholderConfigurer toReturn = new PropertySourcesPlaceholderConfigurer();
        toReturn.setProperties(PropertiesReader.getEffectiveProperties("portal.properties"));
        return toReturn;
    }
}
