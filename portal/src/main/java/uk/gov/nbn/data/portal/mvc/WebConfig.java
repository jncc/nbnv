/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.mvc;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import uk.gov.nbn.data.portal.config.PowerlessHandlerMapping;
import uk.gov.nbn.data.portal.config.PowerlessRequestSpecificsInterceptor;
import uk.gov.nbn.data.portal.config.PowerlessSpringMVCConfig;
import uk.gov.nbn.data.portal.mvc.RequestScopedResponseInterceptor.HttpServletResponseFactoryBean;
import uk.gov.nbn.data.powerless.view.PowerlessFreeMarkerViewResolver;

/**
 *
 * @author CJOHN
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "uk.gov.nbn.data.portal")
@Import(ApplicationConfig.class)
public class WebConfig extends WebMvcConfigurerAdapter {    
    @Bean
    public FreeMarkerViewResolver configureFreeMarkerViewResolver() {
        FreeMarkerViewResolver resolver = new PowerlessFreeMarkerViewResolver();
        resolver.setCache(true);
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
    @Scope("request")
    public HttpServletResponseFactoryBean httpServletResponse() {
        return new HttpServletResponseFactoryBean();
    }
    
    @Bean
    public RequestScopedResponseInterceptor requestScopedResponseInterceptor() {
        return new RequestScopedResponseInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry configurer) {
        configurer.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(86400);
        configurer.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(86400);
        configurer.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(86400);
        configurer.addResourceHandler("/favicon.ico/**").addResourceLocations("/favicon.ico").setCachePeriod(86400);
    }
    
    @Bean
    public PowerlessRequestSpecificsInterceptor powerlessRequestSpecificsInterceptor() {
        return new PowerlessRequestSpecificsInterceptor();
    }
    
    @Bean 
    public PowerlessSpringMVCConfig powerlessSpringMVCConfig() {
        return new PowerlessSpringMVCConfig(configureFreeMarker());
    }
    
    @Bean
    public PowerlessHandlerMapping powerlessHandlerMapping() {
        PowerlessHandlerMapping toReturn = new PowerlessHandlerMapping();
        //For some reason, we need to register the interceptors to the custom
        //handler, even though spring mvc knows about them
        toReturn.setInterceptors(new Object[]{ 
            powerlessRequestSpecificsInterceptor(), 
            webContentInterceptor()
        });
        return toReturn;
    }
    
    @Bean
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        ResourceBundleMessageSource toReturn = new ResourceBundleMessageSource();
        toReturn.setBasename("validation-messages");
        return toReturn;
    }
    
    @Bean
    public WebContentInterceptor webContentInterceptor() {
        WebContentInterceptor toReturn = new WebContentInterceptor();
        toReturn.setCacheSeconds(0);
        toReturn.setUseExpiresHeader(true);
        toReturn.setUseCacheControlHeader(true);
        toReturn.setUseCacheControlNoStore(true);
        return toReturn;
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJacksonHttpMessageConverter());
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(powerlessRequestSpecificsInterceptor());
        registry.addInterceptor(requestScopedResponseInterceptor());
        registry.addInterceptor(webContentInterceptor());
    }
}
