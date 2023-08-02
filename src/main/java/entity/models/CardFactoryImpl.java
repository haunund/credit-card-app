package entity.models;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CardFactoryImpl implements CardFactory {
    public Card createCard(String cardNumber){

        if (cardNumber.equals("") || !isChecksumValid(cardNumber) ){
            return null;
        }


        try {
            // Remove any whitespace or dash characters from the card number
            cardNumber = cardNumber.replaceAll("[\\s-]+", "");

            long number = Double.valueOf(cardNumber).longValue();
            String value = Long.toString(number);
            if (value.length() <= 16) {

                if (value.length() == 15) {
                    return new AmericanExpressCard(cardNumber);

                } else if (value.length() == 16 && value.charAt(0) == '5') {
                    return new MasterCard(cardNumber);

                } else if (value.length() == 16 && value.charAt(0) == '6') {
                    return new Discover(cardNumber);

                } else if ((value.length() == 13 || value.length() == 16) && value.charAt(0) == '4' ) {
                    return new Visa(cardNumber);

                } else {
                    return null;
                }

            }
        }catch (Exception e) {
            return null;
        }
        return null;
    }


    public boolean isChecksumValid(String cardNumber) {
        // Step 1: Remove all non-digit characters (e.g., spaces and dashes)
        cardNumber = cardNumber.replaceAll("[^0-9-]", "").replaceAll("[\\s-]+", "");

        // Step 2: Reverse the card number
        StringBuilder reversedCardNumber = new StringBuilder(cardNumber).reverse();

        int total = 0;
        boolean isEven = false;

        // Step 3: Iterate through each digit of the reversed card number
        for (char digitChar : reversedCardNumber.toString().toCharArray()) {
            int digit = Character.getNumericValue(digitChar);

            if (isEven) {
                // Step 4: Double the value of every second digit
                digit *= 2;

                // Step 5: If the result is greater than 9, subtract 9 from the doubled value
                if (digit > 9) {
                    digit -= 9;
                }
            }

            // Step 6: Sum up all the digits
            total += digit;

            // Alternate between even and odd positions
            isEven = !isEven;
        }

        // Step 7: If the total modulo 10 is not equal to 0, the checksum is valid
        return total % 10 != 0;
    }
/*
    private boolean isChecksumValid(String cardNumber) {
        cardNumber = cardNumber.replaceAll("[^0-9-]", "").replaceAll("[\\s-]+", "");
        cardNumber = cardNumber.replaceAll("[\\s-]+", "");

        int[] ints = new int[cardNumber.length()];
        for (int i = 0; i < cardNumber.length(); i++) {
            ints[i] = Integer.parseInt(cardNumber.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        if (sum % 10 == 0) {
           return true;
        } else {
            return false;

        }
    }
*/


}
