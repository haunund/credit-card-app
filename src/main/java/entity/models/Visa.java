package entity.models;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Visa extends Card {

    public Visa(String cardNumber) {

        super(cardNumber);
    }

    @Override
    public boolean validate(String cardNumber) {

        //test if card number is null and checksum is valid
        if (Objects.isNull(cardNumber) || StringUtils.isBlank(cardNumber) || !this.isChecksumValid(cardNumber)) {
            return  false;
        }

        // Remove any non-numeric characters from the card number
        cardNumber = cardNumber.replaceAll("\\D", "");

        // Check if the card number is empty or not a string of digits
        if (!cardNumber.matches("\\d+") || cardNumber.length() < 13 || cardNumber.length() > 16) {
            return false;
        }

        // Visa card numbers start with 4
        if (cardNumber.charAt(0) != '4') {
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