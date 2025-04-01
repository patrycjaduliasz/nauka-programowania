package backend.model;

import java.sql.Timestamp;

public class Uzytkownik {
    private int id;
    private String nazwaUzytkownika;
    private String haslo;
    private String email;
    private Timestamp dataStworzenia;

    //pobieranie danych wpisywanych przez uzytkownika

    // Gettery i settery
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwaUzytkownika() {
        return nazwaUzytkownika;
    }

    public void setNazwaUzytkownika(String nazwaUzytkownika) {
        this.nazwaUzytkownika = nazwaUzytkownika;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getDataStworzenia() {
        return dataStworzenia;
    }

    public void setDataStworzenia(Timestamp dataStworzenia) {
        this.dataStworzenia = dataStworzenia;
    }
}