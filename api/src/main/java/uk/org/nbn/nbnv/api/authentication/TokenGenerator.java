/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.authentication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * The following class creates a simple way of generating secret messages which
 * can be only decrypted by this Token Generator.
 * 
 * @author Christopher Johnson
 */
public class TokenGenerator {
    private static final int INITALIZATION_VECTOR_SIZE = 16;
    private static final int KEY_CHECK_VALUE_SIZE = 32;
    private static final String MAC_ALGORITHM = "HmacSHA256";
    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final String CRYPTO_ALGORITHM = "AES/CBC/PKCS5Padding";

    private final File keyFile;
    
    private SecretKey key, hmac;
    
    public TokenGenerator(File keyFile) throws NoSuchAlgorithmException, IOException {
        this.keyFile = keyFile;
        if(keyFile.exists()) 
            loadKeys();
        else {
            //Ensure that the parent folder for the key file exists. Only do this once
            File parent = keyFile.getParentFile();
            if(!parent.exists() && !parent.mkdirs())
                throw new RuntimeException("Failed to create parent file");
            
            generateKeys(); //generate keys and save these to the key file
        }
    }
    
    /**
     * The following method will reset the secrets of this TokenAuthenticator with
     * new ones. A new encryption key and checkvalue. These will be saved to the
     * key file for this Authenticator
     * @throws NoSuchAlgorithmException 
     */
    public final synchronized void generateKeys() throws NoSuchAlgorithmException, IOException {
        this.key = KeyGenerator.getInstance(SECRET_KEY_ALGORITHM).generateKey();
        this.hmac = KeyGenerator.getInstance(MAC_ALGORITHM).generateKey();
        saveKeys();
    }
    
    /**
     * The following method will perform a secret message decryption of the given
     * token.
     * @param token The token to decrypt
     * @return The message encoded in the Token which was encoded by this 
     *  generator
     * @throws InvalidTokenException If the token was not generated by this Object
     * @throws ExpiredTokenException If the token is no longer valid
     */
    public ByteBuffer getMessage(Token token) throws InvalidTokenException, ExpiredTokenException {
        try {
            Mac mac = Mac.getInstance(MAC_ALGORITHM);
            mac.init(hmac);
            
            if(token.getBytes().length > INITALIZATION_VECTOR_SIZE + mac.getMacLength()) {
                ByteBuffer message = ByteBuffer.wrap(token.getBytes());

                byte[] keyCheckValue = getBytes(message, mac.getMacLength());
                message.mark(); //store the current place into the buffer
                
                //check if this message was created by this authenticator
                if(Arrays.equals(keyCheckValue, mac.doFinal(getBytes(message, message.remaining())))) {
                    message.reset(); //return to just after reading the mac message
                    Cipher decrypt = Cipher.getInstance(CRYPTO_ALGORITHM);
                    decrypt.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec( getBytes(message, INITALIZATION_VECTOR_SIZE) ));

                    ByteBuffer payload = ByteBuffer.wrap(decrypt.doFinal(getBytes(message, message.remaining())));
                    if(payload.getLong() >= Calendar.getInstance().getTimeInMillis())  //is ticket still valid
                        return payload;
                    else 
                        throw new ExpiredTokenException("No longer valid");
                }
            }
            throw new InvalidTokenException("Invalid composite key");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("A configuration error has occurred", ex);
        } catch (NoSuchPaddingException ex) {
            throw new RuntimeException("A configuration error has occurred", ex);
        } catch (InvalidKeyException ex) {
            throw new InvalidTokenException("Invalid composite key");
        } catch (InvalidAlgorithmParameterException ex) {
            throw new InvalidTokenException("Invalid composite key");
        } catch (IllegalBlockSizeException ibse) {
            throw new InvalidTokenException("Invalid composite key");
        } catch (BadPaddingException bpe) {
            throw new InvalidTokenException("Invalid composite key");
        }
    }
    
    /**
     * The following method will perform a SECRET_KEY_ALGORITHM encryption of
     * the following structure.
     * 
     * The byte structure of a token generated by this class is :
     *      BYTE_ARRAY_LENGTH           |                   CONTENT
     * -----------------------------------------------------------------------
     * ---------------------------- PLAINTEXT --------------------------------
     *  32 (byte size of hmac)          | HMAC value of the message including 
     *                                  |   the plain text vector
     *  @see #INITALIZATION_VECTOR_SIZE | The vector used for the message
     * --------------------------- CIPHER TEXT -------------------------------
     *  8 (byte size of long)           | The time in milliseconds from epoch 
     *                                  | when this token expires
     *  message.length                  | A secret message to be encrypted in 
     *                                  |   the token
     * 
     * @param message A message to be encrypted as a token
     * @param type The type of token which will be produced
     * @param ttl Time in milliseconds until this token expires
     * @return An Token object with the above byte structure encrypted in 
     *  @see #SECRET_KEY_ALGORITHM
     */
    public Token generateToken(ByteBuffer messageBuf, int ttl) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] message = new byte[messageBuf.remaining()];
        messageBuf.get(message);
        try {
            Cipher encrypt = Cipher.getInstance(CRYPTO_ALGORITHM);
            encrypt.init(Cipher.ENCRYPT_MODE, key);
            Mac mac = Mac.getInstance(MAC_ALGORITHM);
            mac.init(hmac);
            
            byte[] cipherText = encrypt.doFinal(ByteBuffer.allocate(8 + message.length) //Encrypt byte buffer
                .putLong(Calendar.getInstance().getTimeInMillis() + ttl)    //Store the time when this token becomes invalid
                .put(message)                                               //Store message
                .array()
            );                    
            
            byte[] payload = ByteBuffer.allocate(INITALIZATION_VECTOR_SIZE + cipherText.length)
                .put(encrypt.getIV())
                .put(cipherText)
                .array();
            
            return new Token(ByteBuffer.allocate(mac.getMacLength() + payload.length)
                .put(mac.doFinal(payload))
                .put(payload)
                .array()
            );
        } catch (IllegalBlockSizeException ex) {
            throw new RuntimeException("A configuration error has occurred", ex);
        } catch (BadPaddingException ex) {
            throw new RuntimeException("A configuration error has occurred", ex);
        }
    }
    
    /**
     * The following method will load the key and keycheck values from this beans
     * key file.
     * The structure of this file is as follows :
     *      BYTE_ARRAY_LENGTH           |                   CONTENT
     * -----------------------------------------------------------------------
     *  @see #INITALIZATION_VECTOR_SIZE | The secret key for this authenticator
     *  @see #KEY_CHECK_VALUE_SIZE      | Randomly generated id for this 
     *                                  |   TokenAuthenticator
     * @throws IOException 
     */
    private void loadKeys() throws IOException {
        FileInputStream in = new FileInputStream(keyFile);
        try {
            byte[] keyBytes = new byte[INITALIZATION_VECTOR_SIZE];
            in.read(keyBytes);
            key = new SecretKeySpec(keyBytes, SECRET_KEY_ALGORITHM);
            byte[] hmacBytes = new byte[KEY_CHECK_VALUE_SIZE];
            in.read(hmacBytes);
            hmac = new SecretKeySpec(hmacBytes, MAC_ALGORITHM);
        }
        finally {
            in.close();
        }
    }
    
    /**
     * The following method will persist a secret key file for this bean.
     * @see #loadKeys() For details on the structure of this file.
     * @throws IOException 
     */
    private void saveKeys() throws IOException {
        FileOutputStream out = new FileOutputStream(keyFile);
        try {
            out.write(key.getEncoded());
            out.write(hmac.getEncoded());
            out.flush();
        }
        finally {
            out.close();
        }
    }
    
    /**
     * Utility method to read a byte array of size from a ByteBuffer
     * @param buf The buffer to read from
     * @param size The amount of bytes to read
     * @return A byte array of bytes read from the buffer
     */
    private static byte[] getBytes(ByteBuffer buf, int size) {
        byte[] toReturn = new byte[size];
        buf.get(toReturn);
        return toReturn;
    }  
}
