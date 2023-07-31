package process;

import entity.CreditCard;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.UUID;

@ApplicationScoped
public class ProcessCreditCardService {

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



}