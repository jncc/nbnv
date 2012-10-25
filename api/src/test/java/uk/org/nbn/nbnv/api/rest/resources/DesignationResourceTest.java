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
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.DesignationCategory;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonNavigationGroup;

/**
 *
 * @author Matt Debont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jersey-jetty-applicationContext.xml")
@DirtiesContext
public class DesignationResourceTest {
    @Autowired WebResource resource;
    
    public DesignationResourceTest() {
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
     * Test of getDesignationList method, of class DesignationResource.
     */
    @Test
    public void testGetDesignationList() {
        GenericType<List<Designation>> gt = new GenericType<List<Designation>>() { };
        List<Designation> dc = resource
                .path("designations")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
        
        assertEquals(dc.size(), 3);
        
        assertEquals(dc.get(0).getId(), 0);
        assertEquals(dc.get(0).getName(), "Test Description 1");
        assertEquals(dc.get(0).getLabel(), "TestDes1");
        assertEquals(dc.get(0).getCode(), "TD1");
        assertEquals(dc.get(0).getDesignationCategoryID(), 19);
        assertEquals(dc.get(0).getDescription(), "Thats is a mighty fine test you have there");
        
        assertEquals(dc.get(1).getId(), 1);
        assertEquals(dc.get(1).getName(), "Test Description 2");
        assertEquals(dc.get(1).getLabel(), "TestDes2");
        assertEquals(dc.get(1).getCode(), "TD2");
        assertEquals(dc.get(1).getDesignationCategoryID(), 23);
        assertEquals(dc.get(1).getDescription(), "Would be a shame if something where to happen to it ...");
        
        assertEquals(dc.get(2).getId(), 2);
        assertEquals(dc.get(2).getName(), "Test Description 3");
        assertEquals(dc.get(2).getLabel(), "TestDes3");
        assertEquals(dc.get(2).getCode(), "TD3");
        assertEquals(dc.get(2).getDesignationCategoryID(), 23);
        assertEquals(dc.get(2).getDescription(), "sssssssssssss......");
    }

    /**
     * Test of getDesignation method, of class DesignationResource.
     */
    @Test
    public void testGetDesignation() {
        GenericType<Designation> gt = new GenericType<Designation>() { };
        Designation dc = resource
                .path("designations")
                .path("TD1")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);  
        
        assertEquals(dc.getId(), 0);
        assertEquals(dc.getCode(), "TD1");
    }

    /**
     * Test of getDesignationCategory method, of class DesignationResource.
     */
    @Ignore
    public void testGetDesignationCategory() {
        GenericType<DesignationCategory> gt = new GenericType<DesignationCategory>() { };
        DesignationCategory dc = resource
                .path("designations")
                .path("TD1")
                .path("designationCategories")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
        
        
        assertEquals(dc, null);
    }

    /**
     * Test of getDesignationDatasets method, of class DesignationResource.
     */
    @Ignore
    public void testGetDesignationDatasets() {
        GenericType<List<Dataset>> gt = new GenericType<List<Dataset>>() { };
        List<Dataset> dc = resource
                .path("designations")
                .path("1")
                .path("datasets")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
    }

    /**
     * Test of getSpeciesByDesignationAndTaxonNavigationGroup method, of class DesignationResource.
     */
    @Ignore
    public void testGetSpeciesByDesignationAndTaxonNavigationGroup() {
        GenericType<List<Taxon>> gt = new GenericType<List<Taxon>>() { };
        List<Taxon> dc = resource
                .path("designations")
                .path("1")
                .path("species")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
    }

    /**
     * Test of getTaxonNavigationGroupByDesignation method, of class DesignationResource.
     */
    @Ignore
    public void testGetTaxonNavigationGroupByDesignation() {
        GenericType<TaxonNavigationGroup> gt = new GenericType<TaxonNavigationGroup>() { };
        TaxonNavigationGroup dc = resource
                .path("designations")
                .path("1")
                .path("taxonNavigationGroups")
                .path("1")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
    }
}
