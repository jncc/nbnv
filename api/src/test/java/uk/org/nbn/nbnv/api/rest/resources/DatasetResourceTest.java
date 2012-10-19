/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

/**
 *
 * @author Matt Debont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jersey-jetty-applicationContext.xml")
@DirtiesContext
public class DatasetResourceTest {
    @Autowired WebResource resource;
    
    public DatasetResourceTest() {
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
    public void testGetDatasetList() {
       GenericType<List<Dataset>> gt = new GenericType<List<Dataset>>() { };
        List<Dataset> dc = resource
            .path("datasets")
            .accept(MediaType.APPLICATION_JSON)
            .get(gt);
        
        assertEquals(dc.size(), 3);
        
        try {
            assertEquals(dc.get(0).getKey(), "DATASET1");
            assertEquals(dc.get(0).getTitle(), "Test Dataset 1");
            assertEquals(dc.get(0).getDescription(), "Description 1");
            assertEquals(dc.get(0).getTypeName(), "Taxon");
            assertEquals(dc.get(0).getOrganisationName(), "TestOrg 1");
            assertEquals(dc.get(0).getOrganisationID(), 1);
            assertEquals(dc.get(0).getCaptureMethod(), "Method 1");
            assertEquals(dc.get(0).getPurpose(), "Purpose 1");
            assertEquals(dc.get(0).getGeographicalCoverage(), "GeoCover 1");
            assertEquals(dc.get(0).getQuality(), "Quite Interesting");
            assertEquals(dc.get(0).getAdditionalInformation(), "Additional Info 1");
            assertEquals(dc.get(0).getAccessConstraints(), "Access Con 1");
            assertEquals(dc.get(0).getUseConstraints(), "Use Con 1");
            assertEquals(dc.get(0).getDateUploaded(), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2012-03-01 10:00:00"));
            assertEquals(dc.get(0).getMetadataLastEdited(), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2012-03-01 11:11:31"));
            assertEquals(dc.get(0).getTemporalCoverage(), "Temp Cover 1");
            assertEquals(dc.get(0).getUpdateFrequency(), "unknown");

            assertEquals(dc.get(1).getKey(), "DATASET2");
            assertEquals(dc.get(1).getTitle(), "Test Dataset 2");
            assertEquals(dc.get(1).getDescription(), "Description 2");
            assertEquals(dc.get(1).getTypeName(), "Taxon");
            assertEquals(dc.get(1).getOrganisationName(), "TestOrg 2");
            assertEquals(dc.get(1).getOrganisationID(), 2);
            assertEquals(dc.get(1).getCaptureMethod(), "Method 2");
            assertEquals(dc.get(1).getPurpose(), "Purpose 2");
            assertEquals(dc.get(1).getGeographicalCoverage(), "GeoCover 2");
            assertEquals(dc.get(1).getQuality(), "Quite Interesting");
            assertEquals(dc.get(1).getAdditionalInformation(), "Additional Info 2");
            assertEquals(dc.get(1).getAccessConstraints(), "Access Con 2");
            assertEquals(dc.get(1).getUseConstraints(), "Use Con 2");
            assertEquals(dc.get(1).getDateUploaded(), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2012-03-01 10:00:00"));
            assertEquals(dc.get(1).getMetadataLastEdited(), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2012-03-01 11:11:31"));
            assertEquals(dc.get(1).getTemporalCoverage(), "Temp Cover 2");
            assertEquals(dc.get(1).getUpdateFrequency(), "unknown");
            
            assertEquals(dc.get(2).getKey(), "DATASET3");
            assertEquals(dc.get(2).getTitle(), "Test Dataset 3");
            assertEquals(dc.get(2).getDescription(), null);
            assertEquals(dc.get(2).getTypeName(), "Taxon");
            assertEquals(dc.get(2).getOrganisationName(), "TestOrg 2");
            assertEquals(dc.get(2).getOrganisationID(), 2);
            assertEquals(dc.get(2).getCaptureMethod(), null);
            assertEquals(dc.get(2).getPurpose(), null);
            assertEquals(dc.get(2).getGeographicalCoverage(), null);
            assertEquals(dc.get(2).getQuality(), null);
            assertEquals(dc.get(2).getAdditionalInformation(), null);
            assertEquals(dc.get(2).getAccessConstraints(), null);
            assertEquals(dc.get(2).getUseConstraints(), null);
            assertEquals(dc.get(2).getDateUploaded(), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2012-03-01 10:00:00"));
            assertEquals(dc.get(2).getMetadataLastEdited(), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2012-03-01 11:11:31"));
            assertEquals(dc.get(2).getTemporalCoverage(), null);
            assertEquals(dc.get(2).getUpdateFrequency(), "unknown");
        } catch (ParseException ex) {
            fail("Got a ParseException, something is broken with this test");
        }
    }

    @Ignore
    public void testGetDatasetByID() {
        GenericType<List<Dataset>> gt = new GenericType<List<Dataset>>() { };
        List<Dataset> dc = resource
            .path("datasets")
            .path("DATASET3")
            .accept(MediaType.APPLICATION_JSON)
            .get(gt);
        
        assertEquals(dc.size(), 1);
        
        try {
            assertEquals(dc.get(0).getKey(), "DATASET3");
            assertEquals(dc.get(0).getTitle(), "Test Dataset 3");
            assertEquals(dc.get(0).getDescription(), null);
            assertEquals(dc.get(0).getTypeName(), "Taxon");
            assertEquals(dc.get(0).getOrganisationName(), null);
            assertEquals(dc.get(0).getOrganisationID(), 2);
            assertEquals(dc.get(0).getCaptureMethod(), null);
            assertEquals(dc.get(0).getPurpose(), null);
            assertEquals(dc.get(0).getGeographicalCoverage(), null);
            assertEquals(dc.get(0).getQuality(), null);
            assertEquals(dc.get(0).getAdditionalInformation(), null);
            assertEquals(dc.get(0).getAccessConstraints(), null);
            assertEquals(dc.get(0).getUseConstraints(), null);
            assertEquals(dc.get(0).getDateUploaded(), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2012-03-01 10:00:00"));
            assertEquals(dc.get(0).getMetadataLastEdited(), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2012-03-01 11:11:31"));
            assertEquals(dc.get(0).getTemporalCoverage(), null);
            assertEquals(dc.get(0).getUpdateFrequency(), "unknown");
        } catch (ParseException ex) {
            fail("Got a ParseException, something is broken with this test");
        } 
    }
}
