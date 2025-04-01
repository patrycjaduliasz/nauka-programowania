package frontend.quiz;

import backend.model.Kategoria;
import backend.model.Uzytkownik;
import backend.usluga.UslugaQuizu;
import frontend.glowny.PanelGlowny;
import stale.StaleKolory;
import stale.StaleCzcionki;
import stale.StaleWspolne;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EkranStartuWpisywania extends StaleWspolne {
    private Uzytkownik uzytkownik;
    private UslugaQuizu uslugaQuizu;
    private JComboBox<String> kategorieComboBox;
    private JSpinner liczbaPytanSpinner;
    private List<Kategoria> kategorie;

    public EkranStartuWpisywania(Uzytkownik uzytkownik) {
        super("Dopasuj quiz otwarty");
        this.uzytkownik = uzytkownik;
        this.uslugaQuizu = new UslugaQuizu();
        setSize(520, 700);

        try {
            kategorie = uslugaQuizu.pobierzKategorie();
        } catch (SQLException e) {
            pokazKomunikat("Błąd podczas pobierania kategorii: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            dispose();
            new PanelGlowny(uzytkownik).setVisible(true);
            return;
        }
        dodajKomponenty();
    }

    private void dodajKomponenty() {
        JLabel naglowek = new JLabel("", SwingConstants.CENTER);
        naglowek.setBounds(0, 50, 520, 80);
        naglowek.setFont(StaleCzcionki.CZCIONKA_NAGLOWEK);
        naglowek.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(naglowek);

        JLabel kategoriaLabel = new JLabel("Wybierz kategorię:");
        kategoriaLabel.setBounds(30, 150, 200, 30);
        kategoriaLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        kategoriaLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(kategoriaLabel);

        String[] nazwyKategorii = new String[kategorie.size()];
        for (int i = 0; i < kategorie.size(); i++) {
            nazwyKategorii[i] = kategorie.get(i).getNazwa();
        }
        kategorieComboBox = new JComboBox<>(nazwyKategorii);
        kategorieComboBox.setBounds(230, 150, 260, 30);
        kategorieComboBox.setFont(StaleCzcionki.CZCIONKA_TEKST);
        kategorieComboBox.setBackground(StaleKolory.KOLOR_TLA);
        kategorieComboBox.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(kategorieComboBox);

        JLabel liczbaPytanLabel = new JLabel("Liczba pytań:");
        liczbaPytanLabel.setBounds(30, 200, 200, 30);
        liczbaPytanLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        liczbaPytanLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(liczbaPytanLabel);

        SpinnerNumberModel model = new SpinnerNumberModel(5, 1, 10, 1);
        liczbaPytanSpinner = new JSpinner(model);
        liczbaPytanSpinner.setBounds(230, 200, 260, 30);
        liczbaPytanSpinner.setFont(StaleCzcionki.CZCIONKA_TEKST);
        add(liczbaPytanSpinner);

        JButton rozpocznijButton = new JButton("Rozpocznij") {
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
        rozpocznijButton.setBounds(135, 300, 250, 50);
        rozpocznijButton.setFont(StaleCzcionki.CZCIONKA_TEKST);
        rozpocznijButton.setBackground(StaleKolory.KOLOR_AKCENTU);
        rozpocznijButton.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        rozpocznijButton.setFocusPainted(false);
        rozpocznijButton.setContentAreaFilled(false);
        rozpocznijButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                rozpocznijButton.setBackground(StaleKolory.KOLOR_NAJECHANIA);
                rozpocznijButton.setBounds(130, 295, 260, 60);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                rozpocznijButton.setBackground(StaleKolory.KOLOR_AKCENTU);
                rozpocznijButton.setBounds(135, 300, 250, 50);
            }
        });
        rozpocznijButton.addActionListener(e -> rozpocznijSesje());
        add(rozpocznijButton);

        JLabel powrotLabel = new JLabel("\u21E6");
        powrotLabel.setBounds(30, 30, 50, 50);
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

    private void rozpocznijSesje() {
        int wybranaKategoriaIndex = kategorieComboBox.getSelectedIndex();
        if (wybranaKategoriaIndex == -1) {
            pokazKomunikat("Proszę wybrać kategorię!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idKategorii = kategorie.get(wybranaKategoriaIndex).getId();
        int liczbaPytan = (int) liczbaPytanSpinner.getValue();
        dispose();
        new EkranSesjiWpisywania(uzytkownik, idKategorii, liczbaPytan).setVisible(true);
    }
}