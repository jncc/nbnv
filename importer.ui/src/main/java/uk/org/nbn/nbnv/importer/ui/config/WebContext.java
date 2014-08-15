package uk.org.nbn.nbnv.importer.ui.config;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.nbn.data.properties.PropertiesReader;

import java.io.IOException;
import java.util.Properties;

/**
 * @author stephen batty
 *         Date: 15/08/14
 *         Time: 11:44
 */
@Configuration
public class WebContext {

    @Bean
    public Properties properties() throws IOException {
        return PropertiesReader.getEffectiveProperties("importer.ui.properties");
    }

    @Bean
    public CloseableHttpClient httpClient() throws IOException {
        PoolingHttpClientConnectionManager connPool = new PoolingHttpClientConnectionManager();
        connPool.setMaxTotal(Integer.parseInt(properties().getProperty("httpclient.maxConnections")));
        connPool.setDefaultMaxPerRoute(Integer.parseInt(properties().getProperty("httpclient.maxPerRoute")));

        return HttpClients.custom()
                .setConnectionManager(connPool)
                .build();
    }
    @Bean(name = "client")
    public WebResource client() throws IOException {
        final DefaultClientConfig config = new DefaultClientConfig();
        config.getFeatures()
                .put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        ApacheHttpClient4Handler apacheHandler = new ApacheHttpClient4Handler(httpClient(),
                null,
                true);
        Client client = new Client(apacheHandler, config);
        return client.resource(properties().getProperty("api"));
    }

    /*@Bean
    public TokenUserArgumentResolver tokenUserArgumentResolver() throws IOException {
        return new TokenUserArgumentResolver(client());
    } */
}
