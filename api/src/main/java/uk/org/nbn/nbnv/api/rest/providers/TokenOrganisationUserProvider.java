package uk.org.nbn.nbnv.api.rest.providers;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
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
import uk.org.nbn.nbnv.api.dao.mappers.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.OrganisationMembership.Role;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenOrganisationUser;

/**
 * The following Injectable Provider will produce users who have been checked for
 * specified role membership to some organisation stated as a path param in the
 * URL called which triggers this provider.
 * @author Christopher Johnson
 */
@Provider
@Component
public class TokenOrganisationUserProvider implements InjectableProvider<TokenOrganisationUser, Type> {   
    @Autowired private OrganisationMembershipMapper organisationMembershipMapper;
    @Autowired private UserProviderHelper userObtainer;
    @Context private UriInfo request;
    @Context private HttpHeaders headers;
    
    /**
     * A new Injectable is instantiated per request
     */
    @Override public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override public Injectable<User> getInjectable(ComponentContext cc, TokenOrganisationUser a, Type c) {
        if (c.equals(User.class)) {
            return new UserInjector(a);
        }
        return null;
    }
    
    private class UserInjector implements Injectable<User> {
        private final TokenOrganisationUser userAnnot;
        
        private UserInjector(TokenOrganisationUser userAnnot) {
            this.userAnnot = userAnnot;
        }
        
        /**
         * The following method will return the a User who is not public and has
         * one of the required roles for the organisation given in the 
         * TokenOrganisationUser path attribute.
         * @return A user who is a member of the specified organisation with the
         * given roles
         * @throws WebApplicationException If an invalid token, expired token, 
         * user is not a member or user does not have the valid membership role.
         */
        @Override public User getValue() {
            try {
                User user = userObtainer.getValue(headers, false); //get the logged in user
                EnumSet<Role> requiredRoles = EnumSet.copyOf(Arrays.asList(userAnnot.roles()));
                int organisationID = Integer.parseInt(request.getPathParameters().getFirst(userAnnot.path()));
                OrganisationMembership membership = organisationMembershipMapper.selectByUserAndOrganisation(user.getId(), organisationID);
                if(membership != null && requiredRoles.contains(membership.getRole())) {
                    return membership.getUser();
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
