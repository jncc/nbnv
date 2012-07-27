/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import java.util.List;
import javax.ws.rs.core.MediaType;
import junit.framework.Assert;
import uk.org.nbn.nbnv.api.model.DesignationCategory;

/**
 *
 * @author Paul Gilbertson
 */
public class DesignationCategoryResourceTest extends JerseyTest {
    private static final ClientConfig cc; 
    static 
    { 
        cc = new DefaultClientConfig(); 
        cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, Boolean.TRUE);         
    }

    public DesignationCategoryResourceTest() throws Exception {
        super(new WebAppDescriptor.Builder("uk.org.nbn.nbnv.api", "org.codehaus.jackson.jaxrs", "org.nbn.nbnv.api.dao").clientConfig(cc).build()); 
    }
    
    //@Test
    public void categoryListTest() {
        WebResource webResource = resource();
        GenericType<List<DesignationCategory>> gt = new GenericType<List<DesignationCategory>>() { };
        List<DesignationCategory> dc = webResource.path("designationCategories").accept(MediaType.APPLICATION_JSON).get(gt);
        Assert.assertTrue(dc.size() > 0);
    }

    // TODO: Learn to use mocking to create a self contained test
    //@Test
    public void categoryTest() {
        WebResource webResource = resource();
        DesignationCategory dc = webResource.path("designationCategories").path("20").accept(MediaType.APPLICATION_JSON).get(DesignationCategory.class);
        Assert.assertEquals("Nat Legislation", dc.getLabel());
        //Assert.assertTrue(dc.size() > 0);
    }

}
