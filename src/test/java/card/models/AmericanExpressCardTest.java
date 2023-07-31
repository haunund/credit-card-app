package card.models;

import entity.models.AmericanExpressCard;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AmericanExpressCardTest {

    @Test
    @Disabled
    public void testValidAmericanExpCard() {
        AmericanExpressCard americanExpressCard = new AmericanExpressCard("378282246310005");
        boolean isValid = americanExpressCard.validate("378282246310005");
        assertTrue(isValid);
    }

    @Test
    public void testInvalidAmericanExpCardType() {
        AmericanExpressCard americanExpressCard = new AmericanExpressCard("378282246310006");
        boolean isValid = americanExpressCard.validate("378282246310006");
        assertFalse(isValid);
    }

    @Test
    public void testAmericanExpCardWithNonNumericCharacters() {
        AmericanExpressCard americanExpressCard = new AmericanExpressCard("37828224631000A");
        boolean isValid = americanExpressCard.validate("37828224631000A");
        assertFalse(isValid);
    }

    @Test
    public void testAmericanExpCardWithLessThan15Digits() {
        AmericanExpressCard americanExpressCard = new AmericanExpressCard("3782822463100");
        boolean isValid = americanExpressCard.validate("3782822463100");
        assertFalse(isValid);
    }

    @Test
    public void testAmericanExpCardWithMoreThan15Digits() {
        AmericanExpressCard americanExpressCard = new AmericanExpressCard("378282246310005123");
        boolean isValid = americanExpressCard.validate("378282246310005123");
        assertFalse(isValid);
    }

    @Test
    public void testAmericanExpCardWithNull() {
        AmericanExpressCard americanExpressCard = new AmericanExpressCard(null);
        boolean isValid = americanExpressCard.validate(null);
        assertFalse(isValid);
    }
}