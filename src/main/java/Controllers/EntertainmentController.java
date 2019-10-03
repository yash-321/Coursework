package Controllers;

import Server.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EntertainmentController {

    public static void insertEntertainer(String entertainerName, String description, Float priceHr) {

        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Entertainment(EntertainerName, Description, PriceHr) VALUES (?, ?, ?)");

            ps.setString(1, entertainerName);
            ps.setString(2, description);
            ps.setFloat(3, priceHr);
            ps.executeUpdate();
            System.out.println("Record added to Entertainment table");

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    public static void deleteEntertainer(int entertainerID) {
        try{

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Entertainment WHERE EntertainerID = ?");
            ps.setInt(1, entertainerID);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateEntertainer(String entertainerName, String description, Float priceHr) {

        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Entertainment SET EntertainerName = ?, Description = ?, PriceHr = ? WHERE EntertainerID = ?");

            ps.setString(1, entertainerName);
            ps.setString(2, description);
            ps.setFloat(3, priceHr);

            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void listEntertainer() {

        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT EntertainerID, EntertainerName, Description, PriceHr FROM Entertainment");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int entertainerID = results.getInt(1);
                String entertainerName = results.getString(2);
                String description = results.getString(3);
                Float priceHr = results.getFloat(4);
                System.out.print("EntertainerID: " + entertainerID + ", ");
                System.out.print("Entertainer Name: " + entertainerName + ", ");
                System.out.print("Description: " + description + ", ");
                System.out.print("Price/hour: " + priceHr);
                System.out.println(" ");
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}
