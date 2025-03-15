package main.java.m2l.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import main.java.m2l.services.AdherentService;
import main.java.m2l.model.Adherent;

public class SearchAdherentFrame extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(24, 56, 120); // Bleu foncé pour l'escrime
    private static final Color SECONDARY_COLOR = new Color(220, 53, 69); // Rouge pour l'accent
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250); // Fond léger
    private static final Color BUTTON_HOVER_COLOR = new Color(240, 240, 245); // Couleur au survol

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    private final AdherentService adherentService = new AdherentService();
    private JTextField searchField;
    private JTextArea resultArea;

    public SearchAdherentFrame() {
        setTitle("Rechercher un Adhérent");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JButton backButton = new JButton("← Retour");
        backButton.setFont(BUTTON_FONT);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(PRIMARY_COLOR);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> dispose());

        JLabel titleLabel = new JLabel("Rechercher un Adhérent", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        panel.add(backButton, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel searchPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Nom de l'adhérent :");
        searchLabel.setFont(LABEL_FONT);
        searchField = new JTextField(20);
        JButton searchButton = createStyledButton("Rechercher");
        searchButton.addActionListener(e -> rechercherAdherent());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        resultArea = new JTextArea(12, 40);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void rechercherAdherent() {
        String nom = searchField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nom", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Adherent adherent = adherentService.rechercherParNom(nom);
        if (adherent != null) {
            String adherentInfo = """
                    Nom : %s
                    Prénom : %s
                    Genre : %s
                    Date de naissance : %s
                    Nationalité : %s
                    Adresse : %s
                    Code Postal : %s
                    Ville : %s
                    Téléphone : %s
                    Email : %s
                    Responsable Légal : %s
                    Arme Pratiquée : %s
                    Latéralité : %s
                    """.formatted(
                    adherent.getNom(), adherent.getPrenom(), adherent.getGenre(), adherent.getNaissance(),
                    adherent.getNationalite(), adherent.getAdresse(), adherent.getCodePostal(), adherent.getVille(),
                    adherent.getTelephone1(), adherent.getCourriel(), adherent.getResponsableLegal(),
                    adherent.getArmesPratique(), adherent.getLateralite()
            );

            resultArea.setText(adherentInfo);
        } else {
            resultArea.setText("Aucun adhérent trouvé.");
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(new Dimension(150, 35));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.BLACK);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SearchAdherentFrame();
        });
    }
}
