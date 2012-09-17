package uk.org.nbn.nbnv.api.rest.providers;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import java.lang.reflect.Type;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.authentication.ExpiredTokenException;
import uk.org.nbn.nbnv.api.authentication.InvalidTokenException;
import uk.org.nbn.nbnv.api.dao.mappers.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.model.DatasetAdministrator;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.security.UserProviderHelper;

/**
 * The following Injectable Provider will produce users who have been checked for
 * dataset administration rights to a given dataset as specified as a path param 
 * in the URL called which triggers this provider.
 * @author Christopher Johnson
 */
@Provider
@Component
public class TokenDatasetAdminUserProvider implements InjectableProvider<TokenDatasetAdminUser, Type> {   
    @Autowired private DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired private UserProviderHelper userObtainer;
    @Context private UriInfo request;
    @Context private HttpHeaders headers;
    
    /**
     * A new Injectable is instantiated per request
     */
    @Override public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override public Injectable<User> getInjectable(ComponentContext cc, TokenDatasetAdminUser a, Type c) {
        if (c.equals(User.class)) {
            return new UserInjector(a);
        }
        return null;
    }
    
    private class UserInjector implements Injectable<User> {
        private final TokenDatasetAdminUser userAnnot;
        
        private UserInjector(TokenDatasetAdminUser userAnnot) {
            this.userAnnot = userAnnot;
        }
        
        /**
         * The following method will return the a User who is not public is an
         * administrator for a given dataset as specified in the user annotation
         * @return A user who is a administrator of the given dataset
         * @throws WebApplicationException If an invalid token, expired token, 
         * user is not an administrator of the specified dataset.
         */
        @Override public User getValue() {
            try {
                User user = userObtainer.getValue(headers, false); //get the logged in user
                String datasetKey = request.getPathParameters().getFirst(userAnnot.path());
                DatasetAdministrator datasetAdministrator = datasetAdministratorMapper.selectByUserAndDataset(user.getId(), datasetKey);
                
                if(datasetAdministrator != null) {
                    return datasetAdministrator.getUser();
                }
                else {
                    throw new WebApplicationException(Response.Status.FORBIDDEN);
                }
            } catch (InvalidTokenException ite) {
                throw new WebApplicationException(ite, Response.Status.UNAUTHORIZED);
            } catch (ExpiredTokenException ete) {
                throw new WebApplicationException(ete, Response.Status.UNAUTHORIZED);
            }
        }
    }
}
