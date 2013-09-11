/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenSystemAdministratorUser;

/**
 *
 * @author Matt Debont
 */
@Provider
@Component
public class TokenSystemAdministratorUserProvider implements InjectableProvider<TokenSystemAdministratorUser, Type> {
    @Autowired UserProviderHelper userObtainer;
    @Autowired OperationalUserMapper oUserMapper;
    @Context private HttpHeaders r;
    @Context private UriInfo request;
    
    @Override
    public ComponentScope getScope() {
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
        
        @Override public User getValue() {
            User user = userObtainer.getValue(r, request, false);
            if (oUserMapper.isUserSystemAdministrator(user.getId())) {
                return user;
            } else {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
        }
    }
}
