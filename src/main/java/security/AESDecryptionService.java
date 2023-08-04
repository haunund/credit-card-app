package security;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ApplicationScoped
public class AESDecryptionService {
    private static final String AES_ALGORITHM = "AES";
    // Change to 256 for AES-256
    private static final int AES_KEY_SIZE_BITS = 256;

    @Inject
    EncryptionConfig encryptionConfig;


    public String decrypt(String encryptedData) throws Exception {
        byte[] keyBytes = encryptionConfig.getEncryptionKey().getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        KeyParameter key = new KeyParameter(keyBytes);
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new AESEngine());

        cipher.init(false, key);

        byte[] decryptedBytes = new byte[cipher.getOutputSize(encryptedBytes.length)];
        int length = cipher.processBytes(encryptedBytes, 0, encryptedBytes.length, decryptedBytes, 0);
        length += cipher.doFinal(decryptedBytes, length);

        return new String(decryptedBytes, 0, length, StandardCharsets.UTF_8);
    }
}
