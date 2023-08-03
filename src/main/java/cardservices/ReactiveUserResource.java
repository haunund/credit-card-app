package cardservices;

import cardservices.request.UpdateUser;
import entity.User;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Path("/users")
public class ReactiveUserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<User> list() {
        return User.streamAllUsers();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> addUser(User user) {
        user.creationDate = LocalDateTime.now();
        return user.<User>persist().map(u ->
                Response.created(URI.create("/users/" + u.id.toString()))
                        .entity(user).build());
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<User> update(@PathParam("id") String id, UpdateUser updateUser) {
        return User.updateUser(id, updateUser);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<User> getUser(@PathParam("id") String id) {
        return User.findById(new ObjectId(id));
    }

    @DELETE
    @Path("/{id}")
    public Uni<Void> deleteUser(@PathParam("id") String id) {
        return User.deleteUser(id);
    }

    @GET
    @Path("/search")
    public Uni<List<User>> search(@QueryParam("name") String name, @QueryParam("email") String email,
                                  @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo) {
        if (name != null) {
            return User.find("{'name': ?1, 'email': ?2}", name, email).list();
        }
        return User.find("{'creationDate': {$gte: ?1}, 'creationDate': {$lte: ?2}}",
                ZonedDateTime.parse(dateFrom).toLocalDateTime(),
                ZonedDateTime.parse(dateTo).toLocalDateTime()).list();
    }

    @GET
    @Path("/search2")
    public Uni<List<User>> searchCustomQueries(@QueryParam("names") List<String> names) {
        return User.find("{'name': {'$in': ?1}}", names).list();
    }


}
