/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uk.org.nbn.nbnv.api;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.org.nbn.nbnv.api.model.DesignationCategory;

/**
 *
 * @author Paul Gilbertson
 */
public class DesignationCategoryResourceTest extends SpringJerseyTest{

    
    @Test
    public void categoryListTest() {
        WebResource webResource = resource();
//        GenericType<List<DesignationCategory>> gt = new GenericType<List<DesignationCategory>>() { };
//        List<DesignationCategory> dc = webResource.path("/designationCategories").accept(MediaType.APPLICATION_JSON).get(gt);
        DesignationCategory dc = webResource.path("designationCategories").path("19").accept(MediaType.APPLICATION_JSON).get(DesignationCategory.class);
        //Assert.assertTrue(dc.size() > 0);
        System.out.println(dc);
    }

}
