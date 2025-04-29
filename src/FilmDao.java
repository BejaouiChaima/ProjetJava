import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FilmDao {

    // Ajouter un nouveau film
    public boolean ajouterFilm(Film film) {
        String query = "INSERT INTO film (nom, date, categorie, titre, duree, realisateur, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatbaseCnx.connect();
             PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, film.getNom());
            pstmt.setString(2, film.getDate());
            pstmt.setString(3, film.getCategorie());
            pstmt.setString(4, film.getTitre());
            pstmt.setInt(5, film.getDuree());
            pstmt.setString(6, film.getRealisateur());
            pstmt.setString(7, film.getDescription());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    film.setIdFilm(generatedKeys.getInt(1));
                }
                System.out.println("Film ajouté avec succès. ID: " + film.getIdFilm());
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du film: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Modifier un film existant
    public boolean modifierFilm(Film film) {
        String query = "UPDATE film SET nom = ?, date = ?, categorie = ?, titre = ?, " +
                "duree = ?, realisateur = ?, description = ? WHERE idFilm = ?";

        try (Connection connection = DatbaseCnx.connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, film.getNom());
            pstmt.setString(2, film.getDate());
            pstmt.setString(3, film.getCategorie());
            pstmt.setString(4, film.getTitre());
            pstmt.setInt(5, film.getDuree());
            pstmt.setString(6, film.getRealisateur());
            pstmt.setString(7, film.getDescription());
            pstmt.setInt(8, film.getIdFilm());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Film modifié avec succès. ID: " + film.getIdFilm());
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du film: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Supprimer un film
    public boolean supprimerFilm(int idFilm) {
        String query = "DELETE FROM film WHERE idFilm = ?";

        try (Connection connection = DatbaseCnx.connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, idFilm);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Film supprimé avec succès. ID: " + idFilm);
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du film: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Consulter un film par ID
    public Film consulterFilm(int idFilm) {
        String query = "SELECT * FROM film WHERE idFilm = ?";

        try (Connection connection = DatbaseCnx.connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, idFilm);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Film film = new Film();
                film.setIdFilm(rs.getInt("idFilm"));
                film.setNom(rs.getString("nom"));
                film.setDate(rs.getString("date"));
                film.setCategorie(rs.getString("categorie"));
                film.setTitre(rs.getString("titre"));
                film.setDuree(rs.getInt("duree"));
                film.setRealisateur(rs.getString("realisateur"));
                film.setDescription(rs.getString("description"));

                return film;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation du film: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Récupérer tous les films
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String query = "SELECT * FROM film";

        try (Connection connection = DatbaseCnx.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Film film = new Film();
                film.setIdFilm(rs.getInt("idFilm"));
                film.setNom(rs.getString("nom"));
                film.setDate(rs.getString("date"));
                film.setCategorie(rs.getString("categorie"));
                film.setTitre(rs.getString("titre"));
                film.setDuree(rs.getInt("duree"));
                film.setRealisateur(rs.getString("realisateur"));
                film.setDescription(rs.getString("description"));

                films.add(film);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des films: " + e.getMessage());
            e.printStackTrace();
        }

        return films;
    }

    // Rechercher des films par titre
    public List<Film> rechercherFilmsParTitre(String motCle) {
        List<Film> films = new ArrayList<>();
        String query = "SELECT * FROM film WHERE titre LIKE ? OR nom LIKE ?";

        try (Connection connection = DatbaseCnx.connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, "%" + motCle + "%");
            pstmt.setString(2, "%" + motCle + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Film film = new Film();
                film.setIdFilm(rs.getInt("idFilm"));
                film.setNom(rs.getString("nom"));
                film.setDate(rs.getString("date"));
                film.setCategorie(rs.getString("categorie"));
                film.setTitre(rs.getString("titre"));
                film.setDuree(rs.getInt("duree"));
                film.setRealisateur(rs.getString("realisateur"));
                film.setDescription(rs.getString("description"));

                films.add(film);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de films: " + e.getMessage());
            e.printStackTrace();
        }

        return films;
    }
}