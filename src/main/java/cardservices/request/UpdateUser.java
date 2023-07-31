package cardservices.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUser {
    String firstName;
    String lastName;
    String email;
}
