package main.java.m2l.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.*;
import javax.xml.parsers.*;
import main.java.m2l.services.AdherentService;
import main.java.m2l.model.Adherent;
import org.w3c.dom.*;
import java.util.List;
import java.util.ArrayList;

public class CreateAdherentFrame extends JFrame {
    // Constantes de style identiques à ModifyAdherentFrame
    private static final Color PRIMARY_COLOR = new Color(24, 56, 120);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16);

    private final Map<String, JComponent> champsForm = new HashMap<>();
    private final AdherentService adherentService = new AdherentService();

    public CreateAdherentFrame() {
        setTitle("Cercle d'Escrime de Rouen - Inscription");
        setSize(800, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Ajout des composants principaux
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);

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

        JLabel titleLabel = new JLabel("Inscription d'un Nouvel Adhérent", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        panel.add(backButton, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        // Création du conteneur principal
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        containerPanel.setBackground(BACKGROUND_COLOR);

        // Création du panneau pour le formulaire avec GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.weightx = 1.0;
        gbc.gridwidth = 1;

        // En-tête du formulaire
        JLabel formHeaderLabel = new JLabel("Informations de l'adhérent", SwingConstants.LEFT);
        formHeaderLabel.setFont(new Font("Arial", Font.BOLD, 17));
        formHeaderLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(formHeaderLabel, gbc);

        gbc.gridy++;
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        formPanel.add(separator, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Liste des champs obligatoires
        String[] labelsObligatoires = {
                "Nom", "Prénom", "Genre", "Naissance (AAAA-MM-JJ)",
                "Nationalité", "Adresse", "Code Postal", "Ville",
                "Téléphone", "Courriel", "Responsable Légal"
        };

        // Ajout des champs obligatoires
        int row = gbc.gridy;
        for (String label : labelsObligatoires) {
            JLabel fieldLabel = new JLabel(label + " *");
            fieldLabel.setFont(LABEL_FONT);
            fieldLabel.setForeground(new Color(50, 50, 50));

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.anchor = GridBagConstraints.WEST;
            formPanel.add(fieldLabel, gbc);

            gbc.gridx = 1;
            JTextField textField = new JTextField(20);
            textField.setPreferredSize(new Dimension(250, 30));
            formPanel.add(textField, gbc);
            champsForm.put(label, textField);
            row++;
        }

        // Armes Pratique
        JLabel armesLabel = new JLabel("Armes Pratiquée *");
        armesLabel.setFont(LABEL_FONT);
        armesLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(armesLabel, gbc);

        String[] armesOptions = {"Fleuret", "Épée", "Sabre"};
        JComboBox<String> armesComboBox = new JComboBox<>(armesOptions);
        armesComboBox.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        formPanel.add(armesComboBox, gbc);
        champsForm.put("Armes Pratiquée", armesComboBox);
        row++;

        // Latéralité
        JLabel lateraliteLabel = new JLabel("Latéralité *");
        lateraliteLabel.setFont(LABEL_FONT);
        lateraliteLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lateraliteLabel, gbc);

        String[] lateraliteOptions = {"Droitier", "Gaucher"};
        JComboBox<String> lateraliteComboBox = new JComboBox<>(lateraliteOptions);
        lateraliteComboBox.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        formPanel.add(lateraliteComboBox, gbc);
        champsForm.put("Latéralité", lateraliteComboBox);
        row++;

        // Note sur les champs obligatoires
        JLabel noteLabel = new JLabel("* Champs obligatoires");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        noteLabel.setForeground(Color.GRAY);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 8, 10);
        formPanel.add(noteLabel, gbc);

        // Bouton d'envoi dans un panneau dédié
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(25, 0, 10, 0));

        JButton submitButton = createStyledButton("Enregistrer l'adhérent");
        submitButton.setPreferredSize(new Dimension(280, 50));
        submitButton.addActionListener(e -> validerFormulaire());
        buttonPanel.add(submitButton);

        gbc.gridy = row + 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 10, 10, 10);
        formPanel.add(buttonPanel, gbc);

        // Création du ScrollPane pour s'adapter à toutes les tailles d'écran
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private void validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        // Vérification des champs texte
        for (Map.Entry<String, JComponent> entry : champsForm.entrySet()) {
            String nomChamp = entry.getKey();
            JComponent comp = entry.getValue();

            if (comp instanceof JTextField) {
                JTextField champ = (JTextField) comp;
                if (champ.getText().trim().isEmpty()) {
                    erreurs.append("- ").append(nomChamp).append(" est obligatoire.\n");
                }
            }
        }

        if (erreurs.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez compléter les champs suivants :\n" + erreurs,
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            // Récupération des valeurs
            String nom = ((JTextField) champsForm.get("Nom")).getText();
            String prenom = ((JTextField) champsForm.get("Prénom")).getText();
            String genre = ((JTextField) champsForm.get("Genre")).getText();
            String naissance = ((JTextField) champsForm.get("Naissance (AAAA-MM-JJ)")).getText();
            String nationalite = ((JTextField) champsForm.get("Nationalité")).getText();
            String adresse = ((JTextField) champsForm.get("Adresse")).getText();
            String codePostal = ((JTextField) champsForm.get("Code Postal")).getText();
            String ville = ((JTextField) champsForm.get("Ville")).getText();
            String telephone = ((JTextField) champsForm.get("Téléphone")).getText();
            String courriel = ((JTextField) champsForm.get("Courriel")).getText();
            String responsableLegal = ((JTextField) champsForm.get("Responsable Légal")).getText();
            String armesPratique = ((JComboBox<?>) champsForm.get("Armes Pratiquée")).getSelectedItem().toString();
            String lateralite = ((JComboBox<?>) champsForm.get("Latéralité")).getSelectedItem().toString();

            // Création et sauvegarde de l'adhérent
            Adherent adherent = new Adherent(nom, prenom, genre, naissance, nationalite, adresse, codePostal, ville,
                    telephone, courriel, responsableLegal, armesPratique, lateralite);

            adherentService.ajouterAdherent(adherent);

            // Message de confirmation avec style
            JOptionPane.showMessageDialog(this,
                    "Adhérent ajouté avec succès !",
                    "Inscription réussie",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.BLACK);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        return button;
    }

    private String[] loadCategories() {
        List<String> categories = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("categories.xml");
            if (inputStream == null) return new String[]{"Non défini"};

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("categorie");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String nomCategorie = element.getElementsByTagName("nom").item(0).getTextContent();
                categories.add(nomCategorie);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"Non défini"};
        }
        return categories.toArray(new String[0]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreateAdherentFrame::new);
    }
}