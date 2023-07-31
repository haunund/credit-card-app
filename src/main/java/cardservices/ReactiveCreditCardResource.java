package cardservices;

import cardservices.request.UpdateCreditCard;
import entity.CreditCard;
import entity.models.CustomCreditCard;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import parsers.CsvParserService;
import parsers.JsonParserService;
import parsers.XmlParserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Path("/creditcards")
@ApplicationScoped
public class ReactiveCreditCardResource {
@Inject
UpdateCreditCard  updateCreditCard;

@Inject
JsonParserService jsonParserService;


@Inject
XmlParserService xmlParserService;

@Inject
CsvParserService csvParserService;

@Inject
CreditCard creditCard;

    //Get all credit cards
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<CreditCard>> list() {
        return CreditCard.listAll();
    }

    @GET
    @Path("/latest")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<CustomCreditCard>> getList() {
        return CustomCreditCard.listAll();
    }

    //Create credit cards
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> addCreditCard(CreditCard creditCard) {
        creditCard.creationDate = LocalDateTime.now();
        return creditCard.<CreditCard>persist().map(v ->
                Response.created(URI.create("/creditcards/" + v.id.toString()))
                        .entity(creditCard).build()
        );

        //creditCard.updateCreditCard( creditCard.id.toString(),creditCard );
    }

    //Process credit cards
    //This will read from the json/csv/xml file provided and will add the records tpo database

    @Path("/process")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public  Response processCreditCards(CreditCard creditCard) throws IOException {

        Multi<CreditCard> CreditCards = CreditCard.streamAllCreditCards();

        String creditCardsFileName = "creditcards.json";

        try {
            if (creditCardsFileName.endsWith(".csv")) {
                csvParserService.readFilename("cards/"+ creditCardsFileName);
            } else if (creditCardsFileName.endsWith(".json")) {
                jsonParserService.readFilename("cards/"+ creditCardsFileName);
            } else if (creditCardsFileName.endsWith(".xml")) {
                xmlParserService.readFilename("cards/"+ creditCardsFileName);
            } else {
                Response.ok( "error parsing file").build();
            }

        }catch(Exception e){
            System.out.print("Error while Parsing File:"+e);
        }
       return  Response.ok("Parsed file and inserted credit cards").build();
      //  return Response.ok( csvParserService.readFilename("cards/creditcards.csv")).build();
    }

    //Search credit cards by number, card holder and date created
    @GET
    @Path("/search")
    public Uni<List<CreditCard>> search(@QueryParam("number") String number, @QueryParam("cardHolder") String cardHolder, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo) {
        if (number != null) {
            return CreditCard.find("{'number': ?1,'cardHolder': ?2}", number, cardHolder).list();
        }
        return CreditCard
                .find("{'creationDate': {$gte: ?1}, 'creationDate': {$lte: ?2}}", ZonedDateTime.parse(dateFrom).toLocalDateTime(),
                        ZonedDateTime.parse(dateTo).toLocalDateTime()).list();
    }

    //Get credit cards by id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<CreditCard> getCreditCard(@PathParam("id") String id) {
        return CreditCard.findById(new ObjectId(id));
    }


    //delete a credit card by id
    @DELETE
    @Path("/{id}")
    public Uni<Void> deleteCreditCard(@PathParam("id") String id) {
        return CreditCard.deleteCreditCard(id);
    }


    // Update a credit card
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<CreditCard> update(@PathParam("id") String id, UpdateCreditCard updateCreditCard) {
        return creditCard.updateCreditCard(id, updateCreditCard);
    }

}
