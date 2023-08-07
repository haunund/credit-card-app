package security.authentication.webauthn.setup;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@Entity
public class WebAuthnCertificate extends PanacheEntity {
    
    @ManyToOne
    public WebAuthnCredential webAuthnCredential;
    
    /**
     * The list of X509 certificates encoded as base64url.
     */
    public String x509certificates;
}
