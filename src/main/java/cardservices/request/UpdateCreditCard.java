package cardservices.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Named;

@Getter
@Setter
@ApplicationScoped
//Updates a credit card using cardNumber, cardHolderName and expiryDate
public class UpdateCreditCard {
    public String id;

    public String cardNumber;
    public String cardHolderName;
    public String expiryDate;

}
