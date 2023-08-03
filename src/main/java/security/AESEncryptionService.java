package security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import security.EncryptionConfig;

@ApplicationScoped
public class AESEncryptionService {
    private static final String AES_ALGORITHM = "AES";

    @Inject
    EncryptionConfig encryptionConfig;

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(encryptionConfig.getEncryptionKey().getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
