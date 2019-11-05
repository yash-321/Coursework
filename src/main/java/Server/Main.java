package Server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        // Database opened
        openDatabase("users.db");

        ResourceConfig config = new ResourceConfig();
        config.packages("Controllers");
        config.register(MultiPartFeature.class);
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        // Creation of the server
        Server server = new Server(8081);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(servlet, "/*");

        // Server opened in try catch to stop error crashing program
        try {
            server.start();
            System.out.println("Server successfully started.");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // database opening procedure
    public static Connection db = null;
    private static void openDatabase(String dbFile) {
        try  {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Database connection successfully established.");
        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());
        }

    }
}
