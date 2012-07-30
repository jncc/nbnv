/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Administrator
 */
@Path("/observation/{id}")
public class ObservationResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getObservation(@PathParam("id") int id) {
        return "Hello Jon... my id is:" + id;
    }
}
