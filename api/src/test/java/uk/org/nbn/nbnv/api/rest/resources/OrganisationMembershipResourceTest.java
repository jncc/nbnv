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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;

/**
 *
 * @author Matt Debont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jersey-jetty-applicationContext.xml")
@DirtiesContext
public class OrganisationMembershipResourceTest {
    @Autowired WebResource resource;
    
    public OrganisationMembershipResourceTest() {
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
     * Test of get method, of class OrganisationMembershipResource.
     */
    @Test
    public void testGet() {
        GenericType<List<OrganisationMembership>> gt = new GenericType<List<OrganisationMembership>>() { };
        List<OrganisationMembership> dc = resource
                .path("organisationMemberships")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
        
        assertEquals(dc.size(), 2);
        
        assertEquals(dc.get(0).getUser().getId(), 41);
        assertEquals(dc.get(0).getOrganisation().getId(), 1);
        assertEquals(dc.get(0).getRole(), OrganisationMembership.Role.administrator);
        
        assertEquals(dc.get(1).getUser().getId(), 40);
        assertEquals(dc.get(1).getOrganisation().getId(), 1);
        assertEquals(dc.get(1).getRole(), OrganisationMembership.Role.member);
    }
}
