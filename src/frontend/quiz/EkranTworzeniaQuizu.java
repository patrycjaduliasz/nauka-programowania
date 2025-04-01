package frontend.quiz;

import backend.model.Kategoria;
import backend.usluga.UslugaQuizu;
import backend.model.Uzytkownik;
import frontend.glowny.PanelGlowny;
import stale.StaleKolory;
import stale.StaleCzcionki;
import stale.StaleWspolne;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class EkranTworzeniaQuizu extends StaleWspolne {
    private Uzytkownik uzytkownik;
    private UslugaQuizu uslugaQuizu;
    private JComboBox<Kategoria> kategorieComboBox;
    private JComboBox<String> trybComboBox;
    private JTextField trescPytaniaField;
    private JTextField[] odpowiedziFields;
    private JComboBox<String> poprawnaOdpowiedzComboBox;
    private JTextField poprawnaOdpowiedzField;
    private JButton wybierzZdjecieButton;
    private JLabel sciezkaZdjeciaLabel;
    private String wybranaSciezkaZdjecia;
    private JPanel dynamicPanel;

    public EkranTworzeniaQuizu(Uzytkownik uzytkownik) {
        super("Dodawanie pytań");
        this.uzytkownik = uzytkownik;
        this.uslugaQuizu = new UslugaQuizu();
        setSize(700, 800);
        dodajKomponenty();
    }

    private void dodajKomponenty() {
        JLabel naglowek = new JLabel("", SwingConstants.CENTER);
        naglowek.setBounds(0, 50, 700, 30);
        naglowek.setFont(StaleCzcionki.CZCIONKA_NAGLOWEK);
        naglowek.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(naglowek);

        int startY = 100;

        JLabel trybLabel = new JLabel("Tryb:", SwingConstants.LEFT);
        trybLabel.setBounds(50, startY, 150, 25);
        trybLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        trybLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(trybLabel);

        String[] tryby = {"Dodaj pytanie zamknięte", "Dodaj pytanie otwarte"};
        trybComboBox = new JComboBox<>(tryby);
        trybComboBox.setBounds(50, startY + 30, 350, 40);
        trybComboBox.setFont(StaleCzcionki.CZCIONKA_TEKST);
        trybComboBox.setBackground(StaleKolory.KOLOR_TLA);
        trybComboBox.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(trybComboBox);

        JLabel kategoriaLabel = new JLabel("Kategoria:", SwingConstants.LEFT);
        kategoriaLabel.setBounds(410, startY, 150, 25);
        kategoriaLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        kategoriaLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(kategoriaLabel);

        kategorieComboBox = new JComboBox<>();
        kategorieComboBox.setBounds(410, startY + 30, 240, 40);
        kategorieComboBox.setFont(StaleCzcionki.CZCIONKA_TEKST);
        kategorieComboBox.setBackground(StaleKolory.KOLOR_TLA);
        kategorieComboBox.setForeground(StaleKolory.KOLOR_TEKSTU);
        try {
            List<Kategoria> kategorie = uslugaQuizu.pobierzKategorie();
            for (Kategoria k : kategorie) {
                kategorieComboBox.addItem(k);
            }
        } catch (SQLException e) {
            pokazKomunikat("Błąd podczas pobierania kategorii: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        add(kategorieComboBox);

        JLabel trescLabel = new JLabel("Treść pytania:", SwingConstants.LEFT);
        trescLabel.setBounds(50, startY + 80, 600, 25);
        trescLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        trescLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(trescLabel);

        trescPytaniaField = new JTextField();
        trescPytaniaField.setBounds(50, startY + 110, 600, 40);
        trescPytaniaField.setFont(StaleCzcionki.CZCIONKA_TEKST);
        trescPytaniaField.setBackground(StaleKolory.KOLOR_TLA);
        trescPytaniaField.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(trescPytaniaField);

        dynamicPanel = new JPanel();
        dynamicPanel.setLayout(null);
        dynamicPanel.setBounds(50, startY + 160, 600, 400);
        dynamicPanel.setBackground(StaleKolory.KOLOR_GLOWNY);
        add(dynamicPanel);

        odpowiedziFields = new JTextField[4]; // tablica na 4 odp
        for (int i = 0; i < 4; i++) {
            JLabel odpowiedzLabel = new JLabel("Odpowiedź " + (char) ('A' + i) + ":", SwingConstants.LEFT);
            odpowiedzLabel.setBounds(0, i * 80, 600, 25);
            odpowiedzLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
            odpowiedzLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
            dynamicPanel.add(odpowiedzLabel);

            odpowiedziFields[i] = new JTextField();
            odpowiedziFields[i].setBounds(0, 30 + i * 80, 600, 40);
            odpowiedziFields[i].setFont(StaleCzcionki.CZCIONKA_TEKST);
            odpowiedziFields[i].setBackground(StaleKolory.KOLOR_TLA);
            odpowiedziFields[i].setForeground(StaleKolory.KOLOR_TEKSTU);
            dynamicPanel.add(odpowiedziFields[i]);
        }

        JLabel poprawnaLabel = new JLabel("Poprawna odpowiedź:", SwingConstants.LEFT);
        poprawnaLabel.setBounds(0, 320, 600, 25);
        poprawnaLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        poprawnaLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        dynamicPanel.add(poprawnaLabel);

        String[] opcjePoprawnej = {"A", "B", "C", "D"};
        poprawnaOdpowiedzComboBox = new JComboBox<>(opcjePoprawnej);
        poprawnaOdpowiedzComboBox.setBounds(0, 350, 100, 40);
        poprawnaOdpowiedzComboBox.setFont(StaleCzcionki.CZCIONKA_TEKST);
        poprawnaOdpowiedzComboBox.setBackground(StaleKolory.KOLOR_TLA);
        poprawnaOdpowiedzComboBox.setForeground(StaleKolory.KOLOR_TEKSTU);
        dynamicPanel.add(poprawnaOdpowiedzComboBox);

        JLabel poprawnaOdpowiedzLabel = new JLabel("Poprawna odpowiedź:", SwingConstants.LEFT);
        poprawnaOdpowiedzLabel.setBounds(0, 0, 600, 25);
        poprawnaOdpowiedzLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        poprawnaOdpowiedzLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        poprawnaOdpowiedzLabel.setVisible(false);
        dynamicPanel.add(poprawnaOdpowiedzLabel);

        poprawnaOdpowiedzField = new JTextField();
        poprawnaOdpowiedzField.setBounds(0, 30, 600, 40);
        poprawnaOdpowiedzField.setFont(StaleCzcionki.CZCIONKA_TEKST);
        poprawnaOdpowiedzField.setBackground(StaleKolory.KOLOR_TLA);
        poprawnaOdpowiedzField.setForeground(StaleKolory.KOLOR_TEKSTU);
        poprawnaOdpowiedzField.setVisible(false);
        dynamicPanel.add(poprawnaOdpowiedzField);

        JLabel zdjecieLabel = new JLabel("Zdjęcie (opcjonalne):", SwingConstants.LEFT);
        zdjecieLabel.setBounds(0, 80, 600, 25);
        zdjecieLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        zdjecieLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        zdjecieLabel.setVisible(false);
        dynamicPanel.add(zdjecieLabel);

        wybierzZdjecieButton = new JButton("Wybierz zdjęcie") {
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
        wybierzZdjecieButton.setBounds(0, 110, 150, 40);
        wybierzZdjecieButton.setFont(StaleCzcionki.CZCIONKA_TEKST);
        wybierzZdjecieButton.setBackground(StaleKolory.KOLOR_AKCENTU);
        wybierzZdjecieButton.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        wybierzZdjecieButton.setFocusPainted(false);
        wybierzZdjecieButton.setContentAreaFilled(false);
        wybierzZdjecieButton.setVisible(false);
        wybierzZdjecieButton.addActionListener(e -> wybierzZdjecie());
        dynamicPanel.add(wybierzZdjecieButton);

        sciezkaZdjeciaLabel = new JLabel("Brak wybranego zdjęcia");
        sciezkaZdjeciaLabel.setBounds(160, 110, 440, 40);
        sciezkaZdjeciaLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        sciezkaZdjeciaLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        sciezkaZdjeciaLabel.setVisible(false);
        dynamicPanel.add(sciezkaZdjeciaLabel);

        JButton dodajButton = new JButton("Dodaj pytanie") {
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
        dodajButton.setBounds(225, startY + 560, 250, 50);
        dodajButton.setFont(StaleCzcionki.CZCIONKA_TEKST);
        dodajButton.setBackground(StaleKolory.KOLOR_TLA);
        dodajButton.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        dodajButton.setFocusPainted(false);
        dodajButton.setBorder(BorderFactory.createLineBorder(StaleKolory.KOLOR_TEKSTU_PRZYCISKU, 1));
        dodajButton.setContentAreaFilled(false);
        dodajButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                dodajButton.setBackground(StaleKolory.KOLOR_NAJECHANIA);
                dodajButton.setBounds(220, startY + 560, 260, 60);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                dodajButton.setBackground(StaleKolory.KOLOR_TLA);
                dodajButton.setBounds(225, startY + 560, 250, 50);
            }
        });
        dodajButton.addActionListener(e -> dodajPytanie());
        add(dodajButton);

        JLabel powrotLabel = new JLabel("\u21E6");
        powrotLabel.setBounds(50, 27, 50, 50);
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

        trybComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                aktualizujPola();
            }
        });

        aktualizujPola();
    }

    private void aktualizujPola() {
        String wybranyTryb = (String) trybComboBox.getSelectedItem();
        boolean pytanieZamkniete = wybranyTryb.equals("Dodaj pytanie zamknięte");

        for (int i = 0; i < 4; i++) {
            dynamicPanel.getComponent(i * 2).setVisible(pytanieZamkniete);
            odpowiedziFields[i].setVisible(pytanieZamkniete);
        }
        dynamicPanel.getComponent(8).setVisible(pytanieZamkniete);
        poprawnaOdpowiedzComboBox.setVisible(pytanieZamkniete);

        dynamicPanel.getComponent(10).setVisible(!pytanieZamkniete);
        poprawnaOdpowiedzField.setVisible(!pytanieZamkniete);
        dynamicPanel.getComponent(12).setVisible(!pytanieZamkniete);
        wybierzZdjecieButton.setVisible(!pytanieZamkniete);
        sciezkaZdjeciaLabel.setVisible(!pytanieZamkniete);

        if (pytanieZamkniete) {
            sciezkaZdjeciaLabel.setText("Brak wybranego zdjęcia");
            wybranaSciezkaZdjecia = null;
        }
    }

    private void wybierzZdjecie() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Obrazy", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File wybranyPlik = fileChooser.getSelectedFile();
            try {
                Path imagesDir = Paths.get("images");
                if (!Files.exists(imagesDir)) {
                    Files.createDirectories(imagesDir);
                }
                String nowyPlikNazwa = "pytanie_" + System.currentTimeMillis() + "_" + wybranyPlik.getName();
                Path nowyPlikSciezka = Paths.get("images", nowyPlikNazwa);
                Files.copy(wybranyPlik.toPath(), nowyPlikSciezka);

                wybranaSciezkaZdjecia = nowyPlikSciezka.toString();
                sciezkaZdjeciaLabel.setText(wybranyPlik.getName());
            } catch (IOException ex) {
                pokazKomunikat("Błąd podczas zapisywania zdjęcia: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void dodajPytanie() {
        Kategoria wybranaKategoria = (Kategoria) kategorieComboBox.getSelectedItem();
        String trescPytania = trescPytaniaField.getText().trim();
        String wybranyTryb = (String) trybComboBox.getSelectedItem();

        if (wybranaKategoria == null || trescPytania.isEmpty()) {
            pokazKomunikat("Proszę wypełnić wszystkie wymagane pola!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (wybranyTryb.equals("Dodaj pytanie zamknięte")) {
                String[] odpowiedzi = new String[4];
                for (int i = 0; i < 4; i++) {
                    odpowiedzi[i] = odpowiedziFields[i].getText().trim();
                    if (odpowiedzi[i].isEmpty()) {
                        pokazKomunikat("Proszę wypełnić wszystkie odpowiedzi!", "Błąd", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                int poprawnaOdpowiedz = poprawnaOdpowiedzComboBox.getSelectedIndex() + 1;
                uslugaQuizu.dodajPytanie(wybranaKategoria.getId(), trescPytania, odpowiedzi, poprawnaOdpowiedz);
            } else {
                String poprawnaOdpowiedz = poprawnaOdpowiedzField.getText().trim();
                if (poprawnaOdpowiedz.isEmpty()) {
                    pokazKomunikat("Proszę podać poprawną odpowiedź!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                uslugaQuizu.dodajPytanieOtwarte(wybranaKategoria.getId(), trescPytania, poprawnaOdpowiedz, wybranaSciezkaZdjecia);
            }
            pokazKomunikat("Pytanie zostało dodane!", "Sukces", JOptionPane.INFORMATION_MESSAGE);

            trescPytaniaField.setText("");
            for (JTextField field : odpowiedziFields) {
                field.setText("");
            }
            poprawnaOdpowiedzComboBox.setSelectedIndex(0);
            poprawnaOdpowiedzField.setText("");
            sciezkaZdjeciaLabel.setText("Brak wybranego zdjęcia");
            wybranaSciezkaZdjecia = null;
        } catch (SQLException ex) {
            pokazKomunikat("Błąd podczas dodawania pytania: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}