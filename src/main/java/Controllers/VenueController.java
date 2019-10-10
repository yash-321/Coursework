package Controllers;

import Server.Main;
import com.sun.jersey.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@Path("venue/")
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

    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteVenue(@FormDataParam("venueID") Integer venueID) {

        try{
            if (venueID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("venue/delete venueID=" + venueID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Venue WHERE VenueID = ?");
            ps.setInt(1, venueID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
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

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listVenue() {
        System.out.println("venue/list");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT VenueID, VenueName, Address, City, Postcode, Capacity, PriceHr FROM Venues");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("id", results.getInt(1));
                item.put("name", results.getString(2));
                item.put("address", results.getString(3));
                item.put("city", results.getString(4));
                item.put("postcode", results.getString(5));
                item.put("capacity", results.getInt(6));
                item.put("price/hr", results.getFloat(7));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}
