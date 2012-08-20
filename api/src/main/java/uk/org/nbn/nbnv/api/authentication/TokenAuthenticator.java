package uk.org.nbn.nbnv.api.authentication;

import uk.org.nbn.nbnv.api.model.User;

/**
 * The following interface defines the methods required in order to be a 
 * TokenAuthenticator
 * @author Christopher Johnson
 */
public interface TokenAuthenticator {
    public Token generateToken(String username, String password, int ttl) throws InvalidCredentialsException;
    
    public Token generateToken(String username, byte[] md5_password, int ttl) throws InvalidCredentialsException;

    public User getUser(Token token) throws InvalidTokenException, ExpiredTokenException;
}
