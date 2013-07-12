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
import uk.org.nbn.nbnv.api.dao.core.OperationalAttributeMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.AttributeMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.UserMapper;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenSystemAdministratorUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenTaxonObservationAttributeAdminUser;

/**
 * Create a provider which will give us access to Users in jersey methods who are
 * System Administrators
 * @author Chris Johnson
 */
@Provider
@Component
public class TokenTaxonObservationAttributeAdminProdvider implements InjectableProvider<TokenTaxonObservationAttributeAdminUser, Type> {
    @Autowired private AttributeMapper attributeMapper;
    @Autowired private DatasetAdministratorMapper datasetAdministratorMapper;
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
    public Injectable<User> getInjectable(ComponentContext cc, TokenTaxonObservationAttributeAdminUser a, Type c) {
        if (c.equals(User.class)) {
            return new UserInjector(a);
        }
        return null;
    }

    private class UserInjector implements Injectable<User> {
        private final TokenTaxonObservationAttributeAdminUser userAnnot;
        
        private UserInjector(TokenTaxonObservationAttributeAdminUser userAnnot) {
            this.userAnnot = userAnnot;
        }
        
        /**
         * The following method will return the current requests user. Who has
         * dataset admin privileges over an attribute that exists as a Taxon
         * Observation attribute associated with the selected dataset
         * @return The user for the current context
         * @throws WebApplicationException An unauthorised exception if a logged
         * in or public user can not be returned.
         */
        @Override public User getValue() {
            User user = userObtainer.getValue(r, request, false);
            if(datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(),  
                    request.getPathParameters().getFirst(userAnnot.dataset())) &&
                    attributeMapper.isAttributePartOfDataset(
                    request.getPathParameters().getFirst(userAnnot.dataset()), 
                    Integer.parseInt(request.getPathParameters().getFirst(userAnnot.attribute())))) {
                return user;
            }
            else {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
        }
    }
}
