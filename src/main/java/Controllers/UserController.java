package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("user/")
public class UserController {

    @POST
    @Path("login")
    // input and output data types
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // parameters
    public String loginUser(@FormDataParam("email") String email, @FormDataParam("password") String password) {
        try {
            // parameter checker
            if (email == null || password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("/user/login - Attempt by " + email);
            // first prepared statement to check user details
            PreparedStatement statement1 = Main.db.prepareStatement(
                    "SELECT Email, Password, SessionToken FROM Users WHERE Email = ?");
            statement1.setString(1, email.toLowerCase());
            ResultSet results = statement1.executeQuery();
            // checks if email is valid
            if (results != null && results.next()) {
                // checks if password matches
                if (!password.equals(results.getString("Password"))) {
                    return "{\"error\": \"Incorrect password\"}";
                }
                // session token creator
                String token = UUID.randomUUID().toString();
                // adds token to database
                PreparedStatement statement2 = Main.db.prepareStatement(
                        "UPDATE Users SET SessionToken = ? WHERE LOWER(Email) = ?");
                statement2.setString(1, token);
                statement2.setString(2, email.toLowerCase());
                statement2.executeUpdate();
                JSONObject item = new JSONObject();
                // third prepared statement to return user details
                PreparedStatement statement3 = Main.db.prepareStatement(
                        "SELECT Email, FirstName, SessionToken FROM Users WHERE SessionToken = ?");
                statement3.setString(1,token);
                ResultSet a = statement3.executeQuery();
                if (a.next()){
                    item.put("email",a.getString(1));
                    item.put("firstname",a.getString(2));
                    item.put("token",a.getString(3));
                }
                // returns session token and user's name
                return item.toString();

            } else {
                // returns error if can't find email in database
                return "{\"error\": \"Can't find user account.\"}";
            }
            // catches error to stop crash
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

    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser(@CookieParam("token") String token) {
        if (!UserController.validateSessionCookie(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement(
                    "SELECT Email, Postcode, FirstName, Surname FROM Users WHERE SessionToken = ?");
            ps.setString(1, token);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                item.put("email", results.getString(1));
                item.put("postcode", results.getString(2));
                item.put("firstname", results.getString(3));
                item.put("surname", results.getString(4));
                System.out.println("user/get/" + ("email"));
            }
            return item.toString();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteUser(@FormDataParam("email") String email, @CookieParam("token") String sessionCookie) {
        try{
            if (sessionCookie == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("user/delete session token=" + sessionCookie);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE SessionToken = ?");
            ps.setString(1, sessionCookie);
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
    public String updateUser(@FormDataParam("email") String email, @FormDataParam("postcode") String postcode,
                             @FormDataParam("firstname") String firstName,  @FormDataParam("surname") String surname) {

        try {
            if (email == null || firstName == null || surname == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("user/update email=" + email.toLowerCase());

            PreparedStatement ps = Main.db.prepareStatement(
                    "UPDATE Users SET Postcode = ?, FirstName = ?, Surname = ? WHERE Email = ?");

            ps.setString(1, postcode);
            ps.setString(2, firstName);
            ps.setString(3, surname);
            ps.setString(4, email);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    public static boolean validateSessionCookie(String token) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Email FROM Users WHERE SessionToken = ?");
            ps.setString(1, token);
            ResultSet logoutResults = ps.executeQuery();
            return true;
        } catch (Exception exception) {
            System.out.println("Database error validating session token: " + exception.getMessage());
            return false;
        }
    }


    @GET
    @Path("check")
    @Produces(MediaType.APPLICATION_JSON)
    // gets parameter from cookies of browser
    public String checkLogin(@CookieParam("sessionToken") String sessionCookie){

        try {
            System.out.println("/user/check - Checking user against database");
            // checks if session cookie is valid
            if (validateSessionCookie(sessionCookie) == true) {
                PreparedStatement statement = Main.db.prepareStatement("SELECT Email FROM Users WHERE SessionToken = ?");
                statement.setString(1, sessionCookie);
                ResultSet results = statement.executeQuery();
                // if they match email of active user returned
                if (results != null && results.next()) {
                    String currentUser = ("Email").toLowerCase();
                    return "{\"email\": \"" + currentUser + "\"}";
                    // if it fails error returned
                } else {
                    System.out.println("Error: Invalid user session token");
                    return "{\"error\": \"Invalid user session token\"}";
                }
            }else {
                System.out.println("Error: Invalid user session token");
                return "{\"error\": \"Invalid user session token\"}";
            }
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("logout")
    // gets parameter from cookies of browser
    public String logout(@CookieParam("token") String token) {

        System.out.println("/user/logout - Logging out user");

        try {
            // removes session token from database
            PreparedStatement statement = Main.db.prepareStatement("Update Users SET SessionToken = NULL WHERE SessionToken = ?");
            statement.setString(1, token);
            statement.executeUpdate();
            return "{\"status\": \"OK\"}";
        } catch (Exception resultsException) {
            // returns error if something goes wrong
            String error = "Database error - can't update 'Users' table: " + resultsException.getMessage();
            System.out.println(error);
            return "{\"error\": \"Unable to log user out.\"}";
        }

    }
}
