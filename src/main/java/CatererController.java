import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CatererController {

    public static void insertCaterer(String catererName, String foodType, Float priceHead) {

        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Caterers(CatererName, FoodType, PriceHead) VALUES (?, ?, ?)");

            ps.setString(1, catererName);
            ps.setString(2, foodType);
            ps.setFloat(3, priceHead);
            ps.executeUpdate();
            System.out.println("Record added to Caterers table");

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    public static void deleteCaterer(int catererID) {
        try{

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Caterers WHERE CatererID = ?");
            ps.setInt(1, catererID);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateCaterer(String catererName, String foodType, Float priceHead) {

        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Caterers SET CatererName = ?, FoodType = ?, PriceHead = ? WHERE CatererID = ?");

            ps.setString(1, catererName);
            ps.setString(2, foodType);
            ps.setFloat(3, priceHead);

            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void listCaterer() {

        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT CatererID, CatererName, FoodType, PriceHead FROM Caterers");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int catererID = results.getInt(1);
                String catererName = results.getString(2);
                String foodType = results.getString(3);
                Float priceHead = results.getFloat(4);
                System.out.print("catererID: " + catererID + ", ");
                System.out.print("Caterer Name: " + catererName + ", ");
                System.out.print("Type: " + foodType + ", ");
                System.out.print("Price/head: " + priceHead);
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}
