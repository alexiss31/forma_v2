package main.java.m2l.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import javax.xml.parsers.*;
import main.java.m2l.services.AdherentService;
import main.java.m2l.model.Adherent;
import org.w3c.dom.*;
import java.util.List;
import java.util.ArrayList;

public class CreateAdherentFrame extends JFrame {
    private final Map<String, JTextField> champsTextuels = new HashMap<>();
    private JComboBox<String> categoryComboBox; // ComboBox pour les catégories
    private final AdherentService adherentService = new AdherentService(); // Service pour sauvegarde

    public CreateAdherentFrame() {
        setTitle("Cercle d'Escrime de Rouen - Inscription 2022-2023");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Titre
        JPanel headerPanel = createHeaderPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(headerPanel, gbc);

        gbc.gridy = 1; // Espace après le titre
        gbc.gridwidth = 1;

        // Liste des champs obligatoires
        String[] labelsObligatoires = {
                "Nom", "Nom de naissance", "Prénom", "Genre", "Naissance (JJ/MM/AAAA)",
                "Nationalité", "Adresse", "Code postal", "Ville",
                "Téléphone 1", "Courriel", "Nom et prénom du responsable légal"
        };

        // Ajout des champs obligatoires
        int row = 2;
        for (String label : labelsObligatoires) {
            gbc.gridx = 0;
            gbc.gridy = row;
            add(new JLabel(label + " *"), gbc);

            gbc.gridx = 1;
            JTextField textField = new JTextField(20);
            add(textField, gbc);
            champsTextuels.put(label, textField);
            row++;
        }

        // Armes Pratique
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Armes Pratique:"), gbc);
        String[] armesOptions = {"Fleuret", "Épée", "Sabre"};
        gbc.gridx = 1;
        add(new JComboBox<>(armesOptions), gbc);
        row++;

        // Latéralité
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Latéralité:"), gbc);
        String[] lateraliteOptions = {"Droitier", "Gaucher"};
        gbc.gridx = 1;
        add(new JComboBox<>(lateraliteOptions), gbc);
        row++;

        // Sélection de la catégorie
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Catégorie:"), gbc);
        categoryComboBox = new JComboBox<>(loadCategories()); // Charger les catégories XML
        gbc.gridx = 1;
        add(categoryComboBox, gbc);
        row++;

        // Panneau des boutons
        JPanel buttonPanel = createButtonPanel();
        gbc.gridx = 0;
        gbc.gridy = row + 1;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String[] loadCategories() {
        List<String> categories = new ArrayList<>();
        try {
            File file = new File("C:/Users/HEUGASA/IdeaProjetcs/forma_v2/src/main/resources/categories.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("categorie");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String nomCategorie = element.getElementsByTagName("nom").item(0).getTextContent();
                categories.add(nomCategorie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories.toArray(new String[0]);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton submitButton = new JButton("Envoyer formulaire");
        submitButton.setPreferredSize(new Dimension(150, 30));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validerFormulaire();
            }
        });
        panel.add(submitButton);
        return panel;
    }

    private void validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        for (Map.Entry<String, JTextField> entry : champsTextuels.entrySet()) {
            String nomChamp = entry.getKey();
            JTextField champ = entry.getValue();
            if (!nomChamp.equals("Téléphone 2") && champ.getText().trim().isEmpty()) {
                erreurs.append("- ").append(nomChamp).append(" est obligatoire.\n");
            }
        }

        if (erreurs.length() > 0) {
            JOptionPane.showMessageDialog(this, "Veuillez compléter les champs suivants :\n" + erreurs,
                    "Erreur de validation", JOptionPane.ERROR_MESSAGE);
        } else {
            String nom = champsTextuels.get("Nom").getText();
            String prenom = champsTextuels.get("Prénom").getText();
            String genre = champsTextuels.get("Genre").getText();
            String naissance = champsTextuels.get("Naissance (JJ/MM/AAAA)").getText();
            String nationalite = champsTextuels.get("Nationalité").getText();
            String adresse = champsTextuels.get("Adresse").getText();
            String codePostal = champsTextuels.get("Code postal").getText();
            String ville = champsTextuels.get("Ville").getText();
            String telephone1 = champsTextuels.get("Téléphone 1").getText();
            String courriel = champsTextuels.get("Courriel").getText();
            String responsableLegal = champsTextuels.get("Nom et prénom du responsable légal").getText();
            String categorie = (String) categoryComboBox.getSelectedItem();

            Adherent adherent = new Adherent(nom, prenom, genre, naissance, nationalite, adresse, codePostal, ville,
                    telephone1, courriel, responsableLegal, "", "", categorie);

            adherentService.ajouterAdherent(adherent);
            JOptionPane.showMessageDialog(this, "Adhérent ajouté avec succès !");
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreateAdherentFrame::new);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Cercle d'Escrime de Rouen - Inscription 2022-2023", SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }
}
