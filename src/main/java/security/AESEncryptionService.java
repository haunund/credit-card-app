package security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ApplicationScoped
public class AESEncryptionService {
    private static final String AES_ALGORITHM = "AES";
    // Change to 256 for AES-256
    private static final int AES_KEY_SIZE_BITS = 256;

    @Inject
    EncryptionConfig encryptionConfig;


    public String encrypt(String data) throws Exception {
        byte[] keyBytes = encryptionConfig.getEncryptionKey().getBytes(StandardCharsets.UTF_8);
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

        KeyParameter key = new KeyParameter(keyBytes);
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new AESEngine());

        cipher.init(true, key);

        byte[] encryptedBytes = new byte[cipher.getOutputSize(dataBytes.length)];
        int length = cipher.processBytes(dataBytes, 0, dataBytes.length, encryptedBytes, 0);
        length += cipher.doFinal(encryptedBytes, length);

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
