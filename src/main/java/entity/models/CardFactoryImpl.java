package entity.models;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CardFactoryImpl implements CardFactory {
    public Card createCard(String cardNumber){

        if (cardNumber.equals("")){
            return null;
        }
        try {
            long number = Double.valueOf(cardNumber).longValue();
            String value = Long.toString(number);
            if (value.length() <= 16) {

                if (value.length() == 15) {
                    return new AmericanExpressCard(cardNumber);

                } else if (value.length() == 16 && value.charAt(0) == '5') {
                    return new MasterCard(cardNumber);

                } else if (value.length() == 16 && value.charAt(0) == '6') {
                    return new Discover(cardNumber);

                } else if ((value.length() == 13 || value.length() == 16) && value.charAt(0) == '4') {
                    return new Visa(cardNumber);

                } else {
                    return null;
                }

            }
        }catch (Exception e) {
            return null;
        }
        return null;
    }
}
