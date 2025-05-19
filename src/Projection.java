import java.math.BigDecimal;

public class Projection {
    private int id_seance;
    private String horaire;
    private int nbPlaces;
    private int id_film;
    private int id_salle;
    private BigDecimal prix;

    public Projection(int id_seance, String horaire, int nbPlaces, int id_film,  int id_salle,BigDecimal prix) {
        this.id_seance = id_seance;
        this.horaire = horaire;
        this.nbPlaces = nbPlaces;
        this.id_film = id_film;
        this.id_salle = id_salle;
        this.prix = prix;

    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public int getId_film() {
        return id_film;
    }

    public void setId_film(int id_film) {
        this.id_film = id_film;
    }

    public int getId_salle() {
        return id_salle;
    }

    public void setId_salle(int id_salle) {
        this.id_salle = id_salle;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public int getId_seance() {
        return id_seance;
    }

    public void setId_seance(int id_seance) {
        this.id_seance = id_seance;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    @Override
    public String toString() {
        return "Projection{" +
                "horaire='" + horaire + '\'' +
                ", id_seance=" + id_seance +
                ", nbPlaces=" + nbPlaces +
                ", id_film=" + id_film +
                ", id_salle=" + id_salle +
                '}';
    }
}
