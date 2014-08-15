package uk.org.nbn.nbnv.importer.ui.config;

import com.sun.jersey.api.client.WebResource;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import uk.ac.ceh.dynamo.arguments.QueryParameterResolver;
import uk.org.nbn.nbnv.api.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.Set;

/**
 * The following call will obtain the currently logged in user for a given 
 * request
 * @author Christopher Johnson
 */
public class TokenUserArgumentResolver implements HandlerMethodArgumentResolver, QueryParameterResolver {
    private static final String AUTHENTICATION_ADDRESS = "user";
    public static final String USERNAME_KEY = "username";
    public static final String MD5_PASSWORD_HASH_KEY = "userkey";
    
    private final WebResource api;
    
    public TokenUserArgumentResolver(WebResource api) {
        this.api = api;
    }
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Set<String> getUtilisedQueryParameters(MethodParameter methodParameter) {
        Set<String> parameters = new HashSet<String>();
        parameters.add(USERNAME_KEY);
        parameters.add(MD5_PASSWORD_HASH_KEY);
        return parameters; 
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return getTokenUserWebResourceBuilder(api.path(AUTHENTICATION_ADDRESS), webRequest.getNativeRequest(HttpServletRequest.class))
            .accept(MediaType.APPLICATION_JSON)
            .get(User.class);
    }
    
    public static WebResource.Builder getTokenUserWebResourceBuilder(WebResource resource, HttpServletRequest request) {
        return addUserkeyAndMD5Hash(resource, request)
            .header("Cookie", request.getHeader("Cookie"));
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
