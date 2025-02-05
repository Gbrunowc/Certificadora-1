
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class connector {

    public static Connection getConnector() throws SQLException, ClassNotFoundException{
        Class.forName("org.postgresql.Driver");
        Connection cn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BFdb", "BFadmin", "admin");
        return cn;
    }
}
