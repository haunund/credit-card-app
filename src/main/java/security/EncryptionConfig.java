package security;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "encryption")
public class EncryptionConfig {
    @ConfigProperty(name = "key")
    public String encryptionKey;

    public String getEncryptionKey() {
        return encryptionKey;
    }


}