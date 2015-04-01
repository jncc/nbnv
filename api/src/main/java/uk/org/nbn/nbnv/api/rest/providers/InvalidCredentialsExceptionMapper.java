package uk.org.nbn.nbnv.api.rest.providers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import uk.org.nbn.nbnv.api.authentication.InvalidCredentialsException;
import uk.org.nbn.nbnv.api.model.FriendlyResponse;

/**
 * Simple mapper to handle invalid login requests
 * @author Chris Johnson
 */
@Provider
public class InvalidCredentialsExceptionMapper implements ExceptionMapper<InvalidCredentialsException> {

    @Override public Response toResponse(InvalidCredentialsException e) {        
        return Response.status(Status.UNAUTHORIZED)
            .type(MediaType.APPLICATION_JSON)
            .entity(new FriendlyResponse(false, e.getMessage()))
            .build();
    }
}
