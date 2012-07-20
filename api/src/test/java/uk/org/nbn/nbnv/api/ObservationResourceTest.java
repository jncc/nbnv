/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Paul Gilbertson
 */
public class ObservationResourceTest extends JerseyTest {
    public ObservationResourceTest() throws Exception {
        super("uk.org.nbn.nbnv.api");
    }
    
    @Test
    public void resourceTest() {
        WebResource webResource = resource();
        String resp = webResource.path("observation").path("21").get(String.class);
        assertEquals("Hello Jon... my id is:21", resp);
    }
}