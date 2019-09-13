import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VenueController {

    public static void insertVenue(String venueName, String address, String city, String postcode, int capacity, float priceHr){

        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users(VenueName, Address, City, Postcode, Capacity, PriceHr) VALUES (?, ?, ?, ?, ?, ?)");

            ps.setString(1, venueName);
            ps.setString(2, address);
            ps.setString(3, city);
            ps.setString(4, postcode);
            ps.setInt(5, capacity);
            ps.setFloat(6, priceHr);
            ps.executeUpdate();
            System.out.println("Record added to Venues table");

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    public static void deleteVenue(int venueID) {
        try{

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Venue WHERE VenueID = ?");
            ps.setInt(1, venueID);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateVenue(String venueName, String address, String city, String postcode, int capacity, float priceHr) {

        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Venues SET VenueName = ?, Address = ?, City = ?, Postcode = ?, Capacity = ?, PriceHr = ? WHERE VenueID = ?");

            ps.setString(1,venueName);
            ps.setString(2,address);
            ps.setString(3,city);
            ps.setString(4,postcode);
            ps.setInt(5,capacity);
            ps.setFloat(6,priceHr);

            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void listVenue() {

        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT VenueID, VenueName, Address, City, Postcode, Capacity, PriceHr FROM Venues");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int venueID = results.getInt(1);
                String venueName = results.getString(2);
                String address = results.getString(3);
                String city = results.getString(4);
                String postcode = results.getString(5);
                int capacity = results.getInt(6);
                Float priceHr = results.getFloat(7);
                System.out.print("VenueID: " + venueID + ", ");
                System.out.print("Venue Name: " + venueName + ", ");
                System.out.print("Address: " + address + ", ");
                System.out.print(city + ", ");
                System.out.print(postcode + ", ");
                System.out.print("Capacity: " + capacity + ", ");
                System.out.print("price/hour: " + priceHr + ", ");
                System.out.println(" ");
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}
