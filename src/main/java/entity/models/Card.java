package entity.models;
import javax.enterprise.context.ApplicationScoped;


public abstract class Card {

    public String cardNumber;

    public Card(String cardNumber){
        this.cardNumber = cardNumber;
    }

    protected abstract boolean validate(String cardNumber);

}
