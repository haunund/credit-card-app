package entity.models;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class MasterCard extends Card {

    public MasterCard(String cardNumber) {
        super(cardNumber);
    }

    @Override
    public boolean validate(String cardNumber) {

        //test if card number is null and checksum is valid
        if (Objects.isNull(cardNumber) || StringUtils.isBlank(cardNumber) || !this.isChecksumValid(cardNumber)) {
            return false;
        }

        // Remove any non-numeric characters from the card number
        cardNumber = cardNumber.replaceAll("\\D", "");

        // Check if the card number is empty or not a string of digits
        if (!cardNumber.matches("\\d+") || cardNumber.length() != 16) {
            return false;
        }

        // Mastercard numbers start with 5 and the second digit must be between 1 and 5
        int secondDigit = Character.getNumericValue(cardNumber.charAt(1));
        if (cardNumber.charAt(0) != '5' || secondDigit < 1 || secondDigit > 5) {
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
