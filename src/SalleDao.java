import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDao {

    // Ajouter une salle
    public boolean ajouterSalle(Salle salle) {
        String query = "INSERT INTO salle (nombre_places, numero) VALUES (?, ?)";

        try (Connection connection = DatbaseCnx.connect();
             PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, salle.getNombrePlaces());
            pstmt.setString(2, salle.getNumero());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    salle.setIdSalle(generatedKeys.getInt(1));
                }
                System.out.println("Salle ajoutée avec succès. ID: " + salle.getIdSalle());
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la salle: " + e.getMessage());
        }

        return false;
    }

    // Modifier une salle
    public boolean modifierSalle(Salle salle) {
        String query = "UPDATE salle SET nombre_places = ?, numero = ? WHERE id_salle = ?";

        try (Connection connection = DatbaseCnx.connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, salle.getNombrePlaces());
            pstmt.setString(2, salle.getNumero());
            pstmt.setInt(3, salle.getIdSalle());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Salle modifiée avec succès.");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la salle: " + e.getMessage());
        }

        return false;
    }

    // Supprimer une salle
    public boolean supprimerSalle(int idSalle) {
        String query = "DELETE FROM salle WHERE id_salle = ?";

        try (Connection connection = DatbaseCnx.connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, idSalle);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Salle supprimée avec succès.");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la salle: " + e.getMessage());
        }

        return false;
    }

    // Consulter une salle
    public Salle consulterSalle(int idSalle) {
        String query = "SELECT * FROM salle WHERE id_salle = ?";

        try (Connection connection = DatbaseCnx.connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, idSalle);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Salle salle = new Salle(rs.getInt("nombre_places"), rs.getString("numero"));
                salle.setIdSalle(rs.getInt("id_salle"));
                salle.setNombrePlaces(rs.getInt("nombre_places"));
                salle.setNumero(rs.getString("numero"));
                return salle;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation de la salle: " + e.getMessage());
        }

        return null;
    }

    // Récupérer toutes les salles
    public List<Salle> getAllSalles() {
        List<Salle> salles = new ArrayList<>();
        String query = "SELECT * FROM salle";

        try (Connection connection = DatbaseCnx.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Salle salle = new Salle(rs.getInt("nombre_places"), rs.getString("numero"));
                salle.setIdSalle(rs.getInt("id_salle"));
                salle.setNombrePlaces(rs.getInt("nombre_places"));
                salle.setNumero(rs.getString("numero"));
                salles.add(salle);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des salles: " + e.getMessage());
        }

        return salles;
    }
}
