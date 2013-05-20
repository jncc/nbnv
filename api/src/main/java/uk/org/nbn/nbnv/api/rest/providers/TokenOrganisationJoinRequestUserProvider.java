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
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationJoinRequestMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.model.OrganisationJoinRequest;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenOrganisationJoinRequestUser;

/**
 * The following Injectable Provider will produce users who have been checked for
 * specified permissions to some organisation join request stated as a path param 
 * in the URL called which triggers this provider.
 * @author Matt Debont
 */
@Provider
@Component
public class TokenOrganisationJoinRequestUserProvider implements InjectableProvider<TokenOrganisationJoinRequestUser, Type> {   
    @Autowired private OperationalOrganisationMembershipMapper oOrganisationMembershipMapper;
    @Autowired private OperationalOrganisationJoinRequestMapper oOrganisationJoinRequestMapper;
    @Autowired private UserProviderHelper userObtainer;
    @Context private UriInfo request;
    @Context private HttpHeaders headers;
    
    /**
     * A new Injectable is instantiated per request
     */
    @Override public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override public Injectable<User> getInjectable(ComponentContext cc, TokenOrganisationJoinRequestUser a, Type c) {
        if (c.equals(User.class)) {
            return new UserInjector(a);
        }
        return null;
    }
    
    private class UserInjector implements Injectable<User> {
        private final TokenOrganisationJoinRequestUser userAnnot;
        
        private UserInjector(TokenOrganisationJoinRequestUser userAnnot) {
            this.userAnnot = userAnnot;
        }
        
        /**
         * The following method will return the a User who is not public and has
         * one of the access to the request id given by the
         * TokenOrganisationJoinRequestUser path attribute.
         * @return A user is able to access the request (i.e. org admin or 
         * requesting user)
         * @throws WebApplicationException If an invalid token, expired token, 
         * user is not a member or user does not have the valid membership role.
         */
        @Override public User getValue() {
            User user = userObtainer.getValue(headers, request, false); //get the logged in user
            
            int requestID = Integer.parseInt(request.getPathParameters().getFirst(userAnnot.path()));
            
            OrganisationJoinRequest req = oOrganisationJoinRequestMapper.getJoinRequestByID(requestID);
            
            if(req != null) {
                if (req.getUser().getId() == user.getId()) {
                    return req.getUser();
                }
                
                if (oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), req.getOrganisation().getId())) {
                    return user;
                }
                
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
            
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}