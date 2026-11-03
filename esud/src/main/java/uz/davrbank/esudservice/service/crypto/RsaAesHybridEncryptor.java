package uz.davrbank.esudservice.service.crypto;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Utility class for performing hybrid AES-256 and RSA encryption and decryption.
 * <p>
 * This class provides methods for:
 * <ul>
 *     <li>Generating AES-256 symmetric keys</li>
 *     <li>Encrypting and decrypting data using AES in GCM mode</li>
 *     <li>Encrypting and decrypting AES keys using RSA</li>
 *     <li>Generating secure random initialization vectors (IVs)</li>
 * </ul>
 * </p>
 * <p>
 * Typically used for securely transmitting data to external services (e.g., court systems)
 * where the AES session key is encrypted with the recipient's RSA public key.
 * </p>
 */
@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class RsaAesHybridEncryptor {

    public static final int AES_KEY_SIZE = 256;
    public static final int GCM_IV_LENGTH = 12;
    public static final byte[] GCM_STATIC_IV = new byte[]{-75, -41, 63, -127, -99, 22, -48, -54, 58, -111, 15, 69};
    public static final int GCM_TAG_LENGTH = 128;

    /**
     * Generates a new AES-256 secret key.
     *
     * @return a newly generated {@link SecretKey} for AES-256 encryption
     * @throws Exception if the AES algorithm is not available
     */
    public static SecretKey genAESKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(AES_KEY_SIZE);
        return kg.generateKey();
    }

    /**
     * Encrypts plaintext data using AES in GCM mode.
     *
     * @param data the plaintext data to encrypt
     * @param key  the AES secret key
     * @param iv   the initialization vector (IV). If null, a static IV is used
     * @return the encrypted ciphertext
     * @throws Exception if encryption fails
     */
    public static byte[] aesEncrypt(byte[] data, SecretKey key, byte[] iv) throws Exception {
        if (iv == null) {
            iv = GCM_STATIC_IV;
        }
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        c.init(Cipher.ENCRYPT_MODE, key, spec);
        return c.doFinal(data);
    }

    /**
     * Decrypts ciphertext data using AES in GCM mode.
     *
     * @param cipher the ciphertext data to decrypt
     * @param key    the AES secret key
     * @param iv     the initialization vector (IV). If null, a static IV is used
     * @return the decrypted plaintext
     * @throws SecurityException if integrity verification fails
     * @throws Exception         if decryption fails
     */
    public static byte[] aesDecrypt(byte[] cipher, SecretKey key, byte[] iv) throws Exception {
        if (iv == null) {
            iv = GCM_STATIC_IV;
        }
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        c.init(Cipher.DECRYPT_MODE, key, spec);
        try {
            return c.doFinal(cipher);
        } catch (AEADBadTagException e) {
            throw new SecurityException("Integrity failure!", e);
        }
    }

    /**
     * Encrypts an AES secret key using an RSA public key.
     *
     * @param aesKey the AES key to encrypt
     * @param pubKey the RSA public key used for encryption
     * @return the encrypted AES key bytes
     * @throws Exception if encryption fails
     */
    public static byte[] rsaEncryptKey(SecretKey aesKey, PublicKey pubKey) throws Exception {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, pubKey);
        return c.doFinal(aesKey.getEncoded());
    }

    /**
     * Decrypts an AES secret key using an RSA private key.
     *
     * @param encKey  the encrypted AES key bytes
     * @param privKey the RSA private key used for decryption
     * @return the decrypted {@link SecretKey}
     * @throws Exception if decryption fails
     */
    public static SecretKey rsaDecryptKey(byte[] encKey, PrivateKey privKey) throws Exception {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, privKey);
        byte[] keyBytes = c.doFinal(encKey);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * Generates a secure random initialization vector (IV) of length {@link #GCM_IV_LENGTH}.
     *
     * @return a random IV byte array
     */
    public static byte[] genIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}
