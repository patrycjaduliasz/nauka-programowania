//reprezentuje pytanie w quizie, przechowując jego identyfikator,
// kategorię, treść, cztery możliwe odpowiedzi w tablicy oraz pozycję
// poprawnej odpowiedzi.

package backend.model;

public class PytanieZamkniete {
    private int id;
    private int idKategorii;
    private String trescPytania;
    private String[] odpowiedzi;
    private int poprawnaPozycjaOdpowiedzi;

    public PytanieZamkniete() {

        odpowiedzi = new String[4];
    }

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

    public String[] getOdpowiedzi() {
        return odpowiedzi;
    }

    public void setOdpowiedzi(String[] odpowiedzi) {
        this.odpowiedzi = odpowiedzi;
    }

    public int getPoprawnaPozycjaOdpowiedzi() {
        return poprawnaPozycjaOdpowiedzi;
    }

    public void setPoprawnaPozycjaOdpowiedzi(int poprawnaPozycjaOdpowiedzi) {
        this.poprawnaPozycjaOdpowiedzi = poprawnaPozycjaOdpowiedzi;
    }

}