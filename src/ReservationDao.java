import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {

    public static boolean ajouterReservation(int idSeance, int nbPlaces) {
        User currentUser = Authentification.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Aucun utilisateur connecté.");
            return false;
        }

        int idUser = currentUser.getId();

        try (Connection conn = DatbaseCnx.connect()) {
            String sqlPlacesTotales = "SELECT NBPlaces FROM projection WHERE id_seance = ?";
            int nbPlacesTotales = 0;

            try (PreparedStatement stmt = conn.prepareStatement(sqlPlacesTotales)) {
                stmt.setInt(1, idSeance);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    nbPlacesTotales = rs.getInt("NBPlaces");
                } else {
                    System.out.println("Séance introuvable.");
                    return false;
                }
            }

            String sqlPlacesReservees = "SELECT SUM(NB_places) AS totalReserve FROM reservation WHERE id_seance = ? AND Etat != 'Annuler'";
            int nbPlacesReservees = 0;

            try (PreparedStatement stmt = conn.prepareStatement(sqlPlacesReservees)) {
                stmt.setInt(1, idSeance);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    nbPlacesReservees = rs.getInt("totalReserve");
                }
            }

            int nbPlacesDisponibles = nbPlacesTotales - nbPlacesReservees;

            if (nbPlaces > nbPlacesDisponibles) {
                System.out.println("Pas assez de places disponibles !");
                System.out.println("Places restantes : " + nbPlacesDisponibles);
                System.out.println("Veuillez saisir un nouveau nombre de places.");
                return false;
            }

            String sqlInsert = "INSERT INTO reservation (id_seance, NB_places, Etat, idUser) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
                stmt.setInt(1, idSeance);
                stmt.setInt(2, nbPlaces);
                stmt.setString(3, "EnCours");
                stmt.setInt(4, idUser);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Réservation ajoutée avec succès.");
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réservation : " + e.getMessage());
        }

        return false;
    }



    // 1. L'admin peut annuler n'importe quelle réservation
    public static boolean annulerReservationParAdmin(int idReservation) {
        String checkSql = "SELECT Etat FROM reservation WHERE idReservation = ?";

        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, idReservation);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String etat = rs.getString("Etat");
                if (etat.equalsIgnoreCase("Annuler")) {
                    System.out.println("Cette réservation est déjà annulée.");
                    return false;
                }
            } else {
                System.out.println("Réservation introuvable.");
                return false;
            }

            // L’admin peut forcer l’annulation
            String updateSql = "UPDATE reservation SET Etat = 'Annuler' WHERE idReservation = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, idReservation);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Réservation annulée par l'administrateur avec succès.");
                    return true;
                } else {
                    System.out.println("Erreur lors de l'annulation.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        return false;
    }



    // 2. Un utilisateur peut annuler sa propre réservation
    public static boolean annulerMaReservation(int idReservation) {
        User currentUser = Authentification.getCurrentUser();

        if (currentUser == null) {
            System.out.println("Aucun utilisateur connecté.");
            return false;
        }

        int idUser = currentUser.getId();
        String checkSql = "SELECT Etat FROM reservation WHERE idReservation = ? AND idUser = ?";

        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, idReservation);
            checkStmt.setInt(2, idUser);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String etat = rs.getString("Etat");
                if (etat.equalsIgnoreCase("Payer")) {
                    System.out.println("Vous ne pouvez pas annuler une réservation déjà payée.");
                    return false;
                } else if (etat.equalsIgnoreCase("Annuler")) {
                    System.out.println("Cette réservation est déjà annulée.");
                    return false;
                }
            } else {
                System.out.println("Réservation introuvable ou non autorisée.");
                return false;
            }

            // Si tout va bien, on met à jour
            String updateSql = "UPDATE reservation SET Etat = 'Annuler' WHERE idReservation = ? AND idUser = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, idReservation);
                updateStmt.setInt(2, idUser);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Votre réservation a été annulée avec succès.");
                    return true;
                } else {
                    System.out.println("Erreur lors de l'annulation.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'annulation de votre réservation : " + e.getMessage());
        }

        return false;
    }



    public static boolean payerReservation(int idReservation) {
        String updateReservation = "UPDATE reservation SET Etat = 'payer' WHERE idReservation = ?";
        String getSeanceInfo = "SELECT r.nb_places, p.prix, r.id_seance FROM reservation r JOIN projection p ON r.id_seance = p.id_seance WHERE r.idReservation = ?";

        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement getInfoStmt = conn.prepareStatement(getSeanceInfo)) {

            getInfoStmt.setInt(1, idReservation);
            ResultSet rs = getInfoStmt.executeQuery();

            if (rs.next()) {
                int nbPlaces = rs.getInt("nb_places");
                BigDecimal prixUnitaire = rs.getBigDecimal("prix");
                int idSeance = rs.getInt("id_seance");

                BigDecimal total = prixUnitaire.multiply(new BigDecimal(nbPlaces));

                // 1. Mettre à jour l'état de la réservation
                try (PreparedStatement updateStmt = conn.prepareStatement(updateReservation)) {
                    updateStmt.setInt(1, idReservation);
                    int rowsUpdated = updateStmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        // 2. Insérer un billet par place
                        String insertBillet = "INSERT INTO billet (idReservation, prix, etat, codeBillet) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertBillet)) {
                            for (int i = 0; i < nbPlaces; i++) {
                                insertStmt.setInt(1, idReservation);
                                insertStmt.setBigDecimal(2, prixUnitaire);
                                insertStmt.setString(3, "Payer");
                                insertStmt.setString(4, generateCodeBillet());
                                insertStmt.addBatch();
                            }
                            insertStmt.executeBatch();
                        }

                        System.out.println("Paiement réussi.");
                        System.out.println("Prix unitaire : " + prixUnitaire + " DT");
                        System.out.println("Nombre de billets : " + nbPlaces);
                        System.out.println("Total payé : " + total + " DT");
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur lors du paiement : " + e.getMessage());
        }

        return false;
    }




    public static List<Reservation> consulterReservationsParUser(int idUser) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, r.NB_places, r.Etat, r.id_seance, r.idUser, " +
                "s.Horaire, sa.numero, f.titre, u.nom, u.prenom " +
                "FROM reservation r " +
                "JOIN projection s ON r.id_seance = s.id_seance " +
                "JOIN salle sa ON s.id_salle = sa.id_salle " +
                "JOIN film f ON s.id_film = f.idFilm " +
                "JOIN user u ON r.idUser = u.id " +
                "WHERE r.idUser = ?";

        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Connexion établie");
            System.out.println("========================================================================================================");
            System.out.printf("%-5s | %-20s | %-20s | %-10s | %-15s | %-10s | %-10s%n",
                    "ID", "Utilisateur", "Film", "Nb Places", "Salle", "Horaire", "État");
            System.out.println("========================================================================================================");

            while (rs.next()) {
                Reservation r = new Reservation(
                        rs.getInt("idReservation"),
                        rs.getInt("NB_places"),
                        rs.getString("Etat"),
                        rs.getInt("id_seance"),
                        rs.getInt("idUser")
                );
                reservations.add(r);

                String nomUtilisateur = rs.getString("nom") + " " + rs.getString("prenom");
                String titreFilm = rs.getString("titre");
                int nbPlaces = rs.getInt("NB_places");
                String salle = "Salle " + rs.getInt("numero");
                String horaire = rs.getString("Horaire");
                String etat = rs.getString("Etat");

                System.out.printf("%-5d | %-20s | %-20s | %-10d | %-15s | %-10s | %-10s%n",
                        r.getIdReservation(), nomUtilisateur, titreFilm, nbPlaces, salle, horaire, etat);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation des réservations : " + e.getMessage());
        }

        return reservations;
    }



    public static List<Reservation> consulterToutesReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, r.NB_places, r.Etat, r.id_seance, r.idUser, s.Horaire, sa.numero, f.titre, u.nom, u.prenom " +
                "FROM reservation r " +
                "JOIN projection s ON r.id_seance = s.id_seance " +
                "JOIN salle sa ON s.id_salle = sa.id_salle " +
                "JOIN film f ON s.id_film = f.idFilm " +
                "JOIN user u ON r.idUser = u.id"; // Associer les informations utilisateur

        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Affichage des titres des colonnes
            System.out.printf("%-5s | %-20s | %-20s | %-10s | %-15s | %-10s | %-10s%n",
                    "ID", "Utilisateur", "Film", "Nb Places", "Salle", "Horaire", "Etat");
            System.out.println("=".repeat(100)); // Ligne de séparation

            // Parcours des résultats et affichage dans un format tabulaire
            while (rs.next()) {
                // Créer une réservation avec les données de la base
                Reservation r = new Reservation(
                        rs.getInt("idReservation"),
                        rs.getInt("NB_places"),
                        rs.getString("Etat"),
                        rs.getInt("id_seance"),
                        rs.getInt("idUser")
                );

                // Ajouter des informations supplémentaires récupérées dans la requête SQL
                String horaire = rs.getString("Horaire");
                int numeroSalle = rs.getInt("numero");
                String titreFilm = rs.getString("titre");
                String nomUtilisateur = rs.getString("nom");
                String prenomUtilisateur = rs.getString("prenom");

                // Affichage esthétique avec des données formatées
                System.out.printf("%-5d | %-20s | %-20s | %-10d | %-15s | %-10s | %-10s%n",
                        r.getIdReservation(),
                        nomUtilisateur + " " + prenomUtilisateur, // Nom et prénom de l'utilisateur
                        titreFilm, // Titre du film
                        r.getNbPlaces(), // Nombre de places réservées
                        "Salle " + numeroSalle, // Salle avec son numéro
                        horaire, // Horaire de la projection
                        r.getEtat()); // Etat de la réservation

                // Ajouter la réservation à la liste
                reservations.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation de toutes les réservations : " + e.getMessage());
        }
        return reservations;
    }


    private static String generateCodeBillet() {
        return "BIL" + System.currentTimeMillis();
    }
}

