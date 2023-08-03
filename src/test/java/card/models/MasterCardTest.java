package card.models;

import entity.models.MasterCard;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MasterCardTest {

    @Test
    public void testValidMasterCard() {
        MasterCard masterCard = new MasterCard("5555555555554444");
        boolean isValid = masterCard.validate("5555555555554444");
        assertTrue(isValid);
    }

    @Test
    public void testInvalidMasterCard() {
        MasterCard masterCard = new MasterCard("5555555555554445");
        boolean isValid = masterCard.validate("5555555555554445");
        assertFalse(isValid);
    }

    @Test
    @Disabled
    public void testMasterCardWithNonNumericCharacters() {
        MasterCard masterCard = new MasterCard("5555 5555 5555 4444");
        boolean isValid = masterCard.validate("5555 5555 5555 4444");
        assertFalse(isValid);
    }

    @Test
    @Disabled
    public void testMasterCardWithLessThan16Digits() {
        MasterCard masterCard = new MasterCard("555555555555444");
        boolean isValid = masterCard.validate("555555555555444");
        assertFalse(isValid);
    }

    @Test
    public void testMasterCardWithMoreThan16Digits() {
        MasterCard masterCard = new MasterCard("55555555555544444");
        boolean isValid = masterCard.validate("55555555555544444");
        assertFalse(isValid);
    }

    @Test
    public void testMasterCardWithInvalidStartDigit() {
        MasterCard masterCard = new MasterCard("4555555555554444");
        boolean isValid = masterCard.validate("4555555555554444");
        assertFalse(isValid);
    }

    @Test
    public void testMasterCardWithInvalidSecondDigit() {
        MasterCard masterCard = new MasterCard("5556555555554444");
        boolean isValid = masterCard.validate("5556555555554444");
        assertFalse(isValid);
    }

    @Test
    public void testMasterCardWithNull() {
        MasterCard masterCard = new MasterCard(null);
        boolean isValid = masterCard.validate(null);
        assertFalse(isValid);
    }
}
