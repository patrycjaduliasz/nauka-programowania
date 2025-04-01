package frontend.start;

import backend.usluga.UslugaUwierzytelniania;
import stale.StaleKolory;
import stale.StaleCzcionki;
import stale.StaleWspolne;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EkranRejestracji extends StaleWspolne {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private UslugaUwierzytelniania uwierzytelnianie;

    public EkranRejestracji() {
        super("PPapka");
        this.uwierzytelnianie = new UslugaUwierzytelniania();
        dodajKomponenty();
    }

    private void dodajKomponenty() {
        JLabel naglowek = new JLabel("Rejestracja", SwingConstants.CENTER);
        naglowek.setBounds(0, 50, 520, 80);
        naglowek.setFont(StaleCzcionki.CZCIONKA_NAGLOWEK);
        naglowek.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(naglowek);

        int startY = 165;

        add(stworzEtykiete("Nazwa użytkownika:", 30, startY, 400, 25, StaleCzcionki.WIELKOSC_TEKSTU));
        usernameField = stworzPoleTekstowe(30, startY + 30, 450, 55);
        usernameField.setBackground(StaleKolory.KOLOR_AKCENTU);
        usernameField.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        add(usernameField);

        add(stworzEtykiete("Hasło:", 30, startY + 100, 400, 25, StaleCzcionki.WIELKOSC_TEKSTU));
        passwordField = stworzPoleHasla(30, startY + 130, 450, 55);
        passwordField.setBackground(StaleKolory.KOLOR_AKCENTU);
        passwordField.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        add(passwordField);

        add(stworzEtykiete("Email:", 30, startY + 200, 400, 25, StaleCzcionki.WIELKOSC_TEKSTU));
        emailField = stworzPoleTekstowe(30, startY + 230, 450, 55);
        emailField.setBackground(StaleKolory.KOLOR_AKCENTU);
        emailField.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        add(emailField);

        JButton registerButton = stworzPrzyciskZaokraglony("Zarejestruj się", 125, 520, 250, 50);
        registerButton.addActionListener(this::obsluzRejestracje);
        add(registerButton);
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

    private void obsluzRejestracje(ActionEvent e) {
        String nazwaUzytkownika = usernameField.getText().trim();
        String haslo = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();

        if (nazwaUzytkownika.isEmpty() || haslo.isEmpty() || email.isEmpty()) {
            pokazKomunikat("Wypełnij wszystkie pola!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (uwierzytelnianie.rejestracjaUzytkownika(nazwaUzytkownika, haslo, email)) {
            pokazKomunikat("Rejestracja pomyślna! Możesz się zalogować.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new EkranLogowania().setVisible(true);
        } else {
            pokazKomunikat("Błąd podczas rejestracji. Nazwa użytkownika może być zajęta.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}