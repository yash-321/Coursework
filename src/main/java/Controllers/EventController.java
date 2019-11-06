package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.sql.PreparedStatement;

@Path("event/")
public class EventController {

    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String createEvent(@FormDataParam("venueID") Integer venueID,@FormDataParam("catererID") Integer catererID,
                              @FormDataParam("entertainerID") Integer entertainerID, @FormDataParam("email") Integer email,
                              @FormDataParam("date") Date date, @FormDataParam("hours") Integer hours,
                              @FormDataParam("people") Integer people) {

        try {
            if (venueID == null || email == null || date == null || hours == null || people == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Events(VenueID, CatererID, EntertainerID, Email, Date, Hours, People) VALUES (?, ?, ?, ?, ?, ?,?)");

            ps.setInt(1, venueID);
            ps.setInt(2, catererID);
            ps.setInt(3, entertainerID);
            ps.setInt(4, email);
            ps.setDate(5, date);
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
                              @FormDataParam("email") String email, @FormDataParam("date") Date date, @FormDataParam("hours") Integer hours,
                              @FormDataParam("people") Integer people) {

        try {
            if (eventID == null || venueID == null || email == null || date == null || hours == null || people == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("event/update id=" + eventID);


            PreparedStatement ps = Main.db.prepareStatement(
                    "UPDATE Events SET VenueID = ?, CatererID = ?, EnterertainerID = ?, Email = ?, Date = ?, Hours = ?, People = ? WHERE EventID = ?");

            ps.setInt(1,venueID);
            ps.setInt(2,catererID);
            ps.setInt(3,entertainerID);
            ps.setString(4,email);
            ps.setDate(5,date);
            ps.setInt(6,hours);
            ps.setInt(7,people);
            ps.setInt(8,eventID);
            ps.execute();
            return "{\"status\": \"OK\"}";


        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }

    }
}
