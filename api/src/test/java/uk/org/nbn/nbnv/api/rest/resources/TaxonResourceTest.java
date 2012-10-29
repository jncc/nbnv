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
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

/**
 *
 * @author Matt Debont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jersey-jetty-applicationContext.xml")
@DirtiesContext
public class TaxonResourceTest {
    @Autowired WebResource resource;
    
    public TaxonResourceTest() {
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
     * Test of getTaxon method, of class TaxonResource.
     */
    @Test
    public void testGetTaxon() {
        GenericType<Taxon> gt = new GenericType<Taxon>() { };
        Taxon dc = resource
                .path("taxa")
                .path("ABFG000000100001")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
        
        assertEquals(dc.getTaxonVersionKey(), "ABFG000000100001");
        assertEquals(dc.getName(), "Annulohypoxylon multiforme var. multiforme");
    }

    /**
     * Test of getDatasetListForTaxonViewableByUser method, of class TaxonResource.
     */
    @Ignore
    public void testGetDatasetListForTaxonViewableByUser() {

    }

    /**
     * Test of getTaxa method, of class TaxonResource.
     */
    @Ignore
    public void testGetTaxa() throws Exception {

    }
}
