package org.creditcard;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import entity.CreditCard;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import parsers.CsvParserService;
import parsers.XmlParserService;
import process.common.TechnicalRuntimeException;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class XmlParserServiceTest {

    @Inject
    XmlParserService xmlParserService;

    //  @Test
    public void testReadValidXmlFile() throws Exception {
        String inputFilename = "cards/creditcards.xml";
        List<CreditCard> creditCards = xmlParserService.readFilename(inputFilename);
        assertEquals(7, creditCards.size());
        // Add more assertions if necessary based on the actual content of the XML file.
    }


    public void testReadNonExistentXmlFile() throws IOException {
        String inputFilename = "cards/creditcards404.xml";
        assertThrows(FileNotFoundException.class, () -> xmlParserService.readFilename(inputFilename));
    }

    //  @Test
    public void testReadInvalidXmlFile() throws Exception {
        String inputFilename = "cards/creditcards-invalid.xml";
        List<CreditCard> creditCards = xmlParserService.readFilename(inputFilename);

        assertNull(creditCards);
        assertThrows(UnrecognizedPropertyException.class, () -> xmlParserService.readFilename(inputFilename));
    }

    // Add more test cases to cover different scenarios related to the CreditCard class.
}