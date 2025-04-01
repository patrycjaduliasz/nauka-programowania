package stale;

import java.awt.*;
import java.io.InputStream;

public class StaleCzcionki {
    public static final int WIELKOSC_NAGLOWKA = 38;
    public static final int WIELKOSC_TEKSTU = 18;

    public static final Font CZCIONKA_NAGLOWEK = zaladujCzcionke("/Poppins-Light.ttf", Font.BOLD, WIELKOSC_NAGLOWKA);
    public static final Font CZCIONKA_TEKST = zaladujCzcionke("/WorkSans.ttf", Font.PLAIN, WIELKOSC_TEKSTU);
    public static final Font CZCIONKA_PRZYCISK = zaladujCzcionke("/WorkSans.ttf", Font.BOLD, WIELKOSC_TEKSTU);

    private static Font zaladujCzcionke(String sciezka, int styl, int rozmiar) {
        try {
            InputStream is = StaleCzcionki.class.getResourceAsStream(sciezka);
            if (is == null) throw new Exception("Nie znaleziono pliku czcionki: " + sciezka);
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(styl, (float) rozmiar);
        } catch (Exception e) {
            System.err.println("Błąd ładowania czcionki " + sciezka + ": " + e.getMessage());
            return new Font("Arial", styl, rozmiar); // Zastępcza
        }
    }
}