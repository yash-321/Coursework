package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("entertainment/")
public class EntertainmentController {

    @POST
    @Path("insert")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertEntertainer(@FormDataParam("name") String entertainerName, @FormDataParam("description") String description,
                                    @FormDataParam("price") Float priceHr) {

        try {
            if (entertainerName == null || description == null || priceHr == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("entertainment/new name=" + entertainerName);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Entertainment(EntertainerName, Description, PriceHr) VALUES (?, ?, ?)");

            ps.setString(1, entertainerName);
            ps.setString(2, description);
            ps.setFloat(3, priceHr);
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
    public String deleteEntertainer(@FormDataParam("id") Integer entertainerID) {

        try{
            if (entertainerID == null) {
                throw new Exception("ID is missing in the HTTP request.");
            }
            System.out.println("entertainment/delete id=" + entertainerID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Entertainment WHERE EntertainerID = ?");
            ps.setInt(1, entertainerID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }


    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateEntertainer(@FormDataParam("id") Integer entertainerID, @FormDataParam("name") String entertainerName, @FormDataParam("description") String description,
                                    @FormDataParam("price") Float priceHr) {

        try {
            if (entertainerID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("entertainment/update id=" + entertainerID);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Entertainment SET EntertainerName = ?, Description = ?, PriceHr = ? WHERE EntertainerID = ?");

            ps.setString(1, entertainerName);
            ps.setString(2, description);
            ps.setFloat(3, priceHr);
            ps.setInt(4, entertainerID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("list")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String listEntertainer() {
        System.out.println("venue/list");
        JSONArray list = new JSONArray();
        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT EntertainerID, EntertainerName, Description, PriceHr FROM Entertainment");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("id", results.getInt(1));
                item.put("name", results.getString(2));
                item.put("description", results.getString(3));
                item.put("price", results.getFloat(4));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}
