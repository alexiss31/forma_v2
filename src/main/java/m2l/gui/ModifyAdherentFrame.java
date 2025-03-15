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

public class ModifyAdherentFrame extends JFrame {

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
    private JPanel contentPanel;
    private JTextField searchField;
    private JPanel formPanel;

    public ModifyAdherentFrame() {
        setTitle("Cercle d'Escrime de Rouen - Modifier un Adhérent");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        // Panneau pour le titre avec bouton retour
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Panneau principal pour le contenu
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Panneau de recherche
        JPanel searchPanel = createSearchPanel();
        contentPanel.add(searchPanel, BorderLayout.NORTH);

        // Panneau pour le formulaire (sera rempli après la recherche)
        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        contentPanel.add(formPanel, BorderLayout.CENTER);

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
        JLabel titleLabel = new JLabel("Modifier un Adhérent");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(backButton, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Label de recherche
        JLabel searchLabel = new JLabel("Nom de l'adhérent à modifier :");
        searchLabel.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(searchLabel, gbc);

        // Champ de recherche
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        panel.add(searchField, gbc);

        // Bouton de recherche
        JButton searchButton = createStyledButton("Rechercher");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rechercherAdherent();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        panel.add(searchButton, gbc);

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

        // Vider le panneau de formulaire
        formPanel.removeAll();

        if (adherent != null) {
            // Créer un panneau pour le formulaire
            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(Color.WHITE);
            form.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(8, 5, 8, 5);
            gbc.anchor = GridBagConstraints.WEST;

            // Champs de formulaire
            JTextField nameField = addFormField(form, "Nom :", adherent.getNom(), gbc, 0);
            JTextField prenomField = addFormField(form, "Prénom :", adherent.getPrenom(), gbc, 1);
            // Ajouter d'autres champs selon les propriétés de votre classe Adherent

            // Panneau pour les boutons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);

            JButton saveButton = createStyledButton("Sauvegarder");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Modifier l'adhérent et mettre à jour
                    adherent.setNom(nameField.getText());
                    adherent.setPrenom(prenomField.getText());
                    // Mettre à jour les autres informations...

                    // Récupérer la catégorie actuelle ou nouvelle
                    String categorie = adherent.getCategorie();
                    if (categoryComboBox != null) {
                        categorie = (String) categoryComboBox.getSelectedItem();
                    }

                    // Création d'un nouvel adhérent avec les nouvelles valeurs
                    Adherent nouvelAdherent = new Adherent(
                            nameField.getText(), // Nom
                            prenomField.getText(), // Prénom
                            adherent.getGenre(), // Genre (on garde l'ancien)
                            adherent.getNaissance(), // Naissance
                            adherent.getNationalite(), // Nationalité
                            adherent.getAdresse(), // Adresse
                            adherent.getCodePostal(), // Code Postal
                            adherent.getVille(), // Ville
                            adherent.getTelephone1(), // Téléphone (ancien ou nouveau)
                            adherent.getCourriel(), // Email (ancien ou nouveau)
                            adherent.getResponsableLegal(), // Responsable légal
                            adherent.getArmesPratique(), // Arme pratiquée
                            adherent.getLateralite(), // Latéralité
                            categorie);

// Appel de la modification avec l'ancien nom et le nouvel adhérent
                    adherentService.modifierAdherent(adherent.getNom(), nouvelAdherent);

                    JOptionPane.showMessageDialog(
                            ModifyAdherentFrame.this,
                            "Adhérent modifié avec succès !",
                            "Modification réussie",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    JOptionPane.showMessageDialog(
                            ModifyAdherentFrame.this,
                            "Adhérent modifié avec succès !",
                            "Modification réussie",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Vider le formulaire et le champ de recherche
                    formPanel.removeAll();
                    searchField.setText("");
                    formPanel.revalidate();
                    formPanel.repaint();
                }
            });

            buttonPanel.add(saveButton);

            JButton cancelButton = new JButton("Annuler");
            cancelButton.setFont(BUTTON_FONT);
            cancelButton.setPreferredSize(new Dimension(120, 35));
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Vider le formulaire
                    formPanel.removeAll();
                    searchField.setText("");
                    formPanel.revalidate();
                    formPanel.repaint();
                }
            });

            buttonPanel.add(cancelButton);

            // Ajouter le panneau de boutons au formulaire
            gbc.gridx = 0;
            gbc.gridy = 10; // Position après tous les champs
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.EAST;
            form.add(buttonPanel, gbc);

            formPanel.add(form);
        } else {
            JPanel notFoundPanel = new JPanel(new BorderLayout());
            notFoundPanel.setBackground(Color.WHITE);
            notFoundPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            JLabel notFoundLabel = new JLabel("Adhérent non trouvé.", JLabel.CENTER);
            notFoundLabel.setFont(new Font("Arial", Font.BOLD, 14));
            notFoundLabel.setForeground(SECONDARY_COLOR);
            notFoundPanel.add(notFoundLabel, BorderLayout.CENTER);

            formPanel.add(notFoundPanel);
        }

        formPanel.revalidate();
        formPanel.repaint();
    }

    private JTextField addFormField(JPanel panel, String labelText, String value, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(label, gbc);

        JTextField field = new JTextField(value, 20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.7;
        panel.add(field, gbc);

        return field;
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
                new ModifyAdherentFrame();
            }
        });
    }
}