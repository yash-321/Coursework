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
    public String loginUser(@FormDataParam("email") String email, @FormDataParam("password") String password){
        try {
            if (email == null || password == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("/user/login - Attempt by " + email);

            PreparedStatement statement1 = Main.db.prepareStatement(
                    "SELECT Email, Password, SessionToken FROM Users WHERE Email = ?"
            );
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


        } catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }





    public static void insertUser(String email, String password, String postcode, String firstName, String surname){

        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users(Email, Password, Postcode, FirstName, Surname) VALUES (?, ?, ?, ?, ?)");

            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, postcode);
            ps.setString(4, firstName);
            ps.setString(5, surname);
            ps.executeUpdate();
            System.out.println("Record added to Users table");

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    public static void deleteUser(String email) {
        try{

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE email = ?");
            ps.setString(1, email);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateUser(String email, String password, String postcode, String firstName, String surname) {

        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET Email = ?, Password = ?, Postcode = ?, FirstName = ?, Surname = ? WHERE Email = ?");

            ps.setString(1,email);
            ps.setString(2,password);
            ps.setString(3,postcode);
            ps.setString(4,firstName);
            ps.setString(5,surname);

            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void listUser() {

        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT Email, Password, Postcode, FirstName, Surname FROM Users");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                String email = results.getString(1);
                String password = results.getString(2);
                String postcode = results.getString(3);
                String firstName = results.getString(4);
                String surname = results.getString(5);
                System.out.print("Email: " + email + ", ");
                System.out.print("Password: " + password + ", ");
                System.out.print("Postcode: " + postcode + ", ");
                System.out.print("First Name: " + firstName + ", ");
                System.out.print("Surname: " + surname);
                System.out.println(" ");
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

}
