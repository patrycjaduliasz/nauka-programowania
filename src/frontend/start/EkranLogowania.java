package frontend.start;

import backend.usluga.UslugaUwierzytelniania;
import backend.model.Uzytkownik;
import stale.StaleKolory;
import stale.StaleCzcionki;
import stale.StaleWspolne;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EkranLogowania extends StaleWspolne {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UslugaUwierzytelniania uwierzytelnianie;

    public EkranLogowania() {
        super("Pora na quiz!");
        this.uwierzytelnianie = new UslugaUwierzytelniania();
        dodajKomponenty();
    }

    private void dodajKomponenty() {
        JLabel naglowek = new JLabel("Logowanie", SwingConstants.CENTER);
        naglowek.setBounds(0, 50, 520, 80);
        naglowek.setFont(StaleCzcionki.CZCIONKA_NAGLOWEK);
        naglowek.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(naglowek);

        int startY = 190;

        add(stworzEtykiete("Nazwa użytkownika:", 30, startY, 400, 25, StaleCzcionki.WIELKOSC_TEKSTU));
        usernameField = stworzPoleTekstowe(30, startY + 30, 450, 55);
        usernameField.setBackground(StaleKolory.KOLOR_AKCENTU);
        usernameField.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        add(usernameField);

        int passwordLabelY = startY + 100;

        add(stworzEtykiete("Hasło:", 30, passwordLabelY, 400, 25, StaleCzcionki.WIELKOSC_TEKSTU));
        passwordField = stworzPoleHasla(30, passwordLabelY + 30, 450, 55);
        passwordField.setBackground(StaleKolory.KOLOR_AKCENTU);
        passwordField.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        add(passwordField);

        JButton loginButton = stworzPrzyciskZaokraglony("Zaloguj się", 125, 420, 250, 50);
        loginButton.addActionListener(this::obsluzLogowanie);
        add(loginButton);

        JLabel registerLabel = new JLabel("Nie masz konta? Założ je!");
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        registerLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        registerLabel.setBounds(125, 600, 250, 30);
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new EkranRejestracji().setVisible(true);
            }
        });
        add(registerLabel);
    }

    private JButton stworzPrzyciskZaokraglony(String tekst, int x, int y, int width, int height) {
        JButton button = new JButton(tekst) {
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
        button.setBounds(x, y, width, height);
        button.setFont(StaleCzcionki.CZCIONKA_TEKST);
        button.setBackground(StaleKolory.KOLOR_AKCENTU);
        button.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(StaleKolory.KOLOR_NAJECHANIA);
                button.setBounds(x - 5, y - 5, width + 10, height + 10);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(StaleKolory.KOLOR_AKCENTU);
                button.setBounds(x, y, width, height);
            }
        });
        return button;
    }

    private void obsluzLogowanie(ActionEvent e) {
        String nazwaUzytkownika = usernameField.getText().trim();
        String haslo = new String(passwordField.getPassword()).trim();

        if (nazwaUzytkownika.isEmpty() || haslo.isEmpty()) {
            pokazKomunikat("Wypełnij wszystkie pola!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Uzytkownik u = uwierzytelnianie.weryfikacjaUzytkownika(nazwaUzytkownika, haslo);
        if (u != null) {
            pokazKomunikat("Zalogowano pomyślnie!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new frontend.glowny.PanelGlowny(u).setVisible(true);
        } else {
            pokazKomunikat("Nieprawidłowa nazwa użytkownika lub hasło", "Błąd logowania", JOptionPane.ERROR_MESSAGE);
        }
    }
}