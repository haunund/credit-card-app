package parsers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import entity.CreditCard;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.smallrye.mutiny.Uni;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import process.ProcessCreditCardService;
import process.common.TechnicalRuntimeException;
import process.iterator.RecordsIterator;
import process.iterator.RecordsIteratorImpl;

import cardservices.ReactiveCreditCardResource;
import security.AESEncryptionService;

@ApplicationScoped
public class JsonParserService {
    @Inject
    ProcessCreditCardService processCreditCardService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvParserService.class);

    @Inject
    public AESEncryptionService aesEncryptionService;
    @Inject
    ReactiveCreditCardResource reactiveCreditCardResource;
    private List<String> records;
    public JsonParserService() {
        records = new ArrayList<>();
    }

   // @Override
    // Read the json file and parse the data
    public List<CreditCard> readFilename(String inputFilename) throws IOException {
        List<CreditCard> creditCards = new ArrayList<>();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(inputFilename);

        if (inputStream != null) {
            ObjectMapper objectMapper = null;
            try {
                String jsonContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

                // Use Jackson ObjectMapper to parse the JSON data into a list of CreditCard objects
                objectMapper = new ObjectMapper();
                creditCards = objectMapper.readValue(jsonContent, new TypeReference<List<CreditCard>>() {
                });
                processRecords(creditCards);
                //System.out.println(creditCards);
            } catch (Exception e) {
                // You can throw a more specific custom exception or just IOException if needed
                throw new IOException("An error occurred while parsing JSON", e);
            }  finally {
                inputStream.close();
            }
        } else {
            // Handle file not found scenario
            LOGGER.debug("File not found: {}", inputFilename );
        }

        return creditCards;
    }
    public  CreditCard parse() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream("cards/creditcards.json");

        if (jsonStream == null) {
            throw new FileNotFoundException("File not found: creditcards.json");
        }

        return objectMapper.readValue(jsonStream, CreditCard.class);
    }

    public void processRecords( List<CreditCard> creditCards){

        //   Iterator<CreditCard> iterator = JsonFileIterator.getRecordsIterator(creditCards);
        Iterator<CreditCard>   iterator = creditCards.iterator();

        while (iterator.hasNext()) {
            CreditCard creditCard = iterator.next();

            CreditCard creditCardEntity = new CreditCard();
            creditCardEntity.setCardNumber(creditCard.getCardNumber());
            creditCardEntity.setCardHolderName(creditCard.getCardHolderName());
            creditCardEntity.setExpiryDate(creditCard.getExpiryDate());
            creditCardEntity.setCreationDate(LocalDateTime.now()) ;


            Uni.createFrom().item(creditCardEntity);
            Uni<String> resultUni = processCreditCardService.processSingleCreditCard((CreditCard) creditCardEntity);

            // Now you need to subscribe to the Uni to trigger the processing and persistence
            resultUni.subscribe().with(savedCreditCard -> {

                // Handle the result if needed
                LOGGER.debug("Saved CreditCard for json parser: " );
            });

        }

    }

}