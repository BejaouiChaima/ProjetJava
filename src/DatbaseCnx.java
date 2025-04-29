import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatbaseCnx {

    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/salledecinerma";
            String user = "root";
            String password = "";
            System.out.println("Connexion etablie");
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion ");
            return null;
        }
    }
}