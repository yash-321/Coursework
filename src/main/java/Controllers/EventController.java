package Controllers;

import Server.Main;

import java.sql.Date;
import java.sql.PreparedStatement;

public class EventController {

    public static void createEvent(int venueID, int catererId, int entertainerID, int userID, Date date, int hours, int attendees) {

        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Events(VenueID, CatererID, EntertainerID, UserID, Date, Hours, Attendees) VALUES (?, ?, ?, ?, ?, ?,?)");

            ps.setInt(1, venueID);
            ps.setInt(2, catererId);
            ps.setInt(3, entertainerID);
            ps.setInt(4, userID);
            ps.setDate(5, date);
            ps.setInt(6, hours);
            ps.setInt(7, attendees);
            ps.executeUpdate();
            System.out.println("Record added to Events table");

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void deleteEvent(int eventID) {
        try {

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Events WHERE EventID = ?");
            ps.setInt(1, eventID);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateEvent(int eventID, int venueID, int catererID, int entertainerID, Date date, int hours, int attendees) {

        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Events SET VenueName = ?, Address = ?, City = ?, Postcode = ?, Capacity = ?, PriceHr = ? WHERE EventID = ?");

            ps.setInt(1,venueID);
            ps.setInt(2,catererID);
            ps.setInt(3,entertainerID);
            ps.setDate(4,date);
            ps.setInt(5,hours);
            ps.setInt(6,attendees);
            ps.setInt(7,eventID);

            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}
