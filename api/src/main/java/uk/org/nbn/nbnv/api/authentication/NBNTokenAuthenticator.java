package uk.org.nbn.nbnv.api.authentication;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Properties;
import javax.crypto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserAuthenticationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.UserAuthenticationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.UserMapper;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following is a Simple token authentication powered by the NBN Gateway
 * database.
 *
 * This Token Authenticator is configured as a Spring Bean
 *
 * For a detailed description of the byte structure of the tokens generated by
 * this class, @see TokenGenerator#generateToken(byte[], Token.Type, int)
 *
 * @author Christopher Johnson
 */
@Component
public class NBNTokenAuthenticator implements TokenAuthenticator {

	private static final String AUTHENTICATOR_KEY_LOCATION_PROPERTY = "authenticator_key";
	private static final String CREDENTIALS_DIGEST = "SHA-1";
	private static final String STRING_ENCODING = "UTF-8";

	private final TokenGenerator generator;

	@Autowired
	UserAuthenticationMapper userAuthentication;
	@Autowired
	OperationalUserAuthenticationMapper oUserAuthentication;
	@Autowired
	UserAuthenticationMapper UserAuthenticationMapper;
	@Autowired
	UserMapper userMapper;

	/**
	 * The following constructor will initalize a message digest and the
	 * TokenGenerator
	 *
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	@Autowired
	public NBNTokenAuthenticator(Properties properties) throws NoSuchAlgorithmException, IOException {
		this.generator = new TokenGenerator(new File(properties.getProperty(AUTHENTICATOR_KEY_LOCATION_PROPERTY)));
	}

	/**
	 * The following method supports the generation of tokens for username and
	 * password pairs
	 *
	 * @param username The username of the principle
	 * @param password The password of the principle
	 * @param ttl The time this token should live for
	 * @return The Token which can be reused by this class for obtaining a user
	 * @see #getUser(uk.org.nbn.nbnv.api.authentication.Token)
	 * @throws InvalidCredentialsException if the username and password are
	 * invalid
	 */
	@Override
	public Token generateToken(String username, String password, int ttl) throws InvalidCredentialsException {
		try {
			MessageDigest credentialsDigest = MessageDigest.getInstance(CREDENTIALS_DIGEST);
			byte[] usernameHash = credentialsDigest.digest(username.getBytes(STRING_ENCODING));

			User user;

			try {
				user = oUserAuthentication.getUser(usernameHash, credentialsDigest.digest(password.getBytes(STRING_ENCODING)));
			} catch (Exception ex) {
				user = userAuthentication.getUser(usernameHash, credentialsDigest.digest(password.getBytes(STRING_ENCODING)));
			}

			if (user != null) { //check credentials are okay 
				if (user.isActive()) {
					return generator.generateToken(getIntAsBuffer(user.getId()), ttl);
				}
				throw new InvalidCredentialsException("User has not been activated");
			} else {
				throw new InvalidCredentialsException("Invalid username and/or password");
			}
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

	/**
	 * The following method supports the generation of tokens for web services
	 * which use an md5 hash of a password to log in.
	 *
	 * @param username The username of the principle
	 * @param md5Hash A md5hash of a users password
	 * @param ttl The time this token should live for
	 * @return The Token which can be reused by this class for obtaining a user
	 * @see #getUser(uk.org.nbn.nbnv.api.authentication.Token)
	 * @throws InvalidCredentialsException if the username and md5Hash are
	 * invalid
	 */
	@Override
	public Token generateToken(String username, byte[] md5Hash, int ttl) throws InvalidCredentialsException {
		try {
			MessageDigest credentialsDigest = MessageDigest.getInstance(CREDENTIALS_DIGEST);
			byte[] usernameHash = credentialsDigest.digest(username.getBytes(STRING_ENCODING));

			User user;

			try {
				// Attempt to get user from the core database
				user = oUserAuthentication.getUserMD5(usernameHash, credentialsDigest.digest(md5Hash));
			} catch (Exception ex) {
				// Failed to get connection to core database attempting to get 
				// user from warehouse as a fallback
				user = userAuthentication.getUserMD5(usernameHash, credentialsDigest.digest(md5Hash));
			}

			if (user != null) //check credentials are okay
			{
				return generator.generateToken(getIntAsBuffer(user.getId()), ttl);
			} else {
				throw new InvalidCredentialsException("Invalid username and/or password");
			}
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

	/**
	 * The following method will obtain a user object for a given token
	 *
	 * @param token The token which represents a particular user
	 * @return The user which this token represents
	 * @throws InvalidTokenException If the token is not of this authority
	 * @throws ExpiredTokenException If the token is no longer valid
	 */
	@Override
	public User getUser(Token token) throws InvalidTokenException, ExpiredTokenException {
		User toReturn = userMapper.getUserById(generator.getMessage(token).getInt());
		if (toReturn == null) {
			throw new InvalidTokenException("No user exists for this token");
		}
		return toReturn;
	}

	private static ByteBuffer getIntAsBuffer(int id) {
		ByteBuffer toReturn = ByteBuffer.allocate(4).putInt(id);
		toReturn.flip();
		return toReturn;
	}
}
