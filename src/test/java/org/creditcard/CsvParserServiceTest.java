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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
@Disabled
class CsvParserServiceTest {

    @Inject
    CsvParserService csvParserService;

    @Test
    public void testReadValidCsvFile() throws IOException {
        String inputFilename = "cards/creditcards.csv";
        List<CreditCard> creditCards = csvParserService.readFilename(inputFilename);
        assertEquals(7, creditCards.size());
        // Add more assertions if necessary based on the actual content of the XML file.
    }

    @Test
    public void testReadNonExistentCsvFile() throws IOException {
        String inputFilename = "cards/creditcards404.csv";
        List<CreditCard> creditCards = csvParserService.readFilename(inputFilename);
        assertEquals(0, creditCards.size());
        //  assertThrows(FileNotFoundException.class, () -> csvParserService.readFilename(inputFilename));
    }

    @Test
    public void testReadInvalidCsvFile() throws IOException {
        String inputFilename = "cards/creditcards-invalid.csv";
        List<CreditCard> creditCards = csvParserService.readFilename(inputFilename);
        assertEquals(0, creditCards.size());
    }
    // Add more test cases to cover different scenarios related to the CreditCard class.
}