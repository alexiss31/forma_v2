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

    // Couleurs de l'application
    private static final Color PRIMARY_COLOR = new Color(24, 56, 120); // Bleu foncé pour l'escrime
    private static final Color SECONDARY_COLOR = new Color(220, 53, 69); // Rouge pour l'accent
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250); // Fond léger
    private static final Color BUTTON_HOVER_COLOR = new Color(240, 240, 245); // Couleur au survol

    // Police personnalisée
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    private final AdherentService adherentService = new AdherentService();

    public SearchAdherentFrame() {
        setTitle("Cercle d'Escrime de Rouen - Rechercher un Adhérent");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        // Panneau pour le titre avec bouton retour
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Panneau principal pour le contenu
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Champ de recherche
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Bouton de recherche
        JButton searchButton = createStyledButton("Rechercher");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = searchField.getText().trim();

                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(SearchAdherentFrame.this,
                            "Veuillez saisir un nom d'adhérent",
                            "Champ vide",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Adherent adherent = adherentService.rechercherParNom(nom);

                if (adherent != null) {
                    JOptionPane.showMessageDialog(SearchAdherentFrame.this,
                            "Nom: " + adherent.getNom() + "\nPrénom: " + adherent.getPrenom(),
                            "Résultat de la recherche",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(SearchAdherentFrame.this,
                            "Adhérent non trouvé.",
                            "Résultat de la recherche",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ajout des composants au panneau principal
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Label de recherche
        JLabel searchLabel = new JLabel("Nom de l'adhérent à rechercher :");
        searchLabel.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(searchLabel, gbc);

        // Champ de recherche
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(searchField, gbc);

        // Bouton de recherche
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(searchButton, gbc);

        add(contentPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Bouton retour
        JButton backButton = new JButton("← Retour");
        backButton.setFont(BUTTON_FONT);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(PRIMARY_COLOR);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setForeground(SECONDARY_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setForeground(Color.WHITE);
            }
        });

        // Titre
        JLabel titleLabel = new JLabel("Rechercher un Adhérent");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(backButton, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.BLACK); // Texte blanc par défaut
        button.setBackground(PRIMARY_COLOR); // Fond bleu foncé par défaut
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setPreferredSize(new Dimension(120, 35));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR); // Fond rouge au survol
                button.setForeground(Color.BLACK); // Texte reste blanc
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR); // Retour au fond bleu foncé
                button.setForeground(Color.BLACK); // Texte reste blanc
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new SearchAdherentFrame();
            }
        });
    }
}