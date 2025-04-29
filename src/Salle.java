public class Salle {
    private int idSalle;
    private int nombrePlaces;
    private String numero;

    public Salle( int nombrePlaces, String numero) {

        this.nombrePlaces = nombrePlaces;
        this.numero = numero;
    }

    // Getters et Setters
    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
