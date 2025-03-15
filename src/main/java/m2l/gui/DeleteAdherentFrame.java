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

public class DeleteAdherentFrame extends JFrame {

    // Couleurs de l'application
    private static final Color PRIMARY_COLOR = new Color(24, 56, 120); // Bleu foncé pour l'escrime
    private static final Color SECONDARY_COLOR = new Color(220, 53, 69); // Rouge pour l'accent
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250); // Fond léger
    private static final Color WARNING_COLOR = new Color(255, 87, 34); // Orange pour avertissement

    // Police personnalisée
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    private final AdherentService adherentService = new AdherentService();
    private JTextField searchField;
    private JPanel resultPanel;

    public DeleteAdherentFrame() {
        setTitle("Cercle d'Escrime de Rouen - Supprimer un Adhérent");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        // En-tête avec titre et bouton retour
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Panneau de recherche
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Panneau pour afficher le résultat de recherche
        resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBackground(BACKGROUND_COLOR);
        resultPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        mainPanel.add(resultPanel, BorderLayout.CENTER);

        // Message d'avertissement
        JPanel warningPanel = createWarningPanel();
        mainPanel.add(warningPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

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
        JLabel titleLabel = new JLabel("Supprimer un Adhérent");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(backButton, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Label de recherche
        JLabel searchLabel = new JLabel("Nom de l'adhérent à supprimer :");
        searchLabel.setFont(LABEL_FONT);
        panel.add(searchLabel, BorderLayout.WEST);

        // Champ de recherche et bouton dans un sous-panneau
        JPanel searchInputPanel = new JPanel(new BorderLayout(10, 0));
        searchInputPanel.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchInputPanel.add(searchField, BorderLayout.CENTER);

        JButton searchButton = createStyledButton("Rechercher");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rechercherAdherent();
            }
        });
        searchInputPanel.add(searchButton, BorderLayout.EAST);

        panel.add(searchInputPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createWarningPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 243, 224)); // Fond orange clair
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(WARNING_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel warningLabel = new JLabel("<html><b>Attention :</b> La suppression d'un adhérent est définitive et ne peut pas être annulée.</html>");
        warningLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        warningLabel.setForeground(WARNING_COLOR);

        panel.add(warningLabel, BorderLayout.CENTER);

        return panel;
    }

    private void rechercherAdherent() {
        String nom = searchField.getText().trim();

        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez saisir un nom d'adhérent",
                    "Champ vide",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Adherent adherent = adherentService.rechercherParNom(nom);
        resultPanel.removeAll();

        if (adherent != null) {
            // Afficher les informations de l'adhérent trouvé
            JPanel adherentPanel = new JPanel(new BorderLayout(0, 15));
            adherentPanel.setBackground(Color.WHITE);
            adherentPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            // Afficher les informations
            JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 10));
            infoPanel.setBackground(Color.WHITE);

            infoPanel.add(createInfoLabel("Adhérent trouvé :"));
            infoPanel.add(createInfoField("Nom : " + adherent.getNom()));
            infoPanel.add(createInfoField("Prénom : " + adherent.getPrenom()));
            // Ajouter d'autres champs si nécessaire...

            adherentPanel.add(infoPanel, BorderLayout.CENTER);

            // Bouton de suppression
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);

            JButton deleteButton = new JButton("Supprimer");
            deleteButton.setFont(BUTTON_FONT);
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(WARNING_COLOR);
            deleteButton.setFocusPainted(false);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            deleteButton.setPreferredSize(new Dimension(120, 35));

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(
                            DeleteAdherentFrame.this,
                            "Êtes-vous sûr de vouloir supprimer l'adhérent " +
                                    adherent.getPrenom() + " " + adherent.getNom() + " ?",
                            "Confirmation de suppression",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        adherentService.supprimerAdherent(adherent.getNom());
                        JOptionPane.showMessageDialog(
                                DeleteAdherentFrame.this,
                                "L'adhérent a été supprimé avec succès.",
                                "Suppression réussie",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        // Vider le panneau et le champ de recherche
                        resultPanel.removeAll();
                        searchField.setText("");
                        resultPanel.revalidate();
                        resultPanel.repaint();
                    }
                }
            });

            buttonPanel.add(deleteButton);
            adherentPanel.add(buttonPanel, BorderLayout.SOUTH);

            resultPanel.add(adherentPanel, BorderLayout.CENTER);
        } else {
            // Adhérent non trouvé
            JPanel notFoundPanel = new JPanel(new BorderLayout());
            notFoundPanel.setBackground(Color.WHITE);
            notFoundPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            JLabel notFoundLabel = new JLabel("Aucun adhérent trouvé avec ce nom.", JLabel.CENTER);
            notFoundLabel.setFont(new Font("Arial", Font.BOLD, 14));
            notFoundLabel.setForeground(SECONDARY_COLOR);

            notFoundPanel.add(notFoundLabel, BorderLayout.CENTER);
            resultPanel.add(notFoundPanel, BorderLayout.CENTER);
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JLabel createInfoField(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
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
                new DeleteAdherentFrame();
            }
        });
    }
}