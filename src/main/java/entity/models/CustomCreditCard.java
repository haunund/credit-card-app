package entity.models;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomCreditCard extends ReactivePanacheMongoEntity {
    public String cardNumber;

}
