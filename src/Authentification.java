import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class Authentification {
    private static User currentUser = null;
    public static User login(String email, String password) {
        Connection conn = DatbaseCnx.connect();
        if (conn == null) return null;

        try {
            String sql = "SELECT * FROM user WHERE Email = ? AND Password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                currentUser = new User(
                        rs.getString("email"),
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("role")
                );
                return currentUser;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static boolean register(String nom, String prenom, String email, String password) {
        Connection conn = DatbaseCnx.connect();
        if (conn == null) return false;

        try {
            String sql = "INSERT INTO `user`(nom, prenom, Role,Password,Email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, "client");
            stmt.setString(4, password);
            stmt.setString(5, email);


            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void logout() {
        currentUser = null;
    }
}