package frontend.glowny;

import backend.model.Uzytkownik;
import backend.usluga.UslugaQuizu;
import frontend.quiz.EkranStartuQuizu;
import frontend.quiz.EkranStartuWpisywania;
import frontend.quiz.EkranTworzeniaQuizu;
import stale.StaleKolory;
import stale.StaleCzcionki;
import stale.StaleWspolne;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelGlowny extends StaleWspolne {
    private Uzytkownik uzytkownik;
    private JButton stworzQuizButton;
    private JButton rozpocznijQuizButton;
    private JButton wpiszOdpowiedzButton;
    private JLabel avatarLabel;
    private UslugaQuizu uslugaQuizu;

    public PanelGlowny(Uzytkownik uzytkownik) {
        super("aPPka");
        this.uzytkownik = uzytkownik;
        this.uslugaQuizu = new UslugaQuizu();
        dodajKomponenty();
    }

    private void dodajKomponenty() {
        avatarLabel = new JLabel("\uD83D\uDC69") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        avatarLabel.setBounds(450, 30, 32, 32);
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        avatarLabel.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        avatarLabel.setOpaque(false);
        avatarLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        avatarLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                pokazProfil();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                avatarLabel.setBounds(448, 28, 36, 36);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                avatarLabel.setBounds(450, 30, 32, 32);
            }
        });
        add(avatarLabel);

        stworzQuizButton = stworzPrzyciskZaokraglony("Dodaj pytania", 125, 200, 270, 50);
        rozpocznijQuizButton = stworzPrzyciskZaokraglony("Quiz pytania zamknięte", 125, 270, 270, 50);
        wpiszOdpowiedzButton = stworzPrzyciskZaokraglony("Quiz pytania otwarte", 125, 340, 270, 50);

        stworzQuizButton.addActionListener(this::przejdzDoStworzQuiz);
        rozpocznijQuizButton.addActionListener(this::przejdzDoRozpocznijQuiz);
        wpiszOdpowiedzButton.addActionListener(this::przejdzDoWpiszOdpowiedz);

        add(stworzQuizButton);
        add(rozpocznijQuizButton);
        add(wpiszOdpowiedzButton);
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

    private void pokazProfil() {
        new ProfilUzytkownika(uzytkownik).setVisible(true);
    }

    private void przejdzDoStworzQuiz(ActionEvent e) {
        dispose();  // Zamknięcie bieżącego okna
        new EkranTworzeniaQuizu(uzytkownik).setVisible(true);
    }


    private void przejdzDoRozpocznijQuiz(ActionEvent e) {
        dispose();
        new EkranStartuQuizu(uzytkownik).setVisible(true);
    }

    private void przejdzDoWpiszOdpowiedz(ActionEvent e) {
        dispose();
        new EkranStartuWpisywania(uzytkownik).setVisible(true);
    }
}