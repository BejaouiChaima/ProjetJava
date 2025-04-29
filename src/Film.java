public class Film {
    private int idFilm;
    private String nom;
    private String date;
    private String categorie;
    private String titre;
    private int duree;
    private String realisateur;
    private String description;

    // Constructeur
    public Film() {}

    public Film(int idFilm, String nom, String date, String categorie, String titre,
                int duree, String realisateur, String description) {
        this.idFilm = idFilm;
        this.nom = nom;
        this.date = date;
        this.categorie = categorie;
        this.titre = titre;
        this.duree = duree;
        this.realisateur = realisateur;
        this.description = description;
    }

    // Constructeur sans ID pour la création
    public Film(String nom, String date, String categorie, String titre,
                int duree, String realisateur, String description) {
        this.nom = nom;
        this.date = date;
        this.categorie = categorie;
        this.titre = titre;
        this.duree = duree;
        this.realisateur = realisateur;
        this.description = description;
    }

    // Getters et Setters
    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getRealisateur() {
        return realisateur;
    }

    public void setRealisateur(String realisateur) {
        this.realisateur = realisateur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Film{" +
                "idFilm=" + idFilm +
                ", nom='" + nom + '\'' +
                ", date='" + date + '\'' +
                ", categorie='" + categorie + '\'' +
                ", titre='" + titre + '\'' +
                ", duree=" + duree +
                ", realisateur='" + realisateur + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}