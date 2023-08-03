package org.creditcard;

import entity.CreditCard;
import entity.models.Card;
import entity.models.CardFactory;
import entity.models.CardFactoryImpl;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import parsers.CsvParserService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CreditcardTest {

    @Inject
    CardFactory cardFactory;

    @Inject
    CsvParserService csvParserService;

    @Test
    public void testValidCreditCardNumber() {
        String cardNumber = "4532731354562998";
        Card card = cardFactory.createCard(cardNumber);
        assertNotNull(card);
        assertEquals(cardNumber, card.getCardNumber());
    }

    //  @Test
    public void testInvalidCreditCardNumber() {
        String cardNumber = "4539148803436466";
        Card card = cardFactory.createCard(cardNumber);
        assertNull(card);
    }

    @Test
    public void testCreditCardNumberWithSpaces() {
        String cardNumber = "4532 7313 5456 2998";
        Card card = cardFactory.createCard(cardNumber);
        assertNotNull(card);
        assertEquals("4532731354562998", card.getCardNumber());
    }

    @Test
    public void testCreditCardNumberWithDashes() {
        String cardNumber = "4532-7313-5456-2998";
        Card card = cardFactory.createCard(cardNumber);
        assertNotNull(card);
        assertEquals("4532731354562998", card.getCardNumber());
    }

    @Test
    public void testCreditCardNumberWithSpacesAndDashes() {
        String cardNumber = "4532-7313 5456 2998";
        Card card = cardFactory.createCard(cardNumber);
        assertNotNull(card);
        assertEquals("4532731354562998", card.getCardNumber());
    }

    @Test
    public void testCreditCardNumberWithLetters() {
        String cardNumber = "4532-7313-abcd-2998";
        Card card = cardFactory.createCard(cardNumber);
        assertNull(card);
    }

    @Test
    public void testCreditCardNumberWithSpecialCharacters() {
        String cardNumber = "4532-7313-#%&*-2998";
        Card card = cardFactory.createCard(cardNumber);
        assertNull(card);
    }

    @Test
    public void testCreditCardNumberWithLessThan13Digits() {
        String cardNumber = "4532-7313-5456";
        Card card = cardFactory.createCard(cardNumber);
        assertNull(card);
    }

    @Test
    public void testCreditCardNumberWithMoreThan16Digits() {
        String cardNumber = "4532-7313-5456-29981";
        Card card = cardFactory.createCard(cardNumber);
        assertNull(card);
    }

    @Test
    public void testCreditCardNumberWithInvalidChecksum() {
        String cardNumber = "4532-7313-5456-2990";
        Card card = cardFactory.createCard(cardNumber);

        assertNotNull(card);
    }

}