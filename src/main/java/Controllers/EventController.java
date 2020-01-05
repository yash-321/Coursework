package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("event/")
public class EventController {

    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String createEvent(@FormDataParam("venueID") Integer venueID, @FormDataParam("catererID") Integer catererID,
                              @FormDataParam("entertainerID") Integer entertainerID, @FormDataParam("email") String email,
                              @FormDataParam("date") String date, @FormDataParam("hours") Integer hours,
                              @FormDataParam("people") Integer people) {

        try {
            if (venueID == null || email == null || date == null || hours == null || people == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Events(VenueID, CatererID, EntertainerID, Email, Date, Hours, People) VALUES (?, ?, ?, ?, ?, ?,?)");

            ps.setInt(1, venueID);
            ps.setInt(2, catererID);
            ps.setInt(3, entertainerID);
            ps.setString(4, email);
            ps.setString(5, date);
            ps.setInt(6, hours);
            ps.setInt(7, people);
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
    public String deleteEvent(@FormDataParam("eventID") Integer eventID) {
        try {
            if (eventID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("thing/delete id=" + eventID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Events WHERE EventID = ?");
            ps.setInt(1, eventID);
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
    public String updateEvent(@FormDataParam("eventID") Integer eventID, @FormDataParam("venueID") Integer venueID,
                              @FormDataParam("catererID") Integer catererID, @FormDataParam("entertainerID") Integer entertainerID,
                              @FormDataParam("email") String email, @FormDataParam("date") String date, @FormDataParam("hours") Integer hours,
                              @FormDataParam("people") Integer people) {

        try {
            if (eventID == null || venueID == null || email == null || date == null || hours == null || people == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("event/update id=" + eventID);


            PreparedStatement ps = Main.db.prepareStatement(
                    "UPDATE Events SET VenueID = ?, CatererID = ?, EnterertainerID = ?, Email = ?, Date = ?, Hours = ?, People = ? WHERE EventID = ?");

            ps.setInt(1, venueID);
            ps.setInt(2, catererID);
            ps.setInt(3, entertainerID);
            ps.setString(4, email);
            ps.setString(5, date);
            ps.setInt(6, hours);
            ps.setInt(7, people);
            ps.setInt(8, eventID);
            ps.execute();
            return "{\"status\": \"OK\"}";


        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }

    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listEvent(@CookieParam("sessionToken") String sessionCookie) {


        if (UserController.validateSessionCookie(sessionCookie) == true){
            JSONArray list = new JSONArray();
            try {
                PreparedStatement statement = Main.db.prepareStatement("SELECT Email FROM Users WHERE SessionToken = ?");
                statement.setString(1, sessionCookie);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    String currentUser = ("Email").toLowerCase();
                    PreparedStatement ps = Main.db.prepareStatement(
                            "SELECT EventID, VenueID, CatererID, EntertainerID, Date, Hours, People FROM Events WHERE Email = ?");
                    ps.setString(1,currentUser);

                    ResultSet results1 = ps.executeQuery();
                    while (results1.next()) {
                        JSONObject item = new JSONObject();
                        item.put("eventID", results1.getInt(1));
                        item.put("VenueID", results1.getString(2));
                        item.put("CatererID", results1.getString(3));
                        item.put("EntertainerID", results1.getString(4));
                        item.put("date", results1.getInt(5));
                        item.put("hours", results1.getFloat(6));
                        item.put("people", results1.getFloat(7));
                        list.add(item);
                    }
                    return list.toString();
                } else {
                    System.out.println("Error: Invalid user session token");
                    return "{\"error\": \"Invalid user session token\"}";
                }
            } catch (Exception e) {
                System.out.println("Database error: " + e.getMessage());
                return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
            }
        }else {
            System.out.println("Error: Invalid user session token");
            return "{\"error\": \"Invalid user session token\"}";
        }
    }
}
