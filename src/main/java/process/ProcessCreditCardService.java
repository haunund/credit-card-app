package process;

import entity.CreditCard;
import entity.models.Card;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsers.CsvParserService;
import security.AESEncryptionService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class ProcessCreditCardService {

    @Inject
    public AESEncryptionService aesEncryptionService;
    @Inject
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvParserService.class);

    // Method to process all the credit cards
    public void processCreditCards(Multi<CreditCard> creditCards) {
       // Multi<CreditCard> creditCardStream = myRestClient.processCreditCards(creditCard);

        creditCards.subscribe().with(
                creditCard -> {
                    // Process each credit card item here
                    // ...
                    creditCard.persist();
                },
                failure -> {
                    // Handle any errors or exceptions that occurred during processing
                    // ...
                },
                () -> {
                    // Handle the completion of the stream (optional)
                    // ...
                }
        );

    }

    public void processMultipleCreditCards(ArrayList<CreditCard> creditCards) {
        Multi<CreditCard> multiCreditCards = Multi.createFrom().iterable(creditCards);
        // Now you can use multiCreditCards as a reactive stream for processing
        multiCreditCards
                .onItem().transformToUniAndConcatenate(this::processSingleCreditCard)

                .subscribe().with(savedCreditCards -> {
                    // Handle the results if needed

                });
    }
    public Uni<String> processSingleCreditCard(CreditCard creditCard) {
        // Your processing logic for a single CreditCard
        // This method returns a Uni<String> for demonstration purposes
        creditCard._id = UUID.randomUUID().toString();
        Uni<ReactivePanacheMongoEntityBase> resultUni = creditCard.persist();
      //  Uni.createFrom();

        resultUni.subscribe().with(savedCreditCard -> {
            // Handle the result if needed
            System.out.println("Saved CreditCard: " );
        });
        return Uni.createFrom().item("Processed: " + creditCard.getCardNumber());
    }


    public void saveCreditCard(CreditCard creditCard) throws Exception {

        CreditCard creditCardEntity = new CreditCard();
        creditCardEntity.setId(new ObjectId());
        creditCardEntity.setCardNumber(aesEncryptionService.encrypt(creditCard.getCardNumber()));
        creditCardEntity.setCardHolderName(creditCard.getCardHolderName());
        creditCardEntity.setExpiryDate(creditCard.getExpiryDate());
        creditCardEntity.setCreationDate(LocalDateTime.now()) ;

        Uni.createFrom().item(creditCardEntity);
        Uni<String> resultUni = this.processSingleCreditCard((CreditCard) creditCardEntity);

        // Now you need to subscribe to the Uni to trigger the processing and persistence
        resultUni.subscribe().with(savedCreditCard -> {
            // Handle the result if needed
            LOGGER.debug("Saved CreditCard: {}", creditCard );
        });

    }




}