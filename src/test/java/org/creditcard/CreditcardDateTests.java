package org.creditcard;


import entity.CreditCard;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import parsers.CsvParserService;
import process.common.TechnicalRuntimeException;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class CreditcardDateTests {

    @Inject
    CsvParserService csvParserService;


    public CreditCard getFirstCreditCard() throws IOException {
        String inputFilename = "cards/creditcards.csv";
        List<CreditCard> creditCards = csvParserService.readFilename(inputFilename);
        return creditCards.get(0);
    }

    @Test
    public void testValidExpirationDate() throws IOException {
        String expiryDate = "10/25";
        CreditCard creditCard = getFirstCreditCard();
        creditCard.setExpiryDate(expiryDate);
        assertTrue(validateExpirationDate(expiryDate, creditCard));
    }

    @Test
    public void testInvalidExpirationDate() throws IOException {
        String expiryDate1 = "10/15";
        String expiryDate2 = "05/21";

        CreditCard creditCard = getFirstCreditCard();
        creditCard.setExpiryDate(expiryDate1);
        assertFalse(validateExpirationDate(expiryDate1, creditCard));
        creditCard.setExpiryDate(expiryDate2);
        assertFalse(validateExpirationDate(expiryDate2, creditCard));

    }

    @Test
    public void testFutureExpirationDate() throws IOException {
        String expiryDate1 = "12/27";
        String expiryDate2 = "05/23";

        CreditCard creditCard1 = new CreditCard();
        creditCard1.setExpiryDate(expiryDate1);
        assertTrue(validateExpirationDate(expiryDate1, creditCard1));

        CreditCard creditCard2 = new CreditCard();
        creditCard2.setExpiryDate(expiryDate2);
        assertFalse(validateExpirationDate(expiryDate2, creditCard2));
    }

    @Test
    public void testInvalidFormatExpirationDate() throws IOException {
        String expiryDate1 = "1025";
        String expiryDate2 = "ab/cd";
        String expiryDate3 = "12@/23";

        CreditCard creditCard = getFirstCreditCard();
        creditCard.setExpiryDate(expiryDate1);
        assertFalse(validateExpirationDate(expiryDate1, creditCard));
        creditCard.setExpiryDate(expiryDate2);
        assertFalse(validateExpirationDate(expiryDate2, creditCard));
        creditCard.setExpiryDate(expiryDate3);
        assertFalse(validateExpirationDate(expiryDate3, creditCard));
    }

    @Test
    public void testInvalidMonthExpirationDate() throws IOException {
        String expiryDate = "13/23";
        CreditCard creditCard = getFirstCreditCard();
        creditCard.setExpiryDate(expiryDate);
        assertFalse(validateExpirationDate(expiryDate, creditCard));
    }


    @Test
    public void testInvalidYearExpirationDate() throws IOException {
        String expiryDate = "05/00";
        CreditCard creditCard = getFirstCreditCard();
        creditCard.setExpiryDate(expiryDate);
        assertFalse(validateExpirationDate(expiryDate, creditCard));
    }

    private boolean validateExpirationDate(String expirationDate, CreditCard creditCard) {
        try {
            // Check if the expirationDate has a valid format (MM/YY)
            if (!expirationDate.matches("\\d{2}/\\d{2}")) {
                return false;
            }

            // Extract month and year from expirationDate
            String[] dateParts = expirationDate.split("/");
            int expMonth = Integer.parseInt(dateParts[0]);
            int expYear = 2000 + Integer.parseInt(dateParts[1]);

            // Check if the month is in the valid range (1 to 12)
            if (expMonth < 1 || expMonth > 12) {
                return false;
            }

            // Get current date
            Calendar currentDate = Calendar.getInstance();

            // Create a Calendar instance for the expiration date
            Calendar expirationDateCalendar = Calendar.getInstance();
            expirationDateCalendar.set(Calendar.YEAR, expYear);
            expirationDateCalendar.set(Calendar.MONTH, expMonth - 1); // Calendar months are zero-based

            // Check if the expiration date is in the past
            if (expirationDateCalendar.before(currentDate)) {
                return false;
            }

            // Check if the expiration date is in the future
            // Assuming we consider a maximum validity of 10 years for the card
            int maxValidityYears = 30;
            Calendar maxValidDate = (Calendar) currentDate.clone();
            maxValidDate.add(Calendar.YEAR, maxValidityYears);

            if (expirationDateCalendar.after(maxValidDate)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            // Handle any exceptions that might occur during the validation process
            e.printStackTrace();
            return false;
        }
    }


}

