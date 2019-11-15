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

@Path("caterer/")
public class CatererController {

    @POST
    @Path("insert")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertCaterer(@FormDataParam("name") String catererName, @FormDataParam("type") String foodType,
                                    @FormDataParam("price") Float priceHead) {

        try {
            if (catererName == null || foodType == null || priceHead == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("caterer/new name=" + catererName);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Caterers(CatererName, FoodType, PriceHead) VALUES (?, ?, ?)");

            ps.setString(1, catererName);
            ps.setString(2, foodType);
            ps.setFloat(3, priceHead);
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
    public String deleteCaterer(@FormDataParam("id") Integer catererID) {

        try{
            if (catererID == null) {
                throw new Exception("ID is missing in the HTTP request.");
            }
            System.out.println("caterer/delete id=" + catererID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Caterers WHERE CatererID = ?");
            ps.setInt(1, catererID);
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
    public String updateCaterer(@FormDataParam("id") Integer catererID, @FormDataParam("name") String catererName, @FormDataParam("type") String foodType,
                                    @FormDataParam("price") Float priceHead) {

        try {
            if (catererID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("entertainment/update id=" + catererID);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Caterers SET CatererName = ?, FoodType = ?, PriceHead = ? WHERE CatererID = ?");

            ps.setString(1, catererName);
            ps.setString(2, foodType);
            ps.setFloat(3, priceHead);
            ps.setInt(4, catererID);
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
    public String listCaterer() {
        System.out.println("caterer/list");
        JSONArray list = new JSONArray();
        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT CatererID, CatererName, FoodType, PriceHead FROM Caterers");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("id", results.getInt(1));
                item.put("name", results.getString(2));
                item.put("type", results.getString(3));
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
