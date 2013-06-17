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
import uk.org.nbn.nbnv.api.dao.core.OperationalSurveyMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.model.Survey;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetSurveyAdminUser;

/**
 * The following Injectable Provider will produce users who have been checked
 * for dataset administration rights to a given dataset as specified as a path
 * param in the URL called which triggers this provider.
 *
 * @author Christopher Johnson
 */
@Provider
@Component
public class TokenDatasetSurveyAdminUserProvider implements InjectableProvider<TokenDatasetSurveyAdminUser, Type> {

    @Autowired
    private DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired
    private OperationalSurveyMapper oSurveyMapper;
    @Autowired
    private UserProviderHelper userObtainer;
    @Context
    private UriInfo request;
    @Context
    private HttpHeaders headers;

    /**
     * A new Injectable is instantiated per request
     */
    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable<User> getInjectable(ComponentContext cc, TokenDatasetSurveyAdminUser a, Type c) {
        if (c.equals(User.class)) {
            return new UserInjector(a);
        }
        return null;
    }

    private class UserInjector implements Injectable<User> {

        private final TokenDatasetSurveyAdminUser userAnnot;

        private UserInjector(TokenDatasetSurveyAdminUser userAnnot) {
            this.userAnnot = userAnnot;
        }

        /**
         * The following method will return the a User who is not public is an
         * administrator for a given dataset and survey as specified in the user 
         * annotation
         *
         * @return A user who is a administrator of the given dataset and survey
         * @throws WebApplicationException If an invalid token, expired token,
         * user is not an administrator of the specified dataset throw a 403, if 
         * the survey does not exist or is not part of this dataset throw a 404.
         */
        @Override
        public User getValue() {
            User user = userObtainer.getValue(headers, request, false); //get the logged in user
            String datasetKey = request.getPathParameters().getFirst(userAnnot.dataset());
            int surveyID = Integer.parseInt(request.getPathParameters().getFirst(userAnnot.survey()));

            boolean temp = datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey);
            
            Survey survey = oSurveyMapper.getSurveyById(surveyID);
            
            // Throw a 404 if the survey doesn't belong to this dataset or even
            // exist
            if (survey == null || !survey.getDatasetKey().equals(datasetKey)) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            // Throw a 403 if the current user is not a dataset admin for the 
            // specified database
            if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }

            return user;
        }
    }
}
