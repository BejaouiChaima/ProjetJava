public class User {
    private int id;
    private String nom;
    private String prenom;
    private String role;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public User(String email, int id, String nom, String prenom, String role) {
        this.email = email;
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getNomComplet(){
        return nom+" "+prenom;
    }
}