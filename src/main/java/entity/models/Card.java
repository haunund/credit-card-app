package entity.models;


public abstract class Card {

    public String cardNumber;

    public Card(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    protected abstract boolean validate(String cardNumber);

    public String getCardNumber() {
        return this.cardNumber;
    }


    public boolean isChecksumValid(String cardNumber) {
        // Step 1: Remove all non-digit characters (e.g., spaces and dashes)
        cardNumber = cardNumber.replaceAll("[^0-9]", "");

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

        // Step 7: If the total modulo 10 is equal to 0, the checksum is valid
        return total % 10 == 0;
    }
}
