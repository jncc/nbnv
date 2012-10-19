/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.org.nbn.nbnv.api.model.DesignationCategory;

/**
 * The following test is a simple example of Spring/Jersey-Client/Jetty test
 * @author Christopher Johnson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jersey-jetty-applicationContext.xml")
@DirtiesContext
public class DesignationCategoryResourceTest {
    @Autowired WebResource resource;
    
    public DesignationCategoryResourceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void categoryListTest() {        
        GenericType<List<DesignationCategory>> gt = new GenericType<List<DesignationCategory>>() { };
        List<DesignationCategory> dc = resource
                .path("designationCategories")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
        assertTrue(dc.size() > 0);
    }
    
    @Test
    public void embeddedDatabaseCategoryTest() {
        DesignationCategory dc = resource
                .path("designationCategories")
                .path("100")
                .accept(MediaType.APPLICATION_JSON)
                .get(DesignationCategory.class);
        assertEquals(dc.getLabel(), "Embedded Record");
    }
}
