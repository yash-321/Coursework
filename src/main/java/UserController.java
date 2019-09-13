import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserController {


    public static void insertUser(String email, String password, String postcode, String firstName, String surname){

        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users(Email, Password, Postcode, FirstName, Surname) VALUES (?, ?, ?, ?, ?)");

            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, postcode);
            ps.setString(4, firstName);
            ps.setString(5, surname);
            ps.executeUpdate();
            System.out.println("Record added to Users table");

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    public static void deleteUser(int userID) {
        try{

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            ps.setInt(1, userID);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateUser(String email, String password, String postcode, String firstName, String surname) {

        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET Email = ?, Password = ?, Postcode = ?, FirstName = ?, Surname = ? WHERE UserID = ?");

            ps.setString(1,email);
            ps.setString(2,password);
            ps.setString(3,postcode);
            ps.setString(4,firstName);
            ps.setString(5,surname);

            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void listUser() {

        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Email, Password, Postcode, FirstName, Surname FROM Users");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int userID = results.getInt(1);
                String email = results.getString(2);
                String password = results.getString(3);
                String postcode = results.getString(4);
                String firstName = results.getString(5);
                String surname = results.getString(6);
                System.out.print("UserID: " + userID + ", ");
                System.out.print("Email: " + email + ", ");
                System.out.print("Password: " + password + ", ");
                System.out.print("Postcode: " + postcode + ", ");
                System.out.print("First Name: " + firstName + ", ");
                System.out.print("Surname: " + surname + ", ");
                System.out.println(" ");
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

}
