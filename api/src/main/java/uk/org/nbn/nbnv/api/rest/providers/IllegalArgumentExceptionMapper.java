package uk.org.nbn.nbnv.api.rest.providers;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Simple mapper to handle invalid login requests
 * @author Chris Johnson
 */
@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override public Response toResponse(IllegalArgumentException e) {
        Map<String, Object> toReturn = new HashMap<String, Object>();
        toReturn.put("success", false);
        toReturn.put("status", e.getMessage());
        
        return Response.status(Status.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON)
            .entity(toReturn)
            .build();
    }
}
