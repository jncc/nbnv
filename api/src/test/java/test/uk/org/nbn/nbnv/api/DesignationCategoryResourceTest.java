/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uk.org.nbn.nbnv.api;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import javax.ws.rs.core.MediaType;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.org.nbn.nbnv.api.model.DesignationCategory;

/**
 * The following test is a simple example of Spring/Jersey-Client/Jetty test
 * @author Christopher Johnson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jersey-jetty-applicationContext.xml")
public class DesignationCategoryResourceTest {
    @Autowired WebResource resource;

    @Test
    public void categoryListTest() {        
        GenericType<List<DesignationCategory>> gt = new GenericType<List<DesignationCategory>>() { };
        List<DesignationCategory> dc = resource
                .path("designationCategories")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
        Assert.assertTrue(dc.size() > 0);
    }
}
