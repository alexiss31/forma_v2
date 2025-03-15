package main.java.m2l.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import main.java.m2l.services.AdherentService;
import main.java.m2l.model.Adherent;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.InputStream;

public class ModifyAdherentFrame extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(24, 56, 120);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16);

    private final AdherentService adherentService = new AdherentService();
    private JTextField searchField;
    private JPanel formPanel;
    private JTextField[] fields;
    private JLabel categoryLabel;
    private Adherent adherent;
    private JScrollPane scrollPane; // Ajout d'un scrollPane pour accommoder le formulaire

    public ModifyAdherentFrame() {
        setTitle("Modifier un Adhérent");
        setSize(850, 750);  // Augmentation de la taille de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSearchPanel(), BorderLayout.CENTER);

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

        JLabel titleLabel = new JLabel("Modifier un Adhérent", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        panel.add(backButton, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel searchPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Nom de l'adhérent :");
        searchLabel.setFont(LABEL_FONT);
        searchField = new JTextField(30); // Augmentation de la largeur
        JButton searchButton = createStyledButton("Rechercher");
        searchButton.addActionListener(e -> rechercherAdherent());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        formPanel.setBackground(Color.WHITE);

        // Ajout d'un scrollPane pour accommoder le formulaire complet
        scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private void rechercherAdherent() {
        String nom = searchField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nom", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        adherent = adherentService.rechercherParNom(nom);
        formPanel.removeAll();

        if (adherent != null) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(8, 10, 8, 10);
            gbc.weightx = 1; // Permet d'étendre les champs sur toute la largeur

            String[] labels = {"Nom", "Prénom", "Genre", "Naissance (AAAA-MM-JJ)", "Nationalité", "Adresse",
                    "Code Postal", "Ville", "Téléphone", "Email", "Responsable Légal", "Arme Pratiquée", "Latéralité"};

            String[] values = {adherent.getNom(), adherent.getPrenom(), adherent.getGenre(), adherent.getNaissance(),
                    adherent.getNationalite(), adherent.getAdresse(), adherent.getCodePostal(), adherent.getVille(),
                    adherent.getTelephone1(), adherent.getCourriel(), adherent.getResponsableLegal(),
                    adherent.getArmesPratique(), adherent.getLateralite()};

            fields = new JTextField[labels.length];

            for (int i = 0; i < labels.length; i++) {
                JLabel label = new JLabel(labels[i] + " :");
                label.setFont(LABEL_FONT);
                gbc.gridx = 0;
                gbc.gridy = i;
                gbc.gridwidth = 1;
                formPanel.add(label, gbc);

                fields[i] = new JTextField(values[i], 30); // Champs plus larges
                gbc.gridx = 1;
                gbc.gridwidth = 2; // Étendre les champs
                formPanel.add(fields[i], gbc);
            }

            // Déterminer la catégorie
            int anneeNaissance = Integer.parseInt(adherent.getNaissance().split("-")[0]);
            String categorie = getCategorieFromXml(anneeNaissance);
            categoryLabel = new JLabel("Catégorie : " + categorie);
            categoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
            gbc.gridx = 0;
            gbc.gridy = labels.length;
            gbc.gridwidth = 3;
            formPanel.add(categoryLabel, gbc);

            // Ajout d'un panel pour contenir le bouton avec un padding adéquat
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setBorder(new EmptyBorder(25, 0, 25, 0)); // Padding vertical important

            // Bouton sauvegarde bien visible avec une taille plus grande
            JButton saveButton = createStyledButton("Sauvegarder les modifications");
            saveButton.setPreferredSize(new Dimension(280, 50)); // Bouton plus grand
            saveButton.addActionListener(e -> sauvegarderAdherent());
            buttonPanel.add(saveButton);

            // Ajout du panel bouton
            gbc.gridy++;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            formPanel.add(buttonPanel, gbc);

            // Ajout d'un spacer en bas pour garantir que le bouton est bien visible même au bas du formulaire
            JPanel spacer = new JPanel();
            spacer.setPreferredSize(new Dimension(1, 20));
            spacer.setBackground(Color.WHITE);
            gbc.gridy++;
            formPanel.add(spacer, gbc);
        } else {
            formPanel.add(new JLabel("Aucun adhérent trouvé.", JLabel.CENTER));
        }

        formPanel.revalidate();
        formPanel.repaint();

        // Remonter le scrollPane au début
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });
    }

    private void sauvegarderAdherent() {
        Adherent nouvelAdherent = new Adherent(
                fields[0].getText(), fields[1].getText(), fields[2].getText(), fields[3].getText(),
                fields[4].getText(), fields[5].getText(), fields[6].getText(), fields[7].getText(),
                fields[8].getText(), fields[9].getText(), fields[10].getText(), fields[11].getText(),
                fields[12].getText()
        );

        adherentService.modifierAdherent(adherent.getNom(), nouvelAdherent);
        JOptionPane.showMessageDialog(this, "Adhérent modifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private String getCategorieFromXml(int anneeNaissance) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("categories.xml");
            if (inputStream == null) return "Non défini";

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("categorie");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                int anneeMin = Integer.parseInt(element.getElementsByTagName("annee_min").item(0).getTextContent());
                int anneeMax = Integer.parseInt(element.getElementsByTagName("annee_max").item(0).getTextContent());

                if (anneeNaissance >= anneeMin && anneeNaissance <= anneeMax) {
                    return element.getElementsByTagName("nom").item(0).getTextContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Non défini";
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(new Dimension(200, 45)); // Bouton plus large
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.BLACK);
        button.setBorderPainted(true); // Ajout d'une bordure pour meilleure visibilité
        button.setFocusPainted(false);
        return button;
    }
}