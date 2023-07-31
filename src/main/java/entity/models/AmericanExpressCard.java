package entity.models;
import javax.enterprise.context.ApplicationScoped;
import  entity.models.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

//@ApplicationScoped
public class AmericanExpressCard extends Card {

    public AmericanExpressCard(String cardNumber) {
        super(cardNumber);
    }
@Override
public boolean validate(String cardNumber) {
    //test if card number is null
    if (Objects.isNull(cardNumber) || StringUtils.isBlank(cardNumber)) {
        return  false;
    }

    if (Objects.isNull(cardNumber) || StringUtils.isBlank(cardNumber)) {
        return  false;
    }
    // Remove any whitespace or non-numeric characters from the card number
    cardNumber = cardNumber.replaceAll("\\s+", "");


    // Check if the card number is empty or not a string of digits
    if (!cardNumber.matches("\\d+") || cardNumber.length() < 13) {
        return false;
    }


    // Reverse the card number and convert it to an array of integers
    char[] digits = cardNumber.toCharArray();
    for (int i = 0, j = digits.length - 1; i < j; i++, j--) {
        char temp = digits[i];
        digits[i] = digits[j];
        digits[j] = temp;
    }

    // Double every second digit starting from the second-to-last
    for (int i = 1; i < digits.length; i += 2) {
        int digit = Character.getNumericValue(digits[i]);
        digits[i] = Character.forDigit(digit * 2, 10);
    }

    // Subtract 9 from any doubled digit greater than 9
    for (int i = 0; i < digits.length; i++) {
        int digit = Character.getNumericValue(digits[i]);
        if (digit > 9) {
            digits[i] = Character.forDigit(digit - 9, 10);
        }
    }

    // Sum all the digits in the card number
    int sum = 0;
    for (char digit : digits) {
        sum += Character.getNumericValue(digit);
    }

    // Validate the card number based on the Luhn algorithm
    return sum % 10 == 0;
}
}