package card.models;

import entity.models.Discover;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DiscoverTest {

    @Test
    public void testValidDiscoverCard() {
        Discover discover = new Discover("6011111111111117");
        boolean isValid = discover.validate("6011111111111117");
        assertTrue(isValid);
    }

    @Test
    public void testInvalidDiscoverCard() {
        Discover discover = new Discover("6011056413549024");
        boolean isValid = discover.validate("6011056413549024");
        assertFalse(isValid);
    }

    @Test
    public void testDiscoverCardWithNonNumericCharacters() {
        Discover discover = new Discover("6011 0564 1354 9023");
        boolean isValid = discover.validate("6011 0564 1354 9023");
        assertFalse(isValid);
    }

    @Test
    public void testDiscoverCardWithLessThan16Digits() {
        Discover discover = new Discover("601105641354902");
        boolean isValid = discover.validate("601105641354902");
        assertFalse(isValid);
    }

    @Test
    public void testDiscoverCardWithMoreThan16Digits() {
        Discover discover = new Discover("60110564135490234");
        boolean isValid = discover.validate("60110564135490234");
        assertFalse(isValid);
    }

    @Test
    public void testDiscoverCardWithNull() {
        Discover discover = new Discover(null);
        boolean isValid = discover.validate(null);
        assertFalse(isValid);
    }
}