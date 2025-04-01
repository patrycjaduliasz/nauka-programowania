package frontend.quiz;

import backend.model.Kategoria;
import backend.model.PytanieZamkniete;
import backend.usluga.UslugaQuizu;
import backend.model.Uzytkownik;
import frontend.glowny.PanelGlowny;
import stale.StaleKolory;
import stale.StaleCzcionki;
import stale.StaleWspolne;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EkranSesjiQuizu extends StaleWspolne {
    private Uzytkownik uzytkownik;
    private UslugaQuizu uslugaQuizu;
    private List<PytanieZamkniete> pytania;
    private int idKategorii;
    private int liczbaPytan;
    private int aktualnePytanie;
    private int wynik;
    private JTextArea pytanieTextArea;
    private JButton[] odpowiedzButtons;
    private JLabel wynikLabel;
    private boolean pierwszaOdpowiedzZapisana;
    private String nazwaKategorii;


    public EkranSesjiQuizu(Uzytkownik uzytkownik, int idKategorii, int liczbaPytan) {
        super("Quiz zamkniety");
        this.uzytkownik = uzytkownik;
        this.uslugaQuizu = new UslugaQuizu();
        this.idKategorii = idKategorii;
        this.liczbaPytan = liczbaPytan;
        this.aktualnePytanie = 0;
        this.wynik = 0;
        this.pierwszaOdpowiedzZapisana = false;


        try {
            Kategoria kategoria = uslugaQuizu.pobierzKategoriePoId(idKategorii);
            if (kategoria != null) {
                this.nazwaKategorii = kategoria.getNazwa();
            } else {
                this.nazwaKategorii = "Nieznana kategoria";
            }
        } catch (SQLException e) {
            this.nazwaKategorii = "Błąd kategorii";
            pokazKomunikat("Błąd podczas pobierania kategorii: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }

        try {
            this.pytania = uslugaQuizu.pobierzPytaniaZKategorii(idKategorii, liczbaPytan);
        } catch (SQLException e) {
            pokazKomunikat("Błąd podczas pobierania pytań: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            dispose();
            new PanelGlowny(uzytkownik).setVisible(true);
            return;
        }

        if (pytania.isEmpty()) {
            pokazKomunikat("Brak pytań w wybranej kategorii!", "Błąd", JOptionPane.ERROR_MESSAGE);
            dispose();
            new PanelGlowny(uzytkownik).setVisible(true);
            return;
        }

        dodajKomponenty();
        wyswietlPytanie();
    }

    private void dodajKomponenty() {
        JLabel naglowek = new JLabel(nazwaKategorii, SwingConstants.CENTER);
        naglowek.setBounds(0, 50, 520, 80);
        naglowek.setFont(StaleCzcionki.CZCIONKA_NAGLOWEK);
        naglowek.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(naglowek);

        wynikLabel = new JLabel("0/" + liczbaPytan, SwingConstants.RIGHT);
        wynikLabel.setBounds(400, 10, 100, 30);
        wynikLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        wynikLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(wynikLabel);

        pytanieTextArea = new JTextArea();
        pytanieTextArea.setBounds(30, 150, 460, 100);
        pytanieTextArea.setFont(StaleCzcionki.CZCIONKA_TEKST);
        pytanieTextArea.setForeground(StaleKolory.KOLOR_TEKSTU);
        pytanieTextArea.setBackground(getBackground());
        pytanieTextArea.setLineWrap(true);
        pytanieTextArea.setWrapStyleWord(true);
        pytanieTextArea.setEditable(false);
        pytanieTextArea.setFocusable(false);
        pytanieTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        pytanieTextArea.setAlignmentY(Component.CENTER_ALIGNMENT);
        add(pytanieTextArea);

        odpowiedzButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            odpowiedzButtons[i] = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(getBackground());
                    g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                    super.paintComponent(g);
                }

                @Override
                protected void paintBorder(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(getForeground());
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                }
            };
            odpowiedzButtons[i].setBounds(30, 260 + i * 60, 460, 50);
            odpowiedzButtons[i].setFont(StaleCzcionki.CZCIONKA_TEKST);
            odpowiedzButtons[i].setBackground(StaleKolory.KOLOR_AKCENTU);
            odpowiedzButtons[i].setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
            odpowiedzButtons[i].setFocusPainted(false);
            odpowiedzButtons[i].setOpaque(true);
            odpowiedzButtons[i].setContentAreaFilled(false);
            final int odpowiedzIndex = i;
            odpowiedzButtons[i].addActionListener(e -> sprawdzOdpowiedz(odpowiedzIndex));
            add(odpowiedzButtons[i]);
        }
    }


    private void wyswietlPytanie() {
        if (aktualnePytanie < pytania.size()) {
            PytanieZamkniete p = pytania.get(aktualnePytanie);
            pytanieTextArea.setText((aktualnePytanie + 1) + ". " + p.getTrescPytania());
            String[] odpowiedzi = p.getOdpowiedzi();

            for (int i = 0; i < 4; i++) {
                odpowiedzButtons[i].setText((char) ('A' + i) + ". " + odpowiedzi[i]);
                odpowiedzButtons[i].setEnabled(true);
                odpowiedzButtons[i].setBackground(StaleKolory.KOLOR_AKCENTU);
                odpowiedzButtons[i].revalidate();
                odpowiedzButtons[i].repaint();
            }
            pierwszaOdpowiedzZapisana = false;
        } else {
            zakonczQuiz();
        }
    }

    private void sprawdzOdpowiedz(int wybranaOdpowiedz) {
        PytanieZamkniete p = pytania.get(aktualnePytanie);
        int poprawnaOdpowiedz = p.getPoprawnaPozycjaOdpowiedzi() - 1;

        if (!pierwszaOdpowiedzZapisana) {
            if (wybranaOdpowiedz == poprawnaOdpowiedz) {
                wynik++;
            }
            pierwszaOdpowiedzZapisana = true;
            wynikLabel.setText(wynik + "/" + liczbaPytan);
        }


        if (wybranaOdpowiedz == poprawnaOdpowiedz) {
            odpowiedzButtons[wybranaOdpowiedz].setBackground(StaleKolory.KOLOR_POPRAWNEJ);
            odpowiedzButtons[wybranaOdpowiedz].revalidate();
            odpowiedzButtons[wybranaOdpowiedz].repaint();

            for (JButton button : odpowiedzButtons) {
                button.setEnabled(false);
                button.revalidate();
                button.repaint();
            }
            Timer timer = new Timer(1000, e -> nastepnePytanie());
            timer.setRepeats(false);
            timer.start();
        } else {
            odpowiedzButtons[wybranaOdpowiedz].setBackground(StaleKolory.KOLOR_BLEDNEJ);
            odpowiedzButtons[poprawnaOdpowiedz].setBackground(StaleKolory.KOLOR_POPRAWNEJ);
            odpowiedzButtons[wybranaOdpowiedz].revalidate();
            odpowiedzButtons[wybranaOdpowiedz].repaint();
            odpowiedzButtons[poprawnaOdpowiedz].revalidate();
            odpowiedzButtons[poprawnaOdpowiedz].repaint();
        }
    }


    private void nastepnePytanie() {
        aktualnePytanie++;
        if (aktualnePytanie < pytania.size()) {
            wyswietlPytanie();
        } else {
            zakonczQuiz();
        }
    }

    private void zakonczQuiz() {
        try {
            uslugaQuizu.zapiszQuiz(uzytkownik.getId(), idKategorii, wynik, liczbaPytan);
            pokazKomunikat("Quiz zakończony! Wynik: " + wynik + "/" + liczbaPytan, "Koniec", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            pokazKomunikat("Błąd podczas zapisywania quizu: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }

        dispose();
        new PanelGlowny(uzytkownik).setVisible(true);
    }
}