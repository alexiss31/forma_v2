package main.java.m2l.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuFrame extends JFrame {

    // Couleurs de l'application
    private static final Color PRIMARY_COLOR = new Color(24, 56, 120); // Bleu foncé pour l'escrime
    private static final Color SECONDARY_COLOR = new Color(220, 53, 69); // Rouge pour l'accent
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250); // Fond léger
    private static final Color BUTTON_HOVER_COLOR = new Color(240, 240, 245); // Couleur au survol

    // Police personnalisée
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public MainMenuFrame() {
        setTitle("Cercle d'Escrime de Rouen - Gestion des Adhérents");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(BACKGROUND_COLOR);

        // Utilisation d'un BorderLayout comme layout principal
        setLayout(new BorderLayout(10, 10));

        // Panneau pour le titre avec logo
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Panneau principal pour les boutons
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Panneau de pied de page
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Centre la fenêtre
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Titre principal
        JLabel titleLabel = new JLabel("Gestion des Adhérents");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Création des boutons avec icônes (simulations d'icônes avec Unicode)
        mainPanel.add(createMenuButton("➕ Créer un Adhérent",
                e -> new CreateAdherentFrame()));

        mainPanel.add(createMenuButton("✏️ Modifier un Adhérent",
                e -> new ModifyAdherentFrame()));

        mainPanel.add(createMenuButton("🔍 Rechercher un Adhérent",
                e -> new SearchAdherentFrame()));

        mainPanel.add(createMenuButton("📋 Lister les Adhérents",
                e -> new ListAdherentFrame()));

        mainPanel.add(createMenuButton("❌ Supprimer un Adhérent",
                e -> new DeleteAdherentFrame()));

        mainPanel.add(createMenuButton("📄 Générer Formulaire PDF",
                e -> JOptionPane.showMessageDialog(this, "Fonctionnalité PDF à implémenter.")));

        return mainPanel;
    }

    private JButton createMenuButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(PRIMARY_COLOR);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Effet de survol
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER_COLOR);
                button.setForeground(SECONDARY_COLOR);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(PRIMARY_COLOR);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        button.addActionListener(action);
        return button;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(PRIMARY_COLOR);
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel footerLabel = new JLabel("© 2025 Cercle d'Escrime de Rouen - Tous droits réservés");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        return footerPanel;
    }

    public static void main(String[] args) {
        // Utilisation de look and feel système pour une meilleure intégration
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new MainMenuFrame());
    }
}