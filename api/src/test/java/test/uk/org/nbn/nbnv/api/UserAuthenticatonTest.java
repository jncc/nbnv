package test.uk.org.nbn.nbnv.api;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;
import uk.org.nbn.nbnv.api.authentication.InvalidCredentialsException;
import uk.org.nbn.nbnv.api.authentication.Token;
import uk.org.nbn.nbnv.api.authentication.TokenAuthenticator;
import static org.junit.Assert.*;
import org.springframework.test.annotation.DirtiesContext;
import uk.org.nbn.nbnv.api.authentication.*;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Christopher Johnson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@DirtiesContext
public class UserAuthenticatonTest {
    @Autowired TokenAuthenticator authenticator;
    
    @Test
    public void performValidLogin() throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException {
        //Given
        String username = "tester";
        String password = "password";
        //When
        
        Token generateToken = authenticator.generateToken(username, password, 1000);
        //Then
        assertEquals("The user object returned was invald", username, 
            authenticator.getUser(generateToken).getUsername()
        );
    }
    
    @Test(expected=ExpiredTokenException.class)
    public void useValidTokenAfterExpirey() throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException, InterruptedException {
        //Given
        Token generateToken = authenticator.generateToken("tester", "password", 1);
        
        //When
        Thread.sleep(3); //wait 3 milliseconds
        
        //Then
        User user = authenticator.getUser(generateToken);
        fail("Expected Token Expiry Exception");
    }
    
    @Test(expected=InvalidTokenException.class)
    public void useRandomlyCreatedToken() throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException, InterruptedException {
        //Given
        Token randomToken = new Token(new byte[]{0x01, 0x03, 0x54});
        
        //When
        User user = authenticator.getUser(randomToken);
        
        //Then
        fail("No user should have been obtained with random token");
    }
    
    @Test(expected=InvalidTokenException.class)
    public void useManipulatedToken() throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException, InterruptedException {
        //Given
        Token generateToken = authenticator.generateToken("tester", "password", 1000); //get real token
        byte[] tokenData = generateToken.getBytes();
        tokenData[0] = (byte) (tokenData[0] + 1);
        Token manipulatedToken = new Token(tokenData);
        
        //When
        User user = authenticator.getUser(manipulatedToken);
        
        //Then
        fail("No user should have been obtained with manipulated token");
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void performInvalidUsernameLogin() throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException {
        //Given
        String username = "not_a_tester";
        String password = "password";
        
        //When
        authenticator.generateToken(username, password, 1000);
        
        //Then
        fail("Expected InvalidCredential Exception");
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void performInvalidPasswordLogin() throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException {
        //Given
        String username = "tester";
        String password = "password_not_real";
        
        //When
        authenticator.generateToken(username, password, 1000);
        
        //Then
        fail("Expected InvalidCredential Exception");
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void performInvalidUsernameAndPasswordLogin() throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException {
        //Given
        String username = "not_a_tester";
        String password = "password";
        
        //When
        authenticator.generateToken(username, password, 1000);
        
        //Then
        fail("Expected InvalidCredential Exception");
    }
    
    @Test
    public void performValidMD5Login() throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException, NoSuchAlgorithmException, UnsupportedEncodingException {
        //Given
        String username = "tester";
        byte[] password = MessageDigest.getInstance("md5").digest("password".getBytes("UTF-8"));
        
        //When
        Token generateToken = authenticator.generateToken(username, password, 1000);
        
        //Then
        assertEquals("The user object returned was invald", username, 
            authenticator.getUser(generateToken).getUsername()
        );
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void performInvalidUsernameMD5Login() throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException, NoSuchAlgorithmException, UnsupportedEncodingException {
        //Given
        String username = "not_a_tester";
        byte[] password = MessageDigest.getInstance("md5").digest("password".getBytes("UTF-8"));
        
        //When
        authenticator.generateToken(username, password, 1000);
        
        //Then
        fail("Expected InvalidCredential Exception");
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void performInvalidPasswordMD5Login() throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException, NoSuchAlgorithmException, UnsupportedEncodingException {
        //Given
        String username = "tester";
        byte[] password = MessageDigest.getInstance("md5").digest("password_not_real".getBytes("UTF-8"));
        
        //When
        authenticator.generateToken(username, password, 1000);
        
        //Then
        fail("Expected InvalidCredential Exception");
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void performInvalidUsernameAndMD5PasswordLogin() throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException, NoSuchAlgorithmException, UnsupportedEncodingException {
        //Given
        String username = "not_a_tester";
        byte[] password = MessageDigest.getInstance("md5").digest("password".getBytes("UTF-8"));
        
        //When
        authenticator.generateToken(username, password, 1000);
        
        //Then
        fail("Expected InvalidCredential Exception");
    }
}
