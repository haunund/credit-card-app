package org.creditcard;

import entity.CreditCard;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import parsers.JsonParserService;
import parsers.XmlParserService;
import process.common.TechnicalRuntimeException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
@Disabled
class JsonParserServiceTest {

    @Inject
    JsonParserService jsonParserService;

    @Test
    public void testReadValidJsonFile() throws IOException {
        String inputFilename = "cards/creditcards.json";
        List<CreditCard> creditCards = jsonParserService.readFilename(inputFilename);
        assertEquals(7, creditCards.size());
        // Add more assertions if necessary based on the actual content of the XML file.
    }

    @Test
    public void testReadNonExistentJsonFile() throws IOException {
        String inputFilename = "cards/credit_cards404.json";
        List<CreditCard> creditCards = jsonParserService.readFilename(inputFilename);
        assertEquals(0, creditCards.size());
    }
    @Test
    public void testReadInvalidJsonFile() throws IOException {
        String inputFilename = "cards/creditcards-invalid.json";
        assertThrows(IOException.class, () -> jsonParserService.readFilename(inputFilename));
    }
    // Add more test cases to cover different scenarios related to the CreditCard class.
}