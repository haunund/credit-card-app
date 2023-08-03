package parsers;

import com.mongodb.client.ClientSession;
import com.mongodb.reactivestreams.client.MongoClient;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;

import cardservices.ReactiveCreditCardResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import entity.CreditCard;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import process.common.TechnicalRuntimeException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


import entity.models.*;
import entity.models.Card;

import process.*;
import security.AESEncryptionService;


@ApplicationScoped
public class CsvParserService extends PanacheMongoEntityBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvParserService.class);
    @Inject
    public AESEncryptionService aesEncryptionService;
    @Inject
    ReactiveCreditCardResource reactiveCreditCardResource;
    @Inject
    ProcessCreditCardService processCreditCardService;
    private List<String> records;

    public CsvParserService() {
        records = new ArrayList<>();
    }


    // @Override
    public List<String> readFile(String inputFilename) throws FileSystemException {
        try {
            records.addAll(Files.readAllLines(Paths.get(inputFilename)));


        } catch (Exception e) {

            throw new FileSystemException("File not found: creditcards.csv");
        }

        return records;
    }

    //Reads the file and check if it is valid
    public List<CreditCard> readFilename(String inputFilename) throws IOException, FileNotFoundException {
        List<CreditCard> creditCards = new ArrayList<>();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(inputFilename);


        if (inputStream != null) {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String[] headers = csvReader.readNext(); // Read and ignore the header row
                if (headers == null) {
                    return creditCards; // Empty file or no records
                }

                String[] record;
                //Read csv and create new Creditcard entity
                int count = 0;
                while ((record = csvReader.readNext()) != null) {
                    System.out.println(Arrays.toString(record));
                    if (record.length >= 3) {

                        CardFactory cardFactory = new CardFactoryImpl();
                        Card card = cardFactory.createCard(record[0]);

                        CreditCard creditCard = new CreditCard();

                        creditCard.setCardNumber(record[0]);
                        creditCard.setCardHolderName(record[1]);
                        creditCard.setExpiryDate(record[2]);
                        creditCard.setCreationDate(LocalDateTime.now());
                        creditCards.add(count, creditCard);
                        //Check whether the card is valid or not
                        if (Objects.nonNull(card)) {
                            processCreditCardService.saveCreditCard(creditCard);

                        }
                        count++;
                    } else {
                        // Handle invalid records with insufficient fields
                        LOGGER.debug("Invalid CreditCard: {}", record);
                    }
                }
            } catch (CsvValidationException | TechnicalRuntimeException | FileNotFoundException e) {

                throw new TechnicalRuntimeException("An error occurred while parsing csv", e);

            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                inputStream.close();
            }
        } else {
            // Handle file not found scenario
            LOGGER.debug("File not found: {}", inputFilename);
        }

        return creditCards;
    }

    public CreditCard parse() throws IOException {


        ObjectMapper objectMapper = new ObjectMapper();
        InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream("cards/creditcards.json");

        if (jsonStream == null) {
            throw new FileNotFoundException("File not found: creditcards.json");
        }

        return objectMapper.readValue(jsonStream, CreditCard.class);
    }

    public void processRecords(List<CreditCard> creditCards) {

        //   Iterator<CreditCard> iterator = JsonFileIterator.getRecordsIterator(creditCards);
        Iterator<CreditCard> iterator = creditCards.iterator();

        while (iterator.hasNext()) {
            CreditCard creditCard = iterator.next();

            CreditCard creditCardEntity = new CreditCard();
            creditCardEntity.setCardNumber(creditCard.getCardNumber());
            creditCardEntity.setCardHolderName(creditCard.getCardHolderName());
            creditCardEntity.setExpiryDate(creditCard.getExpiryDate());
            creditCardEntity.setCreationDate(LocalDateTime.now());

            Uni.createFrom().item(creditCardEntity);
            Uni<String> resultUni = processCreditCardService.processSingleCreditCard((CreditCard) creditCardEntity);

            // Now you need to subscribe to the Uni to trigger the processing and persistence
            resultUni.subscribe().with(savedCreditCard -> {

                // Handle the result if needed
                LOGGER.debug("saved credit card by csv parser: {}", savedCreditCard);

            });
        }
    }
}