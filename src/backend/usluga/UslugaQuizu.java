package backend.usluga;

import backend.bazadanych.Baza;
import backend.model.Kategoria;
import backend.model.PytanieZamkniete;
import backend.model.PytanieOtwarte;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UslugaQuizu {
    private Baza baza;

    public UslugaQuizu() {
        this.baza = new Baza();
    }

    public List<PytanieZamkniete> pobierzPytaniaZKategorii(int idKategorii, int liczbaPytan) throws SQLException {
        List<PytanieZamkniete> pytania = new ArrayList<>();
        try (CallableStatement stmt = baza.getConnection().prepareCall("{call pobierz_pytania_z_kategorii(?, ?)}")) {
            stmt.setInt(1, idKategorii);
            stmt.setInt(2, liczbaPytan);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PytanieZamkniete p = new PytanieZamkniete();
                p.setId(rs.getInt("id"));
                p.setIdKategorii(rs.getInt("id_kategorii"));
                p.setTrescPytania(rs.getString("tresc_pytania"));
                String[] odpowiedzi = new String[4];
                odpowiedzi[0] = rs.getString("odpowiedz1");
                odpowiedzi[1] = rs.getString("odpowiedz2");
                odpowiedzi[2] = rs.getString("odpowiedz3");
                odpowiedzi[3] = rs.getString("odpowiedz4");
                p.setOdpowiedzi(odpowiedzi);
                p.setPoprawnaPozycjaOdpowiedzi(rs.getInt("poprawna_odpowiedz"));
                pytania.add(p);
            }
        }
        Collections.shuffle(pytania);
        return pytania;
    }

    public void zapiszQuiz(int idUzytkownika, int idKategorii, int wynik, int liczbaPytan) throws SQLException {
        try (CallableStatement stmt = baza.getConnection().prepareCall("{call zapisz_probe_quizu(?, ?, ?, ?, ?)}")) {
            stmt.setInt(1, idUzytkownika);
            stmt.setInt(2, idKategorii);
            stmt.setInt(3, wynik);
            stmt.setInt(4, liczbaPytan);
            stmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        }
    }

    public List<Kategoria> pobierzKategorie() throws SQLException {
        List<Kategoria> kategorie = new ArrayList<>();
        try (CallableStatement stmt = baza.getConnection().prepareCall("{call pobierz_kategorie()}")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Kategoria k = new Kategoria();
                k.setId(rs.getInt("id"));
                k.setNazwa(rs.getString("nazwa"));
                kategorie.add(k);
            }
        }
        return kategorie;
    }

    public void dodajPytanie(int idKategorii, String trescPytania, String[] odpowiedzi, int poprawnaOdpowiedz) throws SQLException {
        try (CallableStatement stmt = baza.getConnection().prepareCall("{call dodaj_pytanie(?, ?, ?, ?, ?, ?, ?)}")) {
            stmt.setInt(1, idKategorii);
            stmt.setString(2, trescPytania);
            stmt.setString(3, odpowiedzi[0]);
            stmt.setString(4, odpowiedzi[1]);
            stmt.setString(5, odpowiedzi[2]);
            stmt.setString(6, odpowiedzi[3]);
            stmt.setInt(7, poprawnaOdpowiedz);
            stmt.executeUpdate();
        }
    }

    public Kategoria pobierzKategoriePoId(int id) throws SQLException {
        String query = "SELECT * FROM kategorie WHERE id = ?";
        try (PreparedStatement stmt = baza.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Kategoria k = new Kategoria();
                k.setId(rs.getInt("id"));
                k.setNazwa(rs.getString("nazwa"));
                return k;
            }
        }
        return null;
    }

    public List<PytanieOtwarte> pobierzPytaniaDoWpisywaniaSpecjalne(int idKategorii, int liczbaPytan) throws SQLException {
        List<PytanieOtwarte> pytania = new ArrayList<>();
        try (CallableStatement stmt = baza.getConnection().prepareCall("{call PobierzPytaniadoWpisywaniaSpecjalne(?, ?)}")) {
            stmt.setInt(1, idKategorii);
            stmt.setInt(2, liczbaPytan);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PytanieOtwarte pytanie = new PytanieOtwarte();
                pytanie.setId(rs.getInt("id"));
                pytanie.setIdKategorii(rs.getInt("id_kategorii"));
                pytanie.setTrescPytania(rs.getString("tresc_pytania"));
                pytanie.setPoprawnaOdpowiedz(rs.getString("poprawna_odpowiedz"));
                pytanie.setSciezkaZdjecia(rs.getString("sciezka_zdjecia"));
                pytania.add(pytanie);
            }
        }
        Collections.shuffle(pytania);
        return pytania;
    }

    public void zapiszSesjeWpisywania(int idUzytkownika, int idKategorii, int wynik, int liczbaPytan) throws SQLException {
        try (CallableStatement stmt = baza.getConnection().prepareCall(
                "INSERT INTO sesje_wpisywania (id_uzytkownika, id_kategorii, wynik, liczba_pytan, data_sesji) VALUES (?, ?, ?, ?, NOW())")) {
            stmt.setInt(1, idUzytkownika);
            stmt.setInt(2, idKategorii);
            stmt.setInt(3, wynik);
            stmt.setInt(4, liczbaPytan);
            stmt.executeUpdate();
        }
    }

    // Dodaje nowe pytanie otwarte do bazy
    public void dodajPytanieOtwarte(int idKategorii, String trescPytania, String poprawnaOdpowiedz, String sciezkaZdjecia) throws SQLException {
        String query = "INSERT INTO pytania_do_wpisywania (id_kategorii, tresc_pytania, poprawna_odpowiedz, sciezka_zdjecia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = baza.getConnection().prepareStatement(query)) {
            stmt.setInt(1, idKategorii);
            stmt.setString(2, trescPytania);
            stmt.setString(3, poprawnaOdpowiedz);
            if (sciezkaZdjecia != null && !sciezkaZdjecia.isEmpty()) {
                stmt.setString(4, sciezkaZdjecia);
            } else {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            }
            stmt.executeUpdate();
        }
    }
}