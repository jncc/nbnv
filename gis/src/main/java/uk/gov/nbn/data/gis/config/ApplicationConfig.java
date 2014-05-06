package uk.gov.nbn.data.gis.config;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import uk.ac.ceh.dynamo.BoundingBox;
import uk.ac.ceh.dynamo.MapServerViewResolver;
import uk.ac.ceh.dynamo.FeatureResolver;
import uk.ac.ceh.dynamo.GridMapRequestMappingHandlerMapping;
import uk.ac.ceh.dynamo.arguments.GridMapArgumentResolver;
import uk.ac.ceh.dynamo.arguments.ServiceURLArgumentResolver;
import uk.ac.ceh.dynamo.bread.BreadSliceCountClimateMeter;
import uk.ac.ceh.dynamo.bread.ShapefileBakery;
import uk.ac.ceh.dynamo.bread.ShapefileGenerator;
import uk.ac.ceh.dynamo.bread.UpdatableClimateMeter;
import uk.gov.nbn.data.properties.PropertiesReader;
import uk.org.nbn.nbnv.api.model.Feature;

/**
 *
 * @author Christopher Johnson
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"uk.gov.nbn.data.gis"})
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
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJacksonJsonpHttpMessageConverter());
        converters.add(new MappingJacksonHttpMessageConverter());
    }
    
    @Bean
    public MapServerViewResolver configureMapServerViewResolver() throws IOException {
        return new MapServerViewResolver(new File(context.getRealPath("WEB-INF/maps")), new URL(properties().getProperty("mapserver")));
    }
    
    @Bean
    public GridMapRequestMappingHandlerMapping gridMap() {
        return new GridMapRequestMappingHandlerMapping();
    }
    
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        try {
            TokenUserArgumentResolver tokenUserArgumentResolver = new TokenUserArgumentResolver(client());
            argumentResolvers.add(tokenUserArgumentResolver);
            argumentResolvers.add(new GridMapArgumentResolver());
            argumentResolvers.add(new ServiceURLArgumentResolver(tokenUserArgumentResolver));
        }
        catch(IOException io) {
            throw new RuntimeException(io);
        }
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
    
    
    @Bean ShapefileGenerator shapefileGenerator() throws IOException {
        Properties properties = properties();
        return new ShapefileGenerator(  properties.getProperty("ogr2ogr.location"),
                                        properties.getProperty("spatialConnection"),
                                        Integer.parseInt(properties.getProperty("ogr2ogr.limit")));
    }
    
    @Bean
    public ShapefileBakery taxonLayerBaker() throws IOException {
        File cache = new File(properties().getProperty("bread.taxon.cacheDir"));
        long staleTime = Long.parseLong(properties().getProperty("bread.taxon.staleTime"));
        long rottenTime = Long.parseLong(properties().getProperty("bread.taxon.rottenTime"));
        int maxBreadSliceCount = Integer.parseInt(properties().getProperty("bread.taxon.maxSliceCount"));
        
        return new ShapefileBakery(cache, new BreadSliceCountClimateMeter(maxBreadSliceCount), shapefileGenerator(), staleTime, rottenTime );
    }
    
    @Bean 
    public ShapefileBakery contextLayerBaker() throws IOException {
        File cache = new File(properties().getProperty("bread.context.cacheDir"));
        long staleTime = Long.parseLong(properties().getProperty("bread.context.staleTime"));
        long rottenTime = Long.parseLong(properties().getProperty("bread.context.rottenTime"));
        
        return new ShapefileBakery(cache, new UpdatableClimateMeter(1), shapefileGenerator(), staleTime, rottenTime );
    }
}
