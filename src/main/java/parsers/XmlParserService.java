package parsers;

import cardservices.ReactiveCreditCardResource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import entity.CreditCard;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import entity.models.Card;
import entity.models.CardFactory;
import entity.models.CardFactoryImpl;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import process.ProcessCreditCardService;
import process.common.TechnicalRuntimeException;
import security.AESEncryptionService;

@ApplicationScoped
public class XmlParserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlParserService.class);
    @Inject
    public AESEncryptionService aesEncryptionService;
    @Inject
    ProcessCreditCardService processCreditCardService;
    @Inject
    ReactiveCreditCardResource reactiveCreditCardResource;
    private XmlMapper xmlMapper;
    private List<String> records;

    public XmlParserService() {
        records = new ArrayList<>();
    }

    public static List<CreditCard> parseXml(InputStream inputStream) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();

        // Enable FAIL_ON_UNKNOWN_PROPERTIES to throw UnrecognizedPropertyException
        xmlMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        //try {
        return xmlMapper.readValue(inputStream, new TypeReference<List<CreditCard>>() {
        });
        /*} catch (UnrecognizedPropertyException | TechnicalRuntimeException e) {
            throw new TechnicalRuntimeException("An error occurred while parsing xmlss", e);
        } */
    }

    // @Override
    public List<String> readFile(String inputFilename) throws FileSystemException {
        try {
            records.addAll(Files.readAllLines(Paths.get(inputFilename)));

            System.out.println(records);

        } catch (Exception e) {

            // throw new FileSystemException("File not found: creditcards.json");

            e.printStackTrace();
        }

        return records;
    }

    public List<CreditCard> readFilename(String inputFilename) throws Exception {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(inputFilename);
        if (inputStream == null) {
            LOGGER.error("File not found: " + inputFilename);
            throw new FileNotFoundException("File not found: " + inputFilename);
        }

        List<CreditCard> cards = null;
        cards = parseXml(inputStream);
        for (CreditCard card : cards) {

            CardFactory cardFactory = new CardFactoryImpl();
            Card createdCard = cardFactory.createCard(card.cardNumber);

            if (Objects.nonNull(createdCard)) {
                CreditCard creditCard = new CreditCard();

                creditCard.setCardNumber(card.getCardNumber());
                creditCard.setCardHolderName(card.getCardHolderName());
                creditCard.setExpiryDate(card.getExpiryDate());
                creditCard.setCreationDate(LocalDateTime.now());

                processCreditCardService.saveCreditCard(creditCard);

            }
        }
        return cards;
    }

    public CreditCard parse() throws IOException, FileNotFoundException {

        ObjectMapper objectMapper;
        InputStream jsonStream;
        try {
            objectMapper = new ObjectMapper();
            jsonStream = this.getClass().getClassLoader().getResourceAsStream("cards/creditcards.xml");

        } catch (Exception e) {
            throw new FileNotFoundException("File not found: creditcards.xml");
        }

        return objectMapper.readValue(jsonStream, CreditCard.class);
    }

    public void processRecords(List<CreditCard> creditCards) {

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
                System.out.println("Saved CreditCard for xml parser: ");
            });
        }


    }

}