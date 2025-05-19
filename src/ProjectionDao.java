import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectionDao {
    // Ajouter une projection
    public static void creerSeance(Projection projection) {
        String sql = "INSERT INTO projection (Horaire, NBPlaces, id_film, id_salle,prix) VALUES (?, ?, ?, ?,?)";
        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, projection.getHoraire());
            stmt.setInt(2, projection.getNbPlaces());
            stmt.setInt(3, projection.getId_film());
            stmt.setInt(4, projection.getId_salle());
            stmt.setBigDecimal(5, projection.getPrix());


            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        projection.setId_seance(generatedId);  // Mettre à jour l'ID de la projection
                        System.out.println("Séance créée avec succès ! ID généré : " + generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la projection : " + e.getMessage());
        }
    }


    // Modifier une projection
    public static void modifierSeance(Projection projection) {
        String sql = "UPDATE projection SET Horaire = ?, NBPlaces = ?, id_film = ?, id_salle = ? , prix = ? WHERE id_seance = ?";
        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projection.getHoraire());
            stmt.setInt(2, projection.getNbPlaces());
            stmt.setInt(3, projection.getId_film());
            stmt.setInt(4, projection.getId_salle());
            stmt.setBigDecimal(5, projection.getPrix());
            stmt.setInt(6, projection.getId_seance());  // Utilisé uniquement pour trouver la bonne ligne à modifier

            stmt.executeUpdate();
            System.out.println("Séance modifiée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la projection : " + e.getMessage());
        }
    }

    // Supprimer une projection
    public static void supprimerSeance(int idSeance) {
        String sql = "DELETE FROM projection WHERE id_seance = ?";
        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSeance);
            stmt.executeUpdate();
            System.out.println("Séance supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la projection : " + e.getMessage());
        }
    }

    // Consulter toutes les projections
    public static void consulterSeances() {
        String sql = "SELECT p.*, f.titre AS titre, s.numero AS numero " +
                "FROM projection p " +
                "JOIN film f ON p.id_film = f.idFilm " +
                "JOIN salle s ON p.id_salle = s.id_salle";

        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // ✅ Affichage tout de suite après connexion
            System.out.println("Connexion établie");
            System.out.println("------------------------------------------------------------------------");
            System.out.printf("%-15s%-20s%-20s%-15s%-10s%-10s%n",
                    "ID Séance", "Film", "Horaire", "Places", "Salle","Prix");
            System.out.println("------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d%-20s%-20s%-15d%-10d%-10d%n",
                        rs.getInt("id_seance"),
                        rs.getString("titre"),
                        rs.getString("Horaire"),
                        rs.getInt("NBPlaces"),
                        rs.getInt("numero"),
                        rs.getInt("prix"));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des projections : " + e.getMessage());
        }
    }



    public static Projection consulterProjection(int id_seance) {
        Projection projection = null;
        String sql = "SELECT p.*, f.titre AS titre, s.numero AS numero " +
                "FROM projection p " +
                "JOIN film f ON p.id_film = f.idFilm " +
                "JOIN salle s ON p.id_salle = s.id_salle " +
                "WHERE p.id_seance = ?";

        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_seance);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String horaire = rs.getString("Horaire");
                int nbPlaces = rs.getInt("NBPlaces");
                int id_film = rs.getInt("id_film");
                int id_salle = rs.getInt("id_salle");
                BigDecimal prix = rs.getBigDecimal("prix");

                projection = new Projection(id_seance, horaire, nbPlaces, id_film, id_salle,prix);

                System.out.println("Titre du film : " + rs.getString("titre"));
                System.out.println("Numéro de salle : " + rs.getInt("numero"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation de la séance : " + e.getMessage());
        }
        return projection;
    }
    public static List<Projection> getAllProjections() {
        List<Projection> projections = new ArrayList<>();
        String sql = "SELECT * FROM projection"; // Assurez-vous que les données nécessaires sont récupérées

        try (Connection conn = DatbaseCnx.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // On crée une projection avec les attributs déjà définis dans la classe Projection
                Projection projection = new Projection(
                        rs.getInt("id_seance"),
                        rs.getString("horaire"),
                        rs.getInt("nbPlaces"),
                        rs.getInt("id_film"),
                        rs.getInt("id_salle"),
                        rs.getBigDecimal("prix")

                );
                projections.add(projection);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des projections : " + e.getMessage());
        }

        return projections;
    }


}
