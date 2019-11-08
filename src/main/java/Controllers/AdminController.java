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



}
