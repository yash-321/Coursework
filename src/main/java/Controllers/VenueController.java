package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("venue/")
public class VenueController {

    @POST
    @Path("insert")
    //Sets what API can receive through HTTP request
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //Determines what API returns (JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertThing(
            //Receiving HTTP request parameters
            @FormDataParam("venueName") String venueName, @FormDataParam("address") String address,
            @FormDataParam("city") String city, @FormDataParam("postcode") String postcode,
            @FormDataParam("capacity") Integer capacity, @FormDataParam("priceHr") Float priceHr) {
        try {
            // Checking parameters
            if (venueName == null || address == null || city == null || postcode == null || capacity == null || priceHr == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            // Adding new venue to database
            PreparedStatement ps = Main.db.prepareStatement(
                    "INSERT INTO Venues(VenueName, Address, City, Postcode, Capacity, PriceHr) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, venueName);
            ps.setString(2, address);
            ps.setString(3, city);
            ps.setString(4, postcode);
            ps.setInt(5, capacity);
            ps.setFloat(6, priceHr);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";
            // Checks for error
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    @GET
    // path involves parameter
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    // extracting parameter from API path
    public String getVenue(@PathParam("id") Integer venueID) throws Exception {
        if (venueID == null) {
            throw new Exception("Venue's 'id' is missing in the HTTP request's URL.");
        }
        System.out.println("venue/get/" + venueID);
        // creates object to put details in
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement(
                    "SELECT VenueID, VenueName, Address, City, Postcode, Description, Capacity, PriceHr, Image FROM Venues WHERE VenueID = ?");
            ps.setInt(1, venueID);
            // SQL statement executed
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                // each value is put in object
                item.put("id", results.getInt(1));
                item.put("name", results.getString(2));
                item.put("address", results.getString(3));
                item.put("city", results.getString(4));
                item.put("postcode", results.getString(5));
                item.put("description", results.getString(6));
                item.put("capacity", results.getInt(7));
                item.put("priceHr", results.getFloat(8));
                item.put("image", results.getString(9));
            }
            // object is returned as a string
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
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

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Venues WHERE VenueID = ?");
            ps.setInt(1, venueID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateVenue(
            @FormDataParam("venueID") Integer venueID, @FormDataParam("venueName") String venueName, @FormDataParam("address") String address,
            @FormDataParam("city") String city, @FormDataParam("postcode") String postcode, @FormDataParam("capacity") Integer capacity,
            @FormDataParam("priceHr") Float priceHr) {
        try {
            if (venueID == null || venueName == null || address == null || city == null || postcode == null || capacity == null || priceHr == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("thing/update id=" + venueID);

            PreparedStatement ps = Main.db.prepareStatement(
                    "UPDATE Venues SET VenueName = ?, Address = ?, City = ?, Postcode = ?, Capacity = ?, PriceHr = ? WHERE VenueID = ?");

            ps.setString(1,venueName);
            ps.setString(2,address);
            ps.setString(3,city);
            ps.setString(4,postcode);
            ps.setInt(5,capacity);
            ps.setFloat(6,priceHr);
            ps.setInt(7,venueID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }


    @GET
    // This is the path that should be used to request this API
    @Path("list")
    // Returns in JSON
    @Produces(MediaType.APPLICATION_JSON)
    public String listVenue() {
        System.out.println("venue/list");
        // Creates JSON list to return information
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT VenueID, VenueName, Address, City, Postcode, Capacity, PriceHr, Image FROM Venues");

            ResultSet results = ps.executeQuery();
            // Object created for each Venue
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("id", results.getInt(1));
                item.put("name", results.getString(2));
                item.put("address", results.getString(3));
                item.put("city", results.getString(4));
                item.put("postcode", results.getString(5));
                item.put("capacity", results.getInt(6));
                item.put("priceHr", results.getFloat(7));
                item.put("image", results.getString(8));
                // Object added to response list
                list.add(item);
            }
            // Changes datatype of list to string then returns it
            return list.toString();
        } catch (Exception exception) {
            // Returns error message and prints error message in console
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}
