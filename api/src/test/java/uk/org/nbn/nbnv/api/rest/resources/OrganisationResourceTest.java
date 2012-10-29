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
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author Matt Debont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jersey-jetty-applicationContext.xml")
@DirtiesContext
public class OrganisationResourceTest {
    @Autowired WebResource resource;
    
    public OrganisationResourceTest() {
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

    /**
     * Test of get method, of class OrganisationResource.
     */
    @Test
    public void testGet() {
        GenericType<List<Organisation>> gt = new GenericType<List<Organisation>>() { };
        List<Organisation> dc = resource
                .path("organisations")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
        
        assertEquals(dc.size(), 2);
        
        assertEquals(dc.get(0).getId(), 1);
        assertEquals(dc.get(0).getName(), "testorg");
        assertEquals(dc.get(0).isAllowPublicRegistration(), false);
        
        assertEquals(dc.get(1).getId(), 2);
        assertEquals(dc.get(1).getName(), "testdataorg");
        assertEquals(dc.get(1).isAllowPublicRegistration(), false);
    }

    /**
     * Test of getByID method, of class OrganisationResource.
     */
    @Test
    public void testGetByID() {
        GenericType<Organisation> gt = new GenericType<Organisation>() { };
        Organisation dc = resource
                .path("organisations")
                .path("2")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
        
        assertEquals(dc.getId(), 2);
        assertEquals(dc.getName(), "testdataorg");
        assertEquals(dc.isAllowPublicRegistration(), false);
    }

    /**
     * Test of getDatasetsByID method, of class OrganisationResource.
     */
    @Test
    public void testGetDatasetsByID() {
        GenericType<List<Dataset>> gt = new GenericType<List<Dataset>>() { };
        List<Dataset> dc = resource
                .path("organisations")
                .path("2")
                .path("datasets")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);  
        
        assertEquals(dc.size(), 2);
        
        assertEquals(dc.get(0).getKey(), "DATASET2");
        assertEquals(dc.get(1).getKey(), "DATASET3");
    }

    /**
     * Test of getLogo method, of class OrganisationResource.
     */
    @Ignore
    public void testGetLogo() {
        GenericType<Object> gt = new GenericType<Object>() { };
        Object dc = resource
                .path("organisations")
                .path("2")
                .path("logo")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
    }

    /**
     * Test of getLogoSmall method, of class OrganisationResource.
     */
    @Ignore
    public void testGetLogoSmall() {
        GenericType<Object> gt = new GenericType<Object>() { };
        Object dc = resource
                .path("organisations")
                .path("1")
                .path("logosmall")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);  

    }
}
