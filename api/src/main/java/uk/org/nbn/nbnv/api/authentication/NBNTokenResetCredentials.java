package uk.org.nbn.nbnv.api.authentication;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import javax.crypto.NoSuchPaddingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.UserAuthenticationMapper;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following class will create a token generator which produces tokens which
 * can be used for resetting user credentials without the use of a password.
 * 
 * @author Christopher Johnson
 */
@Component
public class NBNTokenResetCredentials implements TokenResetCredentials {
    private static final String KEY_LOCATION_PROPERTY = "reset_key";
    private static final String CREDENTIALS_DIGEST = "SHA-1";
    private static final String STRING_ENCODING = "UTF-8";
    private final MessageDigest credentialsDigest;
    
    private final TokenGenerator generator;
    
    @Autowired UserAuthenticationMapper userAuthentication;
    /**
     * The following constructor will generate a random key check value and
     * @see #SECRET_KEY_ALGORITHM key for encrypting tokens
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    @Autowired
    public NBNTokenResetCredentials(Properties properties) throws NoSuchAlgorithmException, IOException {
        this.generator = new TokenGenerator(new File(properties.getProperty(KEY_LOCATION_PROPERTY)));
        this.credentialsDigest = MessageDigest.getInstance(CREDENTIALS_DIGEST);
    }
    
    @Override
    public Token generateToken(String username, int ttl) throws InvalidCredentialsException {
        try {
            byte[] usernameHash = credentialsDigest.digest(username.getBytes(STRING_ENCODING));
            byte[] passHash = (byte[])userAuthentication.getUsersPassHash(usernameHash); //cast the passhash to a byte array (mybatis limitation)
            if(passHash != null) //check user exists
                return generator.generateToken(ByteBuffer.wrap(passHash), ttl);
            else
                throw new InvalidCredentialsException("Invalid username and/or email");
        } catch (InvalidKeyException ex) {
            throw new RuntimeException("A configuration error has occurred", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("A configuration error has occurred", ex);
        } catch (NoSuchPaddingException ex) {
            throw new RuntimeException("A configuration error has occurred", ex);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("A configuration error has occurred", ex);
        }
    }

    @Override
    public User getUser(String username, Token token) throws InvalidTokenException, ExpiredTokenException {
        try {
            byte[] usernameHash = credentialsDigest.digest(username.getBytes(STRING_ENCODING));
            ByteBuffer passwordMessage = generator.getMessage(token);
            byte[] passwordHash = new byte[passwordMessage.remaining()];
            passwordMessage.get(passwordHash);
            User user = userAuthentication.getUser(usernameHash, passwordHash);
            if(user != null)
                return user;
            else
                throw new InvalidTokenException("This token has already been used");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("A configuration error has occurred", ex);
        }
    }
}
