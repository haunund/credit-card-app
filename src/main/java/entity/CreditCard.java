package entity;

import cardservices.request.UpdateCreditCard;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.Data;
import org.bson.types.ObjectId;
import security.AESEncryptionService;
import process.ProcessCreditCardService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

@Data
@ApplicationScoped

public class CreditCard extends ReactivePanacheMongoEntityBase {

    public ObjectId id;
    public String _id;
    public String cardNumber;
    public String cardHolderName;
    public String expiryDate;
    public LocalDateTime creationDate;

    @Inject
    public AESEncryptionService aesEncryptionService;

    @Inject
    public ProcessCreditCardService processCreditCardService;


    public CreditCard() {
        this._id = UUID.randomUUID().toString();

    }

    public static Uni<Void> deleteCreditCard(String creditCardId) {
        Uni<CreditCard> creditCardUni = findById(new ObjectId(creditCardId));

        return creditCardUni.call(creditCard -> {
            // In case there are other operations or checks related to CreditCard deletion, you can add them here

            return Uni.createFrom().item(creditCard);
        }).chain(creditCard -> {
            if (creditCard == null) {
                throw new NotFoundException();
            }
            return creditCard.delete();
        });
    }

    public static Multi<CreditCard> streamAllCreditCards() {
        return streamAll();
    }

    // Assuming that the credit card entity has a field "userId" to identify the user associated with the card
    public static Multi<CreditCard> streamAllCreditCardsByUserId(String userId) {
        return stream("userId", userId);
    }

    public Uni<CreditCard> updateCreditCard(String id, UpdateCreditCard updateCreditCard) {
        Uni<CreditCard> creditCardUni = findById(new ObjectId(id));
        // Add all operations  or checks related to CreditCard update here

        creditCardUni.call(creditCard -> {
            try {
                creditCard.cardNumber = aesEncryptionService.encrypt(updateCreditCard.getCardNumber());
                creditCard.cardHolderName = aesEncryptionService.encrypt(updateCreditCard.getCardHolderName());
                creditCard.expiryDate = updateCreditCard.getExpiryDate();


            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            return Uni.createFrom().item(creditCard);
        });

        creditCardUni.chain(creditCard -> {
            if (creditCard == null) {
                throw new NotFoundException();
            }
            return creditCard.persistOrUpdate();
        });
        return creditCardUni;
    }

    public void processCreditCard(Multi<CreditCard> cards) {
        processCreditCardService.processCreditCards(cards);
    }

    // You can add more methods and properties specific to CreditCard entity as needed
/*
    @Override
    public boolean equals(Object c) {
        if (c == null) return false;
        CreditCard comp = ((CreditCard) c);
        return comp.id.equals(id);
    }

 */
}
