package uk.org.nbn.nbnv.api.authentication;

import uk.org.nbn.nbnv.api.model.User;

/**
 * The following interface defines the methods required in order to be a 
 * CredentialsChangingTokenGenerator
 * @author Christopher Johnson
 */
public interface TokenResetCredentials {
    /**
     * Generates a Token for a given username. Token will be valid for ttl 
     * milliseconds as long as this token is not used to change a users password
     * @param username
     * @param email
     * @param ttl Milliseconds for how long this token is required 
     * @return A generated token for the user
     * @throws InvalidCredentialsException if the user does not exist
     */
    public Token generateToken(String username, String email, int ttl) throws InvalidCredentialsException;
    
    /**
     * Gets the user associated with a particular token
     * @param token The token of which to obtain the user for
     * @return A user associated with the passed in token
     * @throws InvalidTokenException If the token is not valid to this CredentialsChangingTokenGenerator
     * @throws ExpiredTokenException If the token is no longer valid
     */
    public User getUser(String username, Token token) throws InvalidTokenException, ExpiredTokenException;
}
