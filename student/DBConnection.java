import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/library_management_db";
            String user = "root";
<<<<<<< HEAD
            String pass = "1106";
=======
            String pass = "ittchann@1223";
>>>>>>> 35119e73b2cb2a1e8ffe29133807c34c40cd8f86
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to database successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}