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
}
