package card.models;

import entity.models.Visa;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class VisaTest {

    @Test
    public void testValidVisaCard() {
        Visa visa = new Visa("4222222222222");
        boolean isValid = visa.validate("4222222222222");
        assertTrue(isValid);
    }

    // This case is handled by the code, the validate method already remmoves the spaces
    @Test
    public void testInvalidVisaCard() {
        Visa visa = new Visa("4111111111111112");
        boolean isValid = visa.validate("4111111111111112");
        assertFalse(isValid);
    }

    @Test
    @Disabled
    public void testVisaCardWithNonNumericCharacters() {
        Visa visa = new Visa("4111 1111 1111 1111");
        boolean isValid = visa.validate("4111 1111 1111 1111");
        assertTrue(isValid);
    }

    @Test
    public void testVisaCardWithLessThan13Digits() {
        Visa visa = new Visa("411111111111");
        boolean isValid = visa.validate("411111111111");
        assertFalse(isValid);
    }

    @Test
    public void testVisaCardWithMoreThan16Digits() {
        Visa visa = new Visa("41111111111111111");
        boolean isValid = visa.validate("41111111111111111");
        assertFalse(isValid);
    }

    @Test
    public void testVisaCardWithInvalidStartDigit() {
        Visa visa = new Visa("5111111111111111");
        boolean isValid = visa.validate("5111111111111111");
        assertFalse(isValid);
    }

    @Test
    public void testVisaCardWithNull() {
        Visa visa = new Visa(null);
        boolean isValid = visa.validate(null);
        assertFalse(isValid);
    }
}