import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class BilletDao {
    public static List<Billet> consulterBilletsParUser(int idUser) {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT b.idBillet, b.prix, b.idReservation, b.etat, b.codeBillet, " +
                "f.titre AS titreFilm, p.Horaire " +
                "FROM billet b " +
                "JOIN reservation r ON b.idReservation = r.idReservation " +
                "JOIN projection p ON r.id_seance = p.id_seance " +
                "JOIN film f ON p.id_film = f.idFilm " +
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
                        rs.getString("etat"),
                        rs.getString("codeBillet")
                );
                String titreFilm = rs.getString("titreFilm");
                String horaire = rs.getString("Horaire");

                System.out.println("Billet : " + billet);
                System.out.println("Film associé : " + titreFilm);
                System.out.println("Horaire de la projection : " + horaire);
                System.out.println("-----------------------------");

                billets.add(billet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation des billets : " + e.getMessage());
        }

        return billets;
    }




    public static List<Billet> consulterTousBillets() {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT b.idBillet, b.prix, b.idReservation, b.etat, b.codeBillet, " +
                "f.titre AS titreFilm, u.nom AS nomUtilisateur, u.prenom AS prenomUtilisateur " +
                "FROM billet b " +
                "JOIN reservation r ON b.idReservation = r.idReservation " +
                "JOIN projection p ON r.id_seance = p.id_seance " +
                "JOIN film f ON p.id_film = f.idFilm " +
                "JOIN user u ON r.idUser = u.id";

        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Billet billet = new Billet(
                        rs.getInt("idBillet"),
                        rs.getInt("idReservation"),
                        rs.getBigDecimal("prix"),
                        rs.getString("etat"),
                        rs.getString("codeBillet")
                );

                String titreFilm = rs.getString("titreFilm");
                String nom = rs.getString("nomUtilisateur");
                String prenom = rs.getString("prenomUtilisateur");

                System.out.println("Billet : " + billet);
                System.out.println("Film associé : " + titreFilm);
                System.out.println("Client : " + prenom + " " + nom);
                System.out.println("-----------------------------");

                billets.add(billet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation de tous les billets : " + e.getMessage());
        }
        return billets;
    }


}
