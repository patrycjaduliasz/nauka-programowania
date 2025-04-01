// reprezentuje pytanie w quizie otwartym i przechowuje informacje
// o id, kat.,tresci pytania, poprawna odp, sciezka (opcjonalnie).

package backend.model;

public class PytanieOtwarte {
    private int id;
    private int idKategorii;
    private String trescPytania;
    private String poprawnaOdpowiedz;
    private String sciezkaZdjecia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdKategorii() {
        return idKategorii;
    }

    public void setIdKategorii(int idKategorii) {
        this.idKategorii = idKategorii;
    }

    public String getTrescPytania() {
        return trescPytania;
    }

    public void setTrescPytania(String trescPytania) {
        this.trescPytania = trescPytania;
    }

    public String getPoprawnaOdpowiedz() {
        return poprawnaOdpowiedz;
    }

    public void setPoprawnaOdpowiedz(String poprawnaOdpowiedz) {
        this.poprawnaOdpowiedz = poprawnaOdpowiedz;
    }

    public String getSciezkaZdjecia() {
        return sciezkaZdjecia;
    }

    public void setSciezkaZdjecia(String sciezkaZdjecia) {
        this.sciezkaZdjecia = sciezkaZdjecia;
    }
}