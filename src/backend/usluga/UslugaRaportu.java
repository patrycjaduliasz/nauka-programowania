package backend.usluga;

import backend.model.QuizZamkniety;
import backend.model.QuizOtwarty;
import backend.bazadanych.Baza;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UslugaRaportu {
    private Baza baza; //

    public UslugaRaportu() {
        this.baza = new Baza();
    }


    public List<QuizZamkniety> pobierzHistorieQuizow(int idUzytkownika, int limit) throws SQLException {
        List<QuizZamkniety> historia = new ArrayList<>();

        try (CallableStatement stmt = baza.getConnection().prepareCall("{call pobierzHistorieQuizuZamknietego(?, ?)}")) {
            stmt.setInt(1, idUzytkownika);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                QuizZamkniety q = new QuizZamkniety();
                q.setId(rs.getInt("id"));
                q.setIdUzytkownika(rs.getInt("id_uzytkownika"));
                q.setIdKategorii(rs.getInt("id_kategorii"));
                q.setWynik(rs.getInt("wynik"));
                q.setLiczbaPytan(rs.getInt("liczba_pytan"));
                q.setDataProby(rs.getTimestamp("data_proby").toLocalDateTime());
                historia.add(q);
            }
            return historia;
        }
    }

    public double pobierzSredniWynik(int idUzytkownika) throws SQLException {
        try (CallableStatement stmt = baza.getConnection().prepareCall(
                "SELECT AVG(wynik * 1.0 / liczba_pytan) AS srednia FROM quizy WHERE id_uzytkownika = ?")) {
            stmt.setInt(1, idUzytkownika);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("srednia");
            }
            return 0.0;
        }
    }

    public QuizZamkniety pobierzNajlepszyWynikWKategorii(int idUzytkownika, int idKategorii) throws SQLException {
        try (CallableStatement stmt = baza.getConnection().prepareCall(
                "SELECT * FROM quizy WHERE id_uzytkownika = ? AND id_kategorii = ? ORDER BY wynik DESC LIMIT 1")) {
            stmt.setInt(1, idUzytkownika);
            stmt.setInt(2, idKategorii);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                QuizZamkniety q = new QuizZamkniety();
                q.setId(rs.getInt("id"));
                q.setIdUzytkownika(rs.getInt("id_uzytkownika"));
                q.setIdKategorii(rs.getInt("id_kategorii"));
                q.setWynik(rs.getInt("wynik"));
                q.setLiczbaPytan(rs.getInt("liczba_pytan"));
                q.setDataProby(rs.getTimestamp("data_proby").toLocalDateTime());
                return q;
            }
            return null;
        }
    }

    public List<QuizOtwarty> pobierzHistorieSesjiWpisywania(int idUzytkownika, int limit) throws SQLException {
        List<QuizOtwarty> historia = new ArrayList<>();

        try (CallableStatement stmt = baza.getConnection().prepareCall(
                "SELECT * FROM sesje_wpisywania WHERE id_uzytkownika = ? ORDER BY data_sesji DESC LIMIT ?")) {
            stmt.setInt(1, idUzytkownika);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                QuizOtwarty s = new QuizOtwarty();
                s.setId(rs.getInt("id"));
                s.setIdUzytkownika(rs.getInt("id_uzytkownika"));
                s.setIdKategorii(rs.getInt("id_kategorii"));
                s.setWynik(rs.getInt("wynik"));
                s.setLiczbaPytan(rs.getInt("liczba_pytan"));
                s.setDataSesji(rs.getTimestamp("data_sesji").toLocalDateTime());
                historia.add(s);
            }
            return historia;
        }
    }

    public double pobierzSredniWynikWpisywania(int idUzytkownika) throws SQLException {
        try (CallableStatement stmt = baza.getConnection().prepareCall(
                "SELECT AVG(wynik * 1.0 / liczba_pytan) AS srednia FROM sesje_wpisywania WHERE id_uzytkownika = ?")) {
            stmt.setInt(1, idUzytkownika);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("srednia");
            }
            return 0.0;
        }
    }

    public QuizOtwarty pobierzNajlepszyWynikWpisywaniaWKategorii(int idUzytkownika, int idKategorii) throws SQLException {
        try (CallableStatement stmt = baza.getConnection().prepareCall(
                "SELECT * FROM sesje_wpisywania WHERE id_uzytkownika = ? AND id_kategorii = ? ORDER BY wynik DESC LIMIT 1")) {
            stmt.setInt(1, idUzytkownika);
            stmt.setInt(2, idKategorii);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                QuizOtwarty s = new QuizOtwarty();
                s.setId(rs.getInt("id")); // Wypełnia ID
                s.setIdUzytkownika(rs.getInt("id_uzytkownika"));
                s.setIdKategorii(rs.getInt("id_kategorii"));
                s.setWynik(rs.getInt("wynik"));
                s.setLiczbaPytan(rs.getInt("liczba_pytan"));
                s.setDataSesji(rs.getTimestamp("data_sesji").toLocalDateTime());
                return s;
            }
            return null;
        }
    }
}