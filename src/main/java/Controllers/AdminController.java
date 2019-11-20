package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("admin/")
public class AdminController {

    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUser(@FormDataParam("username") String username, @FormDataParam("password") String password) {
        try {
            if (username == null || password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("/admin/login - Attempt by " + username);

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT Username, Password, SessionToken FROM Admins WHERE Username = ?");
            ps1.setString(1, username);
            ResultSet results = ps1.executeQuery();

            if (results != null && results.next()) {
                if (!password.equals(results.getString("Password"))) {
                    return "{\"error\": \"Incorrect password\"}";
                }

                String token = UUID.randomUUID().toString();
                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Admins SET SessionToken = ? WHERE Username = ?");
                ps2.setString(1, token);
                ps2.setString(2, username);
                ps2.executeUpdate();
                return "{\"token\": \"" + token + "\"}";

            } else {
                return "{\"error\": \"Can't find user account.\"}";
            }


        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    public static String validateSessionCookie(String token) {
        try {
            PreparedStatement statement = Main.db.prepareStatement("SELECT Username FROM Admins WHERE SessionToken = ?");
            statement.setString(1, token);
            ResultSet results = statement.executeQuery();
            if (results != null && results.next()) {
                return results.getString("Username");
            }
        } catch (Exception resultsException) {
            String error = "Database error - can't select by email from 'Admins' table: " + resultsException.getMessage();

            System.out.println(error);
        }
        return null;
    }

    @GET
    @Path("check")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkLogin(@CookieParam("sessionToken") String sessionCookie) {

        System.out.println("/admin/check - Checking admin against database");

        String currentAdmin = validateSessionCookie(sessionCookie);

        if (currentAdmin == null) {
            System.out.println("Error: Invalid admin session token");
            return "{\"error\": \"Invalid admin session token\"}";
        } else {
            return "{\"username\": \"" + currentAdmin + "\"}";
        }
    }

    @POST
    @Path("logout")
    public void logout(@CookieParam("sessionToken") String token) {

        System.out.println("/admin/logout - Logging out admin");

        try {
            PreparedStatement statement = Main.db.prepareStatement("Update Admins SET SessionToken = NULL WHERE SessionToken = ?");
            statement.setString(1, token);
            statement.executeUpdate();
        } catch (Exception resultsException) {
            String error = "Database error - can't update 'Admins' table: " + resultsException.getMessage();
            System.out.println(error);
        }

    }

}
