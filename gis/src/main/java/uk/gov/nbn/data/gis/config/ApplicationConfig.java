package uk.gov.nbn.data.gis.config;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import uk.ac.ceh.dynamo.BoundingBox;
import uk.ac.ceh.dynamo.MapServerViewResolver;
import uk.ac.ceh.dynamo.arguments.ServiceURLArgumentResolver;
import uk.ac.ceh.dynamo.DynamoMapRequestMappingHandlerMapping;
import uk.ac.ceh.dynamo.FeatureResolver;
import uk.ac.ceh.dynamo.arguments.DynamoMapArgumentResolver;
import uk.ac.ceh.dynamo.arguments.QueryParameterResolver;
import uk.gov.nbn.data.properties.PropertiesReader;
import uk.org.nbn.nbnv.api.model.Feature;

/**
 *
 * @author Christopher Johnson
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"uk.gov.nbn.data.gis","uk.ac.ceh.dynamo.providers"})
public class ApplicationConfig extends WebMvcConfigurerAdapter {     
    @Autowired ServletContext context;
    
    @Bean
    public FreeMarkerViewResolver configureFreeMarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html;charset=UTF-8");
        return resolver;
    }
    
    @Bean
    public FreeMarkerConfigurer configureFreeMarker() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("WEB-INF/ftl/");
        return configurer;
    }
    
    @Bean
    public MapServerViewResolver configureMapServerViewResolver() throws IOException {
        return new MapServerViewResolver(new File(context.getRealPath("WEB-INF/maps")), new URL("http://localhost:9000/fcgi-bin/mapserv.exe"));
    }
    
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        try {
            TokenUserArgumentResolver resolver = new TokenUserArgumentResolver(client());
            List<QueryParameterResolver> queryParamResolvers = new ArrayList<QueryParameterResolver>();
            queryParamResolvers.add(resolver);

            argumentResolvers.add(new ServiceURLArgumentResolver(queryParamResolvers));
            argumentResolvers.add(new DynamoMapArgumentResolver());
            argumentResolvers.add(resolver);
        }
        catch(IOException io) {
            throw new RuntimeException(io);
        }
    }
    
    @Bean
    public DynamoMapRequestMappingHandlerMapping gridMap() {
        return new DynamoMapRequestMappingHandlerMapping();
    }
    
    @Bean
    public FeatureResolver featureResolver() throws IOException {
        final WebResource client = client();
        return new FeatureResolver() {

            @Override
            public BoundingBox getFeature(String featureId) throws IllegalArgumentException {
                uk.org.nbn.nbnv.api.model.BoundingBox bbox = client
                                                                .path("features")
                                                                .path(featureId)
                                                                .get(Feature.class)
                                                                .getNativeBoundingBox();
                return new BoundingBox(bbox.getEpsgCode(), 
                                       bbox.getMinX(), 
                                       bbox.getMinY(),
                                       bbox.getMaxX(),
                                       bbox.getMaxY());
            }
        };
    }
    
    @Bean
    public WebResource client() throws IOException {
        final DefaultClientConfig config = new DefaultClientConfig();
        config.getFeatures()
                .put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(config);
        return client.resource(properties().getProperty("api"));     
    }
    
    @Bean
    public Properties properties() throws IOException {
        return PropertiesReader.getEffectiveProperties("gis.properties");
    } 
}
