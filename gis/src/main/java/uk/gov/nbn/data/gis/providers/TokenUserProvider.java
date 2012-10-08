/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.gis.providers;

import com.sun.jersey.api.client.WebResource;
import java.lang.annotation.Annotation;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Annotations;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;
import uk.gov.nbn.data.gis.processor.ProviderException;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.gov.nbn.data.gis.providers.annotations.Utilises;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following call will obtain the currently logged in user for a given 
 * request
 * @author Christopher Johnson
 */
@Component
@Utilises({
    @QueryParam(key=TokenUserProvider.USERNAME_KEY),
    @QueryParam(key=TokenUserProvider.MD5_PASSWORD_HASH_KEY)
})
public class TokenUserProvider implements Provider {
    private static final String AUTHENTICATION_ADDRESS = "user";
    public static final String USERNAME_KEY = "username";
    public static final String MD5_PASSWORD_HASH_KEY = "userkey";
    
    @Autowired WebResource api;

    @Override
    public boolean isProviderFor(Class<?> clazz, Annotations annotations) {
        return clazz.equals(User.class);
    }

    @Override
    public Object provide(Class<?> returnType, MapServiceMethod method, HttpServletRequest request, Annotations annotations) throws ProviderException {
        return addUserkeyAndMD5Hash(api.path(AUTHENTICATION_ADDRESS),
            request)
            .header("Cookie", request.getHeader("Cookie"))
            .accept(MediaType.APPLICATION_JSON)
            .get(User.class);
    }
    
    private static WebResource addUserkeyAndMD5Hash(WebResource toAddTo, HttpServletRequest request) {
        if(request.getParameterMap().containsKey(USERNAME_KEY)) {
            return toAddTo
                .queryParam(USERNAME_KEY, request.getParameter(USERNAME_KEY))
                .queryParam(MD5_PASSWORD_HASH_KEY, request.getParameter(MD5_PASSWORD_HASH_KEY));
        }
        return toAddTo;    
    }
}
