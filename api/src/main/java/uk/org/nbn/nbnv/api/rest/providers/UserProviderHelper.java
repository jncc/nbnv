
package uk.org.nbn.nbnv.api.rest.providers;

import java.util.Properties;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
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
    @Autowired private Properties properties;
    
    public User getValue(HttpHeaders r, UriInfo pathInfo, boolean allowPublic)  {
        try {
            MultivaluedMap<String, String> query = pathInfo.getQueryParameters();
            Cookie cookie = r.getCookies().get(properties.getProperty("sso_token_key"));
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
                byte[] md5Password = Hex.decodeHex(query.getFirst(MD5_PASSWORD_HASH_KEY).toCharArray());
                return tokenAuth.getUser(tokenAuth.generateToken(
                    query.getFirst(USERNAME_KEY), md5Password, MD5_PASSWORD_HASH_TTL));
            }
            else {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        } catch (InvalidCredentialsException ice) {
            throw new WebApplicationException(ice, Response.Status.UNAUTHORIZED);
        } catch (DecoderException de) {
            throw new WebApplicationException(de, Response.Status.BAD_REQUEST);
        }
    }
    
    private WebApplicationException createWebApplicationExceptionFromException(Exception e) {
        return new WebApplicationException(e, Response
            .fromResponse(userResource.destroyTokenCookie())
            .status(Response.Status.UNAUTHORIZED)
            .build()
        );
    }
}
