package backend.model;

import java.time.LocalDateTime;

public class QuizOtwarty {
    private int id;
    private int idUzytkownika;
    private int idKategorii;
    private int wynik;
    private int liczbaPytan;
    private LocalDateTime dataSesji;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUzytkownika() {
        return idUzytkownika;
    }

    public void setIdUzytkownika(int idUzytkownika) {
        this.idUzytkownika = idUzytkownika;
    }

    public int getIdKategorii() {
        return idKategorii;
    }

    public void setIdKategorii(int idKategorii) {
        this.idKategorii = idKategorii;
    }

    public int getWynik() {
        return wynik;
    }

    public void setWynik(int wynik) {
        this.wynik = wynik;
    }

    public int getLiczbaPytan() {
        return liczbaPytan;
    }

    public void setLiczbaPytan(int liczbaPytan) {
        this.liczbaPytan = liczbaPytan;
    }

    public LocalDateTime getDataSesji() {
        return dataSesji;
    }

    public void setDataSesji(LocalDateTime dataSesji) {
        this.dataSesji = dataSesji;
    }
}