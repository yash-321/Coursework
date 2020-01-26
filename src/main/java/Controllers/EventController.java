package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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

    // POST as data is changing
    @POST
    @Path("delete")
    // Inputs and outputs for API
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteEvent(@FormDataParam("eventID") Integer eventID) {
        try {
            // Valid input check
            if (eventID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("thing/delete id=" + eventID);
            // SQL statement executed
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Events WHERE EventID = ?");
            ps.setInt(1, eventID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";
            // Stops error crash
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }

    }

    @POST
    @Path("update")
    // Input and output datatype
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // Input parameters
    public String updateEvent(@FormDataParam("eventID") Integer eventID, @FormDataParam("venueID") Integer venueID,
                              @FormDataParam("catererID") Integer catererID, @FormDataParam("entertainerID") Integer entertainerID,
                              @FormDataParam("email") String email, @FormDataParam("date") String date,
                              @FormDataParam("hours") Integer hours, @FormDataParam("people") Integer people) {

        try {
            // Parameter validation
            if (eventID == null || venueID == null || email == null || date == null || hours == null || people == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("event/update id=" + eventID + " for user " + email);

            // SQL statement execution
            PreparedStatement ps = Main.db.prepareStatement(
                    "UPDATE Events SET VenueID = ?, CatererID = ?, EntertainerID = ?, Date = ?, Hours = ?, People = ? WHERE EventID = ?");

            ps.setInt(1, venueID);
            ps.setInt(2, catererID);
            ps.setInt(3, entertainerID);
            ps.setString(4, date);
            ps.setInt(5, hours);
            ps.setInt(6, people);
            ps.setInt(7, eventID);
            ps.execute();
            return "{\"status\": \"OK\"}";

            // Stopping error crashing program
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }

    }

    @GET
    @Path("list/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String listEvent(@PathParam("email") String email) throws Exception {
        if (email == null) {
            throw new Exception("User's 'email' is missing in the HTTP request's URL.");
        }
        System.out.println("event/list for " + email);

        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement(
                    "SELECT EventID, VenueID, CatererID, EntertainerID, Date, Hours, People FROM Events WHERE Email = ?");
            ps.setString(1, email);
            // SQL statement executed
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                // each value is put in object
                JSONObject item = new JSONObject();
                item.put("eventID", results.getInt(1));
                item.put("venueID", results.getInt(2));
                item.put("catererID", results.getInt(3));
                item.put("entertainerID", results.getInt(4));
                item.put("date", results.getString(5));
                item.put("hours", results.getInt(6));
                item.put("people", results.getInt(7));
                list.add(item);
            }
            // object is returned as a string
            return list.toString();
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }


    @GET
    // path involves parameter
    @Path("get/{eventID}")
    @Produces(MediaType.APPLICATION_JSON)
    // extracting parameter from API path
    public String getVenue(@PathParam("eventID") Integer eventID) throws Exception {
        if (eventID == null) {
            throw new Exception("Venue's 'id' is missing in the HTTP request's URL.");
        }
        System.out.println("event/get/" + eventID);
        // creates object to put details in
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement(
                    "SELECT EventID, VenueID, CatererID, EntertainerID, Date, Hours, People FROM Events WHERE EventID = ?");
            ps.setInt(1, eventID);
            // SQL statement executed
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                // each value is put in object
                item.put("eventID", results.getInt(1));
                item.put("venueID", results.getInt(2));
                item.put("catererID", results.getInt(3));
                item.put("entertainerID", results.getInt(4));
                item.put("date", results.getString(5));
                item.put("hours", results.getInt(6));
                item.put("people", results.getInt(7));
            }
            // object is returned as a string
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }
}
