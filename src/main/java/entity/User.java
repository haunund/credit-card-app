package entity;

import cardservices.request.UpdateUser;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;

public class User extends ReactivePanacheMongoEntity {

    public String firstName;
    public String lastName;
    public String email;
    public LocalDateTime creationDate;
    public List<CreditCard> creditCards;

    public static Uni<User> updateUser(String id, UpdateUser updateUser) {
        Uni<User> userUni = User.findById(new ObjectId(id));
        return userUni
                .onItem().transform(user -> {
                    user.firstName = updateUser.getFirstName();
                    user.lastName = updateUser.getLastName();
                    return user;
                }).call(user -> user.persistOrUpdate());
    }

    /*
        public static Uni<CreditCard> addUserToCreditCard(User user, String postId) {
            Uni<CreditCard> postUni = findById(new ObjectId(postId));

            return postUni.onItem().transform(user -> {

                if (user.creditcards == null) {
                    user.creditcards = List.of(user);
                } else {
                    user.creditcards.add(user);
                }
                comment.creationDate = LocalDateTime.now();
                comment.postId = postId;
                return user;
            }).call(user -> comment.persist().chain(() -> user.persistOrUpdate()));
        }
*/
    public static Uni<Void> deleteUser(String UserId) {
        Uni<User> postUni = findById(new ObjectId(UserId));
        Multi<CreditCard> commentsUni = CreditCard.streamAllCreditCardsByUserId(UserId);

        return postUni.call(user -> commentsUni.onItem().call(comment -> comment.delete())
                .collect().asList()).chain(user -> {
            if (user == null) {
                throw new NotFoundException();
            }
            return user.delete();
        });
    }

    public static Multi<User> streamAllUsers() {
        return streamAll();
    }

}
