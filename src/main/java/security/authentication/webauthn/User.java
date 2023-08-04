package security.authentication.webauthn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import security.authentication.webauthn.setup.WebAuthnCredential;

@Table(name = "users")
@Entity
public class User extends PanacheEntity {

    @Column(unique = true)
    public String userName;

    // non-owning side, so we can add more credentials later
    @OneToOne(mappedBy = "user")
    public WebAuthnCredential webAuthnCredential;

    public static Uni<User> findByUserName(String userName) {
        return find("userName", userName).firstResult();
    }
}
