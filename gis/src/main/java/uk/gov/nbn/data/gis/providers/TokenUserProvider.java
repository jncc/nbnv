/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.gis.providers;

import com.sun.jersey.api.client.WebResource;
import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;
import uk.gov.nbn.data.gis.processor.ProviderException;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following call will obtain the currently logged in user for a given 
 * request
 * @author Christopher Johnson
 */
@Component
public class TokenUserProvider implements Provider {
    private static final String AUTHENTICATION_ADDRESS = "user";
    
    @Autowired WebResource api;

    @Override
    public boolean isProviderFor(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) {
        return clazz.equals(User.class);
    }

    @Override
    public Object provide(Class<?> returnType, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) throws ProviderException {
        return api
            .path(AUTHENTICATION_ADDRESS)
            .header("Cookie", request.getHeader("Cookie"))
            .accept(MediaType.APPLICATION_JSON)
            .get(User.class);
    }
}
