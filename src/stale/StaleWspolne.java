package stale;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StaleWspolne extends JFrame {
    public StaleWspolne(String title) {
        setTitle(title);
        setSize(520, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(StaleKolory.KOLOR_GLOWNY);
        setResizable(false);
    }

    protected JLabel stworzEtykiete(String tekst, int x, int y, int width, int height, int fontSize) {
        JLabel label = new JLabel(tekst);
        label.setBounds(x, y, width, height);
        label.setFont(StaleCzcionki.CZCIONKA_TEKST);
        label.setForeground(StaleKolory.KOLOR_TEKSTU);
        return label;
    }

    protected JTextField stworzPoleTekstowe(int x, int y, int width, int height) {
        JTextField field = new JTextField();
        field.setBounds(x, y, width, height);
        field.setBackground(StaleKolory.KOLOR_TLA);
        field.setForeground(StaleKolory.KOLOR_TEKSTU);
        field.setFont(StaleCzcionki.CZCIONKA_TEKST);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return field;
    }

    protected JPasswordField stworzPoleHasla(int x, int y, int width, int height) {
        JPasswordField field = new JPasswordField();
        field.setBounds(x, y, width, height);
        field.setBackground(StaleKolory.KOLOR_TLA);
        field.setForeground(StaleKolory.KOLOR_TEKSTU);
        field.setFont(StaleCzcionki.CZCIONKA_TEKST);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return field;
    }

    protected JButton stworzPrzycisk(String tekst, int x, int y, int width, int height) {
        JButton button = new JButton(tekst);
        button.setBounds(x, y, width, height);
        button.setBackground(StaleKolory.KOLOR_AKCENTU);
        button.setForeground(StaleKolory.KOLOR_TEKSTU_PRZYCISKU);
        button.setFont(StaleCzcionki.CZCIONKA_PRZYCISK);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(StaleKolory.KOLOR_NAJECHANIA);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(StaleKolory.KOLOR_AKCENTU);
            }
        });

        return button;
    }

    protected void pokazKomunikat(String wiadomosc, String tytul, int typ) {
        JOptionPane.showMessageDialog(this, wiadomosc, tytul, typ);
    }
}