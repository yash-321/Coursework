package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("user")
public class UserController {

    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUser(@FormDataParam("email") String email, @FormDataParam("password") String password) {
        try {
            if (email == null || password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("/user/login - Attempt by " + email);

            PreparedStatement statement1 = Main.db.prepareStatement("SELECT Email, Password, SessionToken FROM Users WHERE Email = ?");
            statement1.setString(1, email.toLowerCase());
            ResultSet results = statement1.executeQuery();

            if (results != null && results.next()) {
                if (!password.equals(results.getString("Password"))) {
                    return "{\"error\": \"Incorrect password\"}";
                }

                String token = UUID.randomUUID().toString();
                PreparedStatement statement2 = Main.db.prepareStatement(
                        "UPDATE Users SET SessionToken = ? WHERE LOWER(Email) = ?"
                );
                statement2.setString(1, token);
                statement2.setString(2, email.toLowerCase());
                statement2.executeUpdate();
                return "{\"token\": \"" + token + "\"}";

            } else {
                return "{\"error\": \"Can't find user account.\"}";
            }


        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }


    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertUser(
            @FormDataParam("email") String email, @FormDataParam("password") String password,
            @FormDataParam("postcode") String postcode, @FormDataParam("firstname") String firstName,
            @FormDataParam("surname") String surname) {

        try {
            if (email == null || password == null || postcode == null || firstName == null || surname == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("user/new email=" + email);

            PreparedStatement ps = Main.db.prepareStatement(
                    "INSERT INTO Users(Email, Password, Postcode, FirstName, Surname) VALUES (?, ?, ?, ?, ?)");

            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, postcode);
            ps.setString(4, firstName);
            ps.setString(5, surname);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteUser(@FormDataParam("email") String email) {

        try {
            if (email == null) {
                throw new Exception("Email is missing from HTTP request.");
            }
            System.out.println("user/delete email=" + email);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE email = ?");
            ps.setString(1, email);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUser(@FormDataParam("email") String email, @FormDataParam("password") String password,
                             @FormDataParam("postcode") String postcode, @FormDataParam("firstname") String firstName,
                             @FormDataParam("surname") String surname) {

        try {
            if (email == null || password == null || postcode == null || firstName == null || surname == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("user/update email=" + email);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET Email = ?, Password = ?, Postcode = ?, FirstName = ?, Surname = ? WHERE Email = ?");

            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, postcode);
            ps.setString(4, firstName);
            ps.setString(5, surname);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
}
