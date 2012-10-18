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
import uk.org.nbn.nbnv.api.dao.warehouse.UserMapper;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenSystemAdministratorUser;

/**
 * Create a provider which will give us access to Users in jersey methods who are
 * System Administrators
 * @author Chris Johnson
 */
@Provider
@Component
public class TokenSystemAdministratorUserProvider implements InjectableProvider<TokenSystemAdministratorUser, Type> {
    @Autowired private UserMapper userMapper;
    @Autowired private UserProviderHelper userObtainer;
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
            return new UserInjector(a);
        }
        return null;
    }

    private class UserInjector implements Injectable<User> {
        private final TokenSystemAdministratorUser userAnnot;
        
        private UserInjector(TokenSystemAdministratorUser userAnnot) {
            this.userAnnot = userAnnot;
        }
        
        /**
         * The following method will return the current requests user. Who has
         * the System Administrator role
         * @return The user for the current context
         * @throws WebApplicationException An unauthorised exception if a logged
         *  in or public user can not be returned.
         */
        @Override public User getValue() {
            User loggedInUser = userObtainer.getValue(r, request, false);
            if(userMapper.isUserSystemAdministrator(loggedInUser.getId())) {
                return loggedInUser;
            }
            else {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
        }
    }
}
