
package uk.org.nbn.nbnv.api.rest.security;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.authentication.ExpiredTokenException;
import uk.org.nbn.nbnv.api.authentication.InvalidTokenException;
import uk.org.nbn.nbnv.api.authentication.Token;
import uk.org.nbn.nbnv.api.authentication.TokenAuthenticator;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.resources.UserResource;

/**
 *
 * @author Administrator
 */
@Component
public class UserProviderHelper {
    @Autowired private TokenAuthenticator tokenAuth;
    
    public User getValue(HttpHeaders r, boolean allowPublic) throws InvalidTokenException, ExpiredTokenException {
        Cookie cookie = r.getCookies().get(UserResource.TOKEN_COOKIE_KEY);
        if(cookie != null) {
            byte[] tokenBytes = Base64.decodeBase64(cookie.getValue());
            return tokenAuth.getUser(new Token(tokenBytes));
        }
        else if(!allowPublic) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return User.PUBLIC_USER;
    }
}
