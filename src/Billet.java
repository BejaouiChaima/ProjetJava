import java.math.BigDecimal;

public class Billet {
    private int idBillet;
    private BigDecimal prix;
    private int idReservation;
    private String etat;
    private String codeBillet;

    public String getCodeBillet() {
        return codeBillet;
    }

    public void setCodeBillet(String codeBillet) {
        this.codeBillet = codeBillet;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Billet(int idBillet,  int idReservation, BigDecimal prix, String etat, String codeBillet) {
        this.idBillet = idBillet;
        this.prix = prix;
        this.idReservation = idReservation;
        this.etat = etat;
        this.codeBillet = codeBillet;
    }

    // Getters et Setters
    public int getIdBillet() {
        return idBillet;
    }

    public void setIdBillet(int idBillet) {
        this.idBillet = idBillet;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }



    @Override
    public String toString() {
        return "Billet{" +
                "idBillet=" + idBillet +
                ", prix=" + prix +
                ", idReservation=" + idReservation +

                '}';
    }
}
