/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author felix
 */
public class UnauthorisedException extends WebApplicationException {
        /**
      * Create a HTTP 401 (Unauthorised) exception.
     */
     public UnauthorisedException() {
         super(Response.status(Status.UNAUTHORIZED).build());
     }

     /**
      * Create a HTTP 404 (Not Found) exception.
      * @param message the String that is the entity of the 404 response.
      */
     public UnauthorisedException(String message) {
         super(Response.status(Status.UNAUTHORIZED).entity(message).type("text/plain").build());
     }
}
