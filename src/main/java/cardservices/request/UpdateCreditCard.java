package cardservices.request;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.inject.Named;

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
