package security.authentication.webauthn;

import entity.CreditCard;
import entity.models.CustomCreditCard;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/api/admin")
public class AdminResource {

    @GET
    @Path("/creditcards")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<CustomCreditCard>> getList() {
        return CreditCard.listAll();
    }


    /*
    private Uni<CreditCard> updateCreditCardFields(CreditCard originalCard) {
        // Perform your field updates here
        String updatedCardHolderName = originalCard.getCardHolderName().toUpperCase();
        String updatedCardNumber = "**** **** **** " + originalCard.getCardNumber().substring(12);
        // Update other fields as needed

        // Create a new CreditCard instance with updated fields
        CreditCard updatedCard = new CreditCard();
        updatedCard.setCardHolderName(updatedCardHolderName);
        updatedCard.setCardNumber(updatedCardNumber);
        // Set other fields

        return Uni.createFrom().item(updatedCard);
    }

     */
}
