package davr.uzcard.Service;

import lombok.SneakyThrows;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;

@Service

public class DecryptService {
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private final Cipher cipher;
    byte[] arrayBytes;
    SecretKey key;

    @SneakyThrows
    public DecryptService() {
        String myEncryptionKey = "ThisIsSpartaThisIsSparta";
        String myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        KeySpec ks = new DESedeKeySpec(arrayBytes);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
    }


    @SneakyThrows
    public String encrypt(String value) {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] plainText = value.getBytes(UNICODE_FORMAT);
        byte[] encryptedText = cipher.doFinal(plainText);
        return new String(Base64.encode(encryptedText));
    }


    @SneakyThrows
    public String decrypt(String encryptedValue) {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedText = Base64.decode(encryptedValue);
        byte[] plainText = cipher.doFinal(encryptedText);
        return new String(plainText);
    }


}
