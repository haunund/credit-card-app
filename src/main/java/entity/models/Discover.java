package entity.models;
import javax.enterprise.context.ApplicationScoped;
import  entity.models.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

//@ApplicationScoped
public class Discover  extends  Card {

    public Discover(String cardNumber) {
        super(cardNumber);
    }
    @Override
    public boolean validate(String cardNumber) {

        if (Objects.isNull(cardNumber) || StringUtils.isBlank(cardNumber)) {
            return  false;
        }
        // Remove any whitespace or non-numeric characters from the card number
        cardNumber = cardNumber.replaceAll("\\s+", "");

        // Check if the card number is empty or not a string of digits
        if (!cardNumber.matches("\\d+") || cardNumber.length() != 16) {
            return false;
        }

        // Discover card numbers start with 6011
        if (!cardNumber.startsWith("6011")) {
            return false;
        }

        // Apply the Luhn algorithm for validation
        int sum = 0;
        boolean isSecondDigit = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (isSecondDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            isSecondDigit = !isSecondDigit;
        }

        return sum % 10 == 0;
    }
}
