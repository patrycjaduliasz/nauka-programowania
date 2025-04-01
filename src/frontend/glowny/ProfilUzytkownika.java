package frontend.glowny;

import backend.model.Kategoria;
import backend.model.QuizZamkniety;
import backend.model.QuizOtwarty;
import backend.usluga.UslugaQuizu;
import backend.usluga.UslugaRaportu;
import backend.model.Uzytkownik;
import stale.StaleKolory;
import stale.StaleCzcionki;
import stale.StaleWspolne;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfilUzytkownika extends StaleWspolne {
    private Uzytkownik uzytkownik;
    private UslugaRaportu raport;
    private UslugaQuizu uslugaQuizu;
    private Map<Integer, String> kategorieMap;
    private JTextArea raportTextArea;
    private JScrollPane scrollPane;
    private JButton raportButton;
    private JButton ukryjRaportButton;

    public ProfilUzytkownika(Uzytkownik uzytkownik) {
        super("Profil Uzytkownika");
        this.uzytkownik = uzytkownik;
        this.raport = new UslugaRaportu();
        this.uslugaQuizu = new UslugaQuizu();
        this.kategorieMap = new HashMap<>();

        try {
            List<Kategoria> kategorie = uslugaQuizu.pobierzKategorie();
            for (Kategoria k : kategorie) {
                kategorieMap.put(k.getId(), k.getNazwa());
            }
        } catch (SQLException e) {
            pokazKomunikat("Błąd podczas pobierania kategorii: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }

        dodajKomponenty();
    }

    private void dodajKomponenty() {

        JLabel naglowek = new JLabel("", SwingConstants.CENTER);
        naglowek.setBounds(0, 40, 520, 30);
        naglowek.setFont(StaleCzcionki.CZCIONKA_NAGLOWEK);
        naglowek.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(naglowek);


        JLabel profilLabel = new JLabel("Profil: " + uzytkownik.getNazwaUzytkownika());
        profilLabel.setBounds(30, 100, 460, 30);
        profilLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        profilLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(profilLabel);

        JLabel emailLabel = new JLabel("Email: " + uzytkownik.getEmail());
        emailLabel.setBounds(30, 140, 460, 30);
        emailLabel.setFont(StaleCzcionki.CZCIONKA_TEKST);
        emailLabel.setForeground(StaleKolory.KOLOR_TEKSTU);
        add(emailLabel);


        raportButton = new JButton("Pokaż Raport") {
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
        raportButton.setBounds(135, 200, 250, 50);
        raportButton.setFont(StaleCzcionki.CZCIONKA_TEKST);
        raportButton.setBackground(StaleKolory.KOLOR_TLA);
        raportButton.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        raportButton.setFocusPainted(false);
        raportButton.setBorder(BorderFactory.createLineBorder(StaleKolory.KOLOR_TEKSTU_PRZYCISKU, 1));
        raportButton.setContentAreaFilled(false);

        raportButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                raportButton.setBackground(StaleKolory.KOLOR_NAJECHANIA);
                raportButton.setBounds(130, 195, 260, 60);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                raportButton.setBackground(StaleKolory.KOLOR_TLA);
                raportButton.setBounds(135, 200, 250, 50);
            }
        });

        raportButton.addActionListener(e -> pokazRaport());
        add(raportButton);


        ukryjRaportButton = new JButton("Ukryj Raport") {
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

        ukryjRaportButton.setBounds(135, 200, 250, 50);
        ukryjRaportButton.setFont(StaleCzcionki.CZCIONKA_TEKST);
        ukryjRaportButton.setBackground(StaleKolory.KOLOR_TLA);
        ukryjRaportButton.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        ukryjRaportButton.setFocusPainted(false);
        ukryjRaportButton.setBorder(BorderFactory.createLineBorder(StaleKolory.KOLOR_TEKSTU_PRZYCISKU, 1));
        ukryjRaportButton.setContentAreaFilled(false);
        ukryjRaportButton.setVisible(false);


        ukryjRaportButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                ukryjRaportButton.setBackground(StaleKolory.KOLOR_NAJECHANIA);
                ukryjRaportButton.setBounds(130, 195, 260, 60);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                ukryjRaportButton.setBackground(StaleKolory.KOLOR_TLA);
                ukryjRaportButton.setBounds(135, 200, 250, 50);
            }
        });

        ukryjRaportButton.addActionListener(e -> {
            raportTextArea.setVisible(false);
            scrollPane.setVisible(false);
            ukryjRaportButton.setVisible(false);
            raportButton.setVisible(true);
        });
        add(ukryjRaportButton);


        raportTextArea = new JTextArea();
        raportTextArea.setFont(StaleCzcionki.CZCIONKA_TEKST);
        raportTextArea.setForeground(StaleKolory.KOLOR_TEKSTU);
        raportTextArea.setBackground(getBackground());
        raportTextArea.setLineWrap(true);
        raportTextArea.setWrapStyleWord(true);
        raportTextArea.setEditable(false);
        raportTextArea.setFocusable(false);
        raportTextArea.setVisible(false);


        scrollPane = new JScrollPane(raportTextArea);
        scrollPane.setBounds(30, 270, 460, 400);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVisible(false);
        add(scrollPane);


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

    private void pokazRaport() {
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            StringBuilder raportText = new StringBuilder();

            raportText.append("Quizy zamknięte\n");
            raportText.append("===============\n\n");

            List<QuizZamkniety> historiaQuizow = raport.pobierzHistorieQuizow(uzytkownik.getId(), 3);
            raportText.append("Ostatnie 3 quizy:\n");
            raportText.append("---------------\n");
            if (historiaQuizow != null && !historiaQuizow.isEmpty()) {
                for (QuizZamkniety q : historiaQuizow) {
                    String nazwaKategorii = kategorieMap.getOrDefault(q.getIdKategorii(), "Nieznana kategoria");
                    raportText.append("Data: ").append(q.getDataProby())
                            .append(", Kategoria: ").append(nazwaKategorii)
                            .append(", Wynik: ").append(q.getWynik()).append("/").append(q.getLiczbaPytan()).append("\n");
                }
            } else {
                raportText.append("Brak historii quizów.\n");
            }

            double sredniWynikQuizow = raport.pobierzSredniWynik(uzytkownik.getId());
            raportText.append("\nŚredni wynik:\n");
            raportText.append("------------\n");
            raportText.append(df.format(sredniWynikQuizow * 100)).append("%\n");

            raportText.append("\nNajlepsze wyniki w kategoriach:\n");
            raportText.append("------------------------------\n");
            for (int idKategorii = 1; idKategorii <= kategorieMap.size(); idKategorii++) {
                QuizZamkniety najlepszy = raport.pobierzNajlepszyWynikWKategorii(uzytkownik.getId(), idKategorii);
                if (najlepszy != null) {
                    String nazwaKategorii = kategorieMap.getOrDefault(idKategorii, "Nieznana kategoria");
                    raportText.append(nazwaKategorii).append(": ").append(najlepszy.getWynik())
                            .append("/").append(najlepszy.getLiczbaPytan())
                            .append(" (Data: ").append(najlepszy.getDataProby()).append(")\n");
                }
            }

            raportText.append("\n\nQuizy otwarte\n");
            raportText.append("=============\n\n");

            List<QuizOtwarty> historiaSesjiWpisywania = raport.pobierzHistorieSesjiWpisywania(uzytkownik.getId(), 3);
            raportText.append("Ostatnie 3 sesje:\n");
            raportText.append("---------------\n");
            if (historiaSesjiWpisywania != null && !historiaSesjiWpisywania.isEmpty()) {
                for (QuizOtwarty s : historiaSesjiWpisywania) {
                    String nazwaKategorii = kategorieMap.getOrDefault(s.getIdKategorii(), "Nieznana kategoria");
                    raportText.append("Data: ").append(s.getDataSesji())
                            .append(", Kategoria: ").append(nazwaKategorii)
                            .append(", Wynik: ").append(s.getWynik()).append("/").append(s.getLiczbaPytan()).append("\n");
                }
            } else {
                raportText.append("Brak historii sesji otwartych pytań.\n");
            }

            double sredniWynikWpisywania = raport.pobierzSredniWynikWpisywania(uzytkownik.getId());
            raportText.append("\nŚredni wynik:\n");
            raportText.append("------------\n");
            raportText.append(df.format(sredniWynikWpisywania * 100)).append("%\n");

            raportText.append("\nNajlepsze wyniki w kategoriach:\n");
            raportText.append("------------------------------\n");
            for (int idKategorii = 1; idKategorii <= kategorieMap.size(); idKategorii++) {
                QuizOtwarty najlepszy = raport.pobierzNajlepszyWynikWpisywaniaWKategorii(uzytkownik.getId(), idKategorii);
                if (najlepszy != null) {
                    String nazwaKategorii = kategorieMap.getOrDefault(idKategorii, "Nieznana kategoria");
                    raportText.append(nazwaKategorii).append(": ").append(najlepszy.getWynik())
                            .append("/").append(najlepszy.getLiczbaPytan())
                            .append(" (Data: ").append(najlepszy.getDataSesji()).append(")\n");
                }
            }

            raportTextArea.setText(raportText.toString());
            raportTextArea.setCaretPosition(0);
            raportTextArea.setVisible(true);
            scrollPane.setVisible(true);
            raportButton.setVisible(false);
            ukryjRaportButton.setVisible(true);
        } catch (SQLException e) {
            pokazKomunikat("Błąd podczas pobierania raportu: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}