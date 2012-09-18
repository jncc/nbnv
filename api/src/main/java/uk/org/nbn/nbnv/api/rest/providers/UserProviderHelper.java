
package uk.org.nbn.nbnv.api.rest.providers;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.authentication.ExpiredTokenException;
import uk.org.nbn.nbnv.api.authentication.InvalidCredentialsException;
import uk.org.nbn.nbnv.api.authentication.InvalidTokenException;
import uk.org.nbn.nbnv.api.authentication.Token;
import uk.org.nbn.nbnv.api.authentication.TokenAuthenticator;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.resources.UserResource;

/**
 * The following class is provided as a helper for user authentication which can
 * either be provided as a cookie in a key or as a username and md5 userkey pair
 * in the query string.
 * 
 * The rules of precedence are that the query string will trump any cookies 
 * provided in a request.
 * @author Christopher Johnson
 */
@Component
public class UserProviderHelper {
    private static final String USERNAME_KEY = "username";
    private static final String MD5_PASSWORD_HASH_KEY = "userkey";
    private static final int MD5_PASSWORD_HASH_TTL = 5000;
    
    @Autowired private TokenAuthenticator tokenAuth;
    @Autowired private UserResource userResource;
    
    public User getValue(HttpHeaders r, UriInfo pathInfo, boolean allowPublic)  {
        try {
            MultivaluedMap<String, String> query = pathInfo.getQueryParameters();
            Cookie cookie = r.getCookies().get(UserResource.TOKEN_COOKIE_KEY);
            if(query.containsKey(USERNAME_KEY)) {
                return performUserHashLogin(query);
            }
            else if(cookie != null) {
                byte[] tokenBytes = Base64.decodeBase64(cookie.getValue());
                return tokenAuth.getUser(new Token(tokenBytes));
            }
            else if(!allowPublic) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
            return User.PUBLIC_USER;
        }
        catch(InvalidTokenException ite) {
            throw createWebApplicationExceptionFromException(ite);
        }
        catch(ExpiredTokenException ete) {
            throw createWebApplicationExceptionFromException(ete);
        }
    }
    
    private User performUserHashLogin(MultivaluedMap<String, String> query) throws InvalidTokenException, ExpiredTokenException {
        try {
            if(query.containsKey(MD5_PASSWORD_HASH_KEY)) {
                byte[] md5Password = Base64.decodeBase64(query.getFirst(MD5_PASSWORD_HASH_KEY));
                return tokenAuth.getUser(tokenAuth.generateToken(
                    query.getFirst(USERNAME_KEY), md5Password, MD5_PASSWORD_HASH_TTL));
            }
            else {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        } catch (InvalidCredentialsException ice) {
            throw new WebApplicationException(ice, Response.Status.UNAUTHORIZED);
        }        
    }
    
    private WebApplicationException createWebApplicationExceptionFromException(Exception e) {
        return new WebApplicationException(e, Response
            .fromResponse(userResource.destroyTokenCookie())
            .status(Response.Status.FORBIDDEN)
            .build()
        );
    }
}
