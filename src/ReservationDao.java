import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {

    public static void ajouterReservation(int idSeance, int nbPlaces) {
        User currentUser = Authentification.getCurrentUser();  // Récupérer l'utilisateur connecté
        if (currentUser == null) {
            System.out.println("Aucun utilisateur connecté.");
            return;
        }

        int idUser = currentUser.getId();  // Récupérer l'idUser depuis l'utilisateur connecté

        String sql = "INSERT INTO reservation (id_seance, NB_places, Etat, idUser) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSeance);
            stmt.setInt(2, nbPlaces);
            stmt.setString(3, "EnCours");  // Ou "Reserver" selon la logique de ton application
            stmt.setInt(4, idUser);  // Ajouter l'idUser à la réservation

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Réservation ajoutée avec succès.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réservation : " + e.getMessage());
        }
    }



    public static boolean payerReservation(int idReservation) {
        String updateReservation = "UPDATE reservation SET Etat = 'payer' WHERE idReservation = ?";
        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement updateStmt = conn.prepareStatement(updateReservation)) {

            updateStmt.setInt(1, idReservation);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                // Après avoir changé l'état, on crée le billet
                String insertBillet = "INSERT INTO billet (idReservation, prix, etat, codeBillet) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertBillet)) {
                    insertStmt.setInt(1, idReservation);
                    insertStmt.setBigDecimal(2, new BigDecimal("10.00")); // Exemple de prix fixe
                    insertStmt.setString(3, "Payer");
                    insertStmt.setString(4, generateCodeBillet());

                    insertStmt.executeUpdate();
                }
                System.out.println("Paiement réussi. Billet généré.");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du paiement : " + e.getMessage());
        }
        return false;
    }



    public static List<Reservation> consulterReservationsParUser(int idUser) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, r.NB_places, r.Etat, r.id_seance, r.idUser, s.Horaire, sa.numero, f.titre " +
                "FROM reservation r " +
                "JOIN projection s ON r.id_seance = s.id_seance " +
                "JOIN salle sa ON s.id_salle = sa.id_salle " +
                "JOIN film f ON s.id_film = f.idFilm " +
                "WHERE r.idUser = ?";

        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservation r = new Reservation(
                        rs.getInt("idReservation"),
                        rs.getInt("NB_places"),
                        rs.getString("Etat"),
                        rs.getInt("id_seance"),
                        rs.getInt("idUser")
                );

                // Ajouter les informations supplémentaires à la réservation
                String Horaire = rs.getString("Horaire");
                int numero = rs.getInt("numero");
                String titreFilm = rs.getString("titre");

                System.out.println("Réservation : " + r);
                System.out.println("Heure de la séance : " + Horaire);
                System.out.println("Numéro de la salle : " + numero);
                System.out.println("Titre du film : " + titreFilm);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation des réservations : " + e.getMessage());
        }
        return reservations;
    }


    public static List<Reservation> consulterToutesReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, r.NB_places, r.Etat, r.id_seance, r.idUser, s.Horaire, sa.numero, f.titre " +
                "FROM reservation r " +
                "JOIN projection s ON r.id_seance = s.id_seance " +
                "JOIN salle sa ON s.id_salle = sa.id_salle " +
                "JOIN film f ON s.id_film = f.idFilm";

        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reservation r = new Reservation(
                        rs.getInt("idReservation"),
                        rs.getInt("NB_places"),
                        rs.getString("Etat"),
                        rs.getInt("id_seance"),
                        rs.getInt("idUser")
                );

                // Ajouter les informations supplémentaires à la réservation
                String Horaire = rs.getString("Horaire");
                int numero = rs.getInt("numero");
                String titreFilm = rs.getString("titre");

                System.out.println("Réservation : " + r);
                System.out.println("Heure de la séance : " + Horaire);
                System.out.println("Numéro de la salle : " + numero);
                System.out.println("Titre du film : " + titreFilm);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation de toutes les réservations : " + e.getMessage());
        }
        return reservations;
    }
    private static String generateCodeBillet() {
        return "BIL" + System.currentTimeMillis();  // Simple : BIL + le temps en millisecondes
    }
}

