package Controllers;

import Server.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminController {

    public static void listAdmin() {

        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT AdminID, Username, Password FROM Admins");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int adminID = results.getInt(1);
                String username = results.getString(2);
                String password = results.getString(3);
                System.out.print("AdminID: " + adminID + ", ");
                System.out.print("Username: " + username + ", ");
                System.out.print("Password: " + password);
                System.out.println(" ");
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }


}
