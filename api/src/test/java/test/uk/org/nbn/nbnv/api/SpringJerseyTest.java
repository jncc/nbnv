package test.uk.org.nbn.nbnv.api;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public abstract class SpringJerseyTest extends JerseyTest {
        private static final ClientConfig cc; 
    static 
    { 
        cc = new DefaultClientConfig(); 
        cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, Boolean.TRUE);         
    }

    public SpringJerseyTest(){
        super(new WebAppDescriptor.Builder("uk.org.nbn.nbnv.api.rest.resources", "org.codehaus.jackson.jaxrs", "org.nbn.nbnv.api.dao")
        .contextPath("classpath:applicationContextTest.xml")
        .servletClass(com.sun.jersey.spi.spring.container.servlet.SpringServlet.class)
        .contextListenerClass(org.springframework.web.context.ContextLoaderListener.class)
        .contextParam("contextConfigLocation", "classpath:applicationContextTest.xml")
        .clientConfig(cc)
        .build());

    }
    
}
