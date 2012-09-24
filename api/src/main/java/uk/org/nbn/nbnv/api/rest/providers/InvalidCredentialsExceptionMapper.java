package uk.org.nbn.nbnv.api.rest.providers;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import uk.org.nbn.nbnv.api.authentication.InvalidCredentialsException;

/**
 * Simple mapper to handle invalid login requests
 * @author Chris Johnson
 */
@Provider
public class InvalidCredentialsExceptionMapper implements ExceptionMapper<InvalidCredentialsException> {

    @Override public Response toResponse(InvalidCredentialsException e) {
        Map<String, Object> toReturn = new HashMap<String, Object>();
        toReturn.put("success", false);
        toReturn.put("message", e.getMessage());
        
        return Response.status(Status.UNAUTHORIZED)
            .type(MediaType.APPLICATION_JSON)
            .entity(toReturn)
            .build();
    }
}
