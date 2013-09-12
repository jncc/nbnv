package uk.org.nbn.nbnv.api.rest.providers;

import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
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
import uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenSystemAdministratorUser;

/**
 * Create a provider which will give us access to Users in jersey methods
 * @author Chris Johnson
 */
@Provider
@Component
public class TokenSystemAdministratorUserProvider implements InjectableProvider<TokenSystemAdministratorUser, Type> {
    
    @Autowired private UserProviderHelper userObtainer;
    @Autowired private OperationalUserMapper oUserMapper;
    @Context private HttpHeaders r;
    @Context private UriInfo request;
    

    /**
     * A new Injectable is instantiated per request
     */
    @Override public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }
    
    @Override
    public Injectable<User> getInjectable(ComponentContext cc, TokenSystemAdministratorUser a, Type c) {
        if (c.equals(User.class)) {
            return new TokenSystemAdministratorUserProvider.UserInjector(a);
        }
        return null;
    }

    private class UserInjector implements Injectable<User> {
        private final TokenSystemAdministratorUser userAnnot;
        
        private UserInjector(TokenSystemAdministratorUser userAnnot) {
            this.userAnnot = userAnnot;
        }
        
        /**
         * The following method will return the current requests user. If no 
         * user is logged in and the TokenUser annotation allows public access
         * then a public user will be returned. If suppression of exceptions is
         * defined in the exception then a public user will be returned in those
         * cases.
         * @return The user for the current context
         * @throws WebApplicationException An unauthorised exception if a logged
         *  in or public user can not be returned.
         */
        @Override public User getValue() {
            User u = userObtainer.getValue(r, request, false);
            if (oUserMapper.isUserSystemAdministrator(u.getId())) {
                return u;
            } else {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
        }
    }
}
