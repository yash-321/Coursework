package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("user/")
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
            if (email == null || password == null || firstName == null || surname == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("user/new email=" + email);

            PreparedStatement ps = Main.db.prepareStatement(
                    "INSERT INTO Users(Email, Password, Postcode, FirstName, Surname) VALUES (?, ?, ?, ?, ?)");

            ps.setString(1, email.toLowerCase());
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
    public String deleteUser(@CookieParam("sessionToken") String sessionCookie) {

        System.out.println("/user/check - Checking user against database");

        String currentUser = UserController.validateSessionCookie(sessionCookie);

        if (currentUser == null) {
            System.out.println("Error: Invalid user session token");
            return "{\"error\": \"Invalid user session token\"}";
        } else {

            try {
                if (currentUser == null) {
                    throw new Exception("Email is missing from HTTP request.");
                }
                System.out.println("user/delete email=" + currentUser);

                PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE Email = ?");
                ps.setString(1, currentUser.toLowerCase());
                ps.executeUpdate();
                return "{\"status\": \"OK\"}";

            } catch (Exception e) {
                System.out.println("Database error: " + e.getMessage());
                return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
            }
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
            System.out.println("user/update email=" + email.toLowerCase());

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET Email = ?, Password = ?, Postcode = ?, FirstName = ?, Surname = ? WHERE Email = ?");

            ps.setString(1, email.toLowerCase());
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

    public static String validateSessionCookie(String token) {
        try {
            PreparedStatement statement = Main.db.prepareStatement("SELECT Email FROM Users WHERE SessionToken = ?");
            statement.setString(1, token);
            ResultSet results = statement.executeQuery();
            if (results != null && results.next()) {
                return results.getString("Email").toLowerCase();
            }
        } catch (Exception resultsException) {
            String error = "Database error - can't select by email from 'Users' table: " + resultsException.getMessage();

            System.out.println(error);
        }
        return null;
    }

    @GET
    @Path("check")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkLogin(@CookieParam("sessionToken") String sessionCookie) {

        System.out.println("/user/check - Checking user against database");

        String currentUser = validateSessionCookie(sessionCookie);

        if (currentUser == null) {
            System.out.println("Error: Invalid user session token");
            return "{\"error\": \"Invalid user session token\"}";
        } else {
            return "{\"email\": \"" + currentUser + "\"}";
        }
    }

    @POST
    @Path("logout")
    public void logout(@CookieParam("sessionToken") String token) {

        System.out.println("/user/logout - Logging out user");

        try {
            PreparedStatement statement = Main.db.prepareStatement("Update Users SET SessionToken = NULL WHERE SessionToken = ?");
            statement.setString(1, token);
            statement.executeUpdate();
        } catch (Exception resultsException) {
            String error = "Database error - can't update 'Users' table: " + resultsException.getMessage();
            System.out.println(error);
        }

    }
}
