public class Reservation {
    private int idReservation;
    private int nbPlaces;
    private String etat; // Ex: "réservé", "payé"
    private int idSeance;
    private int idUser; // pour savoir quel utilisateur a réservé

    public Reservation(int idReservation, int nbPlaces,String etat,  int idSeance, int idUser) {
        this.idReservation = idReservation;
        this.nbPlaces = nbPlaces;
        this.etat = etat;
        this.idSeance = idSeance;
        this.idUser = idUser;

    }
    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdSeance() {
        return idSeance;
    }

    public void setIdSeance(int idSeance) {
        this.idSeance = idSeance;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }



    @Override
    public String toString() {
        return "Reservation{" +
                "etat='" + etat + '\'' +
                ", idReservation=" + idReservation +
                ", nbPlaces=" + nbPlaces +
                ", idSeance=" + idSeance +
                ", idUser=" + idUser +
                '}';
    }
}
