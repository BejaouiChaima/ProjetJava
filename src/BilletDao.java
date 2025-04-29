import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class BilletDao {
    public static List<Billet> consulterBilletsParUser(int idUser) {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT b.idBillet, b.prix, b.idReservation, b.etat, b.codeBillet " +
                "FROM billet b " +
                "JOIN reservation r ON b.idReservation = r.idReservation " +
                "WHERE r.idUser = ?";
        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Billet billet = new Billet(
                        rs.getInt("idBillet"),
                        rs.getInt("idReservation"),
                        rs.getBigDecimal("prix"),
                        rs.getString("etat"),        // <-- bien minuscule ici
                        rs.getString("codeBillet")
                );
                billets.add(billet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation des billets : " + e.getMessage());
        }
        return billets;
    }


    public static List<Billet> consulterTousBillets() {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT * FROM billet";
        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Billet billet = new Billet(
                        rs.getInt("idBillet"),
                        rs.getInt("idReservation"),
                        rs.getBigDecimal("prix"),
                        rs.getString("Etat"),
                        rs.getString("codeBillet")
                );
                billets.add(billet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation de tous les billets : " + e.getMessage());
        }
        return billets;
    }

}
