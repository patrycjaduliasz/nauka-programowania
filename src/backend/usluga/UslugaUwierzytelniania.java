package backend.usluga;

import backend.model.Uzytkownik;
import backend.bazadanych.Baza;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UslugaUwierzytelniania {
    private Baza baza;

    public UslugaUwierzytelniania() {
        this.baza = new Baza();
    }


    public boolean rejestracjaUzytkownika(String nazwaUzytkownika, String haslo, String email) {
        try (CallableStatement stmt = baza.getConnection().prepareCall("{call rejestracja_uzytkownika(?, ?, ?)}")) {
            stmt.setString(1, nazwaUzytkownika);
            stmt.setString(2, haslo);
            stmt.setString(3, email);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Uzytkownik weryfikacjaUzytkownika(String nazwaUzytkownika, String haslo) {
        try (CallableStatement stmt = baza.getConnection().prepareCall("{call weryfikacjaUzytkownika(?, ?)}")) {
            stmt.setString(1, nazwaUzytkownika);
            stmt.setString(2, haslo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Uzytkownik u = new Uzytkownik();
                u.setId(rs.getInt("id"));
                u.setNazwaUzytkownika(rs.getString("nazwa_uzytkownika"));
                u.setHaslo(rs.getString("haslo"));
                u.setEmail(rs.getString("email"));
                u.setDataStworzenia(rs.getTimestamp("data_stworzenia"));
                return u;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}