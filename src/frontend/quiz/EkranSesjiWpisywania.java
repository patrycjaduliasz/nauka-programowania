package frontend.quiz;

import backend.model.PytanieOtwarte;
import backend.model.Uzytkownik;
import backend.usluga.UslugaQuizu;
import frontend.glowny.PanelGlowny;
import stale.StaleKolory;
import stale.StaleCzcionki;
import stale.StaleWspolne;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class EkranSesjiWpisywania extends StaleWspolne {
    private Uzytkownik uzytkownik;
    private UslugaQuizu uslugaQuizu;
    private List<PytanieOtwarte> pytania;
    private int aktualnePytanie;
    private int wynik;
    private int idKategorii;
    private int liczbaPytan;
    private JTextArea pytanieTextArea;
    private JLabel zdjecieLabel;
    private JTextField odpowiedzField;
    private JButton sprawdzButton;
    private JButton nastepnePytanieButton;
    private JLabel poprawnaOdpowiedzLabel;
    private JLabel wynikLabel;

    public EkranSesjiWpisywania(Uzytkownik uzytkownik, int idKategorii, int liczbaPytan) {
        super("Quiz otwarty");
        this.uzytkownik = uzytkownik;
        this.idKategorii = idKategorii;
        this.liczbaPytan = liczbaPytan;
        this.uslugaQuizu = new UslugaQuizu();
        this.aktualnePytanie = 0;
        this.wynik = 0;

        try {
            this.pytania = uslugaQuizu.pobierzPytaniaDoWpisywaniaSpecjalne(idKategorii, liczbaPytan);
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
        setSize(520, 700);
        dodajKomponenty();
        wyswietlPytanie();
    }

    private void dodajKomponenty() {
        JLabel naglowek = new JLabel("", SwingConstants.CENTER);
        naglowek.setBounds(0, 50, 520, 80);
        naglowek.setFont(StaleCzcionki.CZCIONKA_NAGLOWEK);
        naglowek.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(naglowek);

        wynikLabel = new JLabel("0/" + pytania.size(), SwingConstants.RIGHT);
        wynikLabel.setBounds(400, 10, 100, 30);
        wynikLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        wynikLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(wynikLabel);

        pytanieTextArea = new JTextArea();
        pytanieTextArea.setBounds(30, 120, 460, 80);
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

        zdjecieLabel = new JLabel();
        zdjecieLabel.setBounds(110, 215, 300, 200);
        zdjecieLabel.setHorizontalAlignment(SwingConstants.CENTER);
        zdjecieLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(zdjecieLabel);

        odpowiedzField = new JTextField();
        odpowiedzField.setBounds(30, 440, 460, 40);
        odpowiedzField.setFont(StaleCzcionki.CZCIONKA_TEKST);
        odpowiedzField.setForeground(StaleKolory.KOLOR_TEKSTU);
        odpowiedzField.setBackground(StaleKolory.KOLOR_TLA);
        odpowiedzField.setBorder(BorderFactory.createLineBorder(StaleKolory.KOLOR_TEKSTU_PRZYCISKU, 1));
        odpowiedzField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sprawdzOdpowiedz();
                }
            }
        });
        add(odpowiedzField);

        sprawdzButton = new JButton("Sprawdź") {
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
        sprawdzButton.setBounds(135, 500, 250, 50); // Przesunięte w dół
        sprawdzButton.setFont(StaleCzcionki.CZCIONKA_TEKST);
        sprawdzButton.setBackground(StaleKolory.KOLOR_AKCENTU);
        sprawdzButton.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        sprawdzButton.setFocusPainted(false);
        sprawdzButton.setContentAreaFilled(false);
        sprawdzButton.addActionListener(e -> sprawdzOdpowiedz());
        add(sprawdzButton);

        nastepnePytanieButton = new JButton("Następne pytanie") {
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
        nastepnePytanieButton.setBounds(135, 500, 250, 50); // Przesunięte w dół
        nastepnePytanieButton.setFont(StaleCzcionki.CZCIONKA_TEKST);
        nastepnePytanieButton.setBackground(StaleKolory.KOLOR_AKCENTU);
        nastepnePytanieButton.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        nastepnePytanieButton.setFocusPainted(false);
        nastepnePytanieButton.setContentAreaFilled(false);
        nastepnePytanieButton.setVisible(false);
        nastepnePytanieButton.addActionListener(e -> nastepnePytanie());
        add(nastepnePytanieButton);

        poprawnaOdpowiedzLabel = new JLabel("", SwingConstants.CENTER);
        poprawnaOdpowiedzLabel.setBounds(30, 560, 460, 30);
        poprawnaOdpowiedzLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        poprawnaOdpowiedzLabel.setForeground(StaleKolory.KOLOR_POPRAWNEJ);
        poprawnaOdpowiedzLabel.setVisible(false);
        add(poprawnaOdpowiedzLabel);

        JLabel powrotLabel = new JLabel("\u21E6");
        powrotLabel.setBounds(30, 24, 50, 50);
        powrotLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        powrotLabel.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        powrotLabel.setBackground(StaleKolory.KOLOR_TLA);
        powrotLabel.setOpaque(true);
        powrotLabel.setBorder(BorderFactory.createLineBorder(StaleKolory.KOLOR_TEKSTU_PRZYCISKU, 1));
        powrotLabel.setHorizontalAlignment(SwingConstants.CENTER);
        powrotLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new PanelGlowny(uzytkownik).setVisible(true);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                powrotLabel.setBackground(StaleKolory.KOLOR_NAJECHANIA);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                powrotLabel.setBackground(StaleKolory.KOLOR_TLA);
            }
        });
        add(powrotLabel);
    }

    private void wyswietlPytanie() {
        if (aktualnePytanie < pytania.size()) {
            PytanieOtwarte p = pytania.get(aktualnePytanie);
            pytanieTextArea.setText((aktualnePytanie + 1) + ". " + p.getTrescPytania());
            odpowiedzField.setText("");
            odpowiedzField.setBackground(StaleKolory.KOLOR_TLA);
            odpowiedzField.setEditable(true);
            poprawnaOdpowiedzLabel.setVisible(false);
            sprawdzButton.setVisible(true);
            nastepnePytanieButton.setVisible(false);

            if (p.getSciezkaZdjecia() != null && !p.getSciezkaZdjecia().isEmpty()) {
                try {
                    File file = new File(p.getSciezkaZdjecia());
                    if (file.exists()) {
                        ImageIcon imageIcon = new ImageIcon(p.getSciezkaZdjecia());
                        zdjecieLabel.setIcon(imageIcon);
                        zdjecieLabel.setVisible(true);
                    } else {
                        zdjecieLabel.setIcon(null);
                        zdjecieLabel.setVisible(false);
                    }
                } catch (Exception e) {
                    zdjecieLabel.setIcon(null);
                    zdjecieLabel.setVisible(false);
                }
            } else {
                zdjecieLabel.setIcon(null);
                zdjecieLabel.setVisible(false);
            }
        } else {
            zakonczSesje();
        }
    }

    private void sprawdzOdpowiedz() {
        PytanieOtwarte p = pytania.get(aktualnePytanie);
        String poprawnaOdpowiedz = p.getPoprawnaOdpowiedz();
        String wpisanaOdpowiedz = odpowiedzField.getText().trim();

        if (wpisanaOdpowiedz.equalsIgnoreCase(poprawnaOdpowiedz)) {
            wynik++;
            odpowiedzField.setBackground(StaleKolory.KOLOR_POPRAWNEJ);
        } else {
            odpowiedzField.setBackground(StaleKolory.KOLOR_BLEDNEJ);
            poprawnaOdpowiedzLabel.setText("Poprawna odpowiedź: " + poprawnaOdpowiedz);
            poprawnaOdpowiedzLabel.setVisible(true);
        }

        wynikLabel.setText(wynik + "/" + pytania.size());
        odpowiedzField.setEditable(false);
        sprawdzButton.setVisible(false);
        nastepnePytanieButton.setVisible(true);
    }

    private void nastepnePytanie() {
        aktualnePytanie++;
        wyswietlPytanie();
    }

    private void zakonczSesje() {
        try {
            uslugaQuizu.zapiszSesjeWpisywania(
                    uzytkownik.getId(),
                    idKategorii,
                    wynik,
                    pytania.size()
            );
        } catch (SQLException e) {
            pokazKomunikat("Błąd podczas zapisywania wyniku sesji: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }

        pokazKomunikat("Sesja zakończona! Wynik: " + wynik + "/" + pytania.size(), "Koniec", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new PanelGlowny(uzytkownik).setVisible(true);
    }
}