package main.java.m2l.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.regex.Pattern;

import main.java.m2l.services.AdherentService;
import main.java.m2l.model.Adherent;

/**
 * Fenêtre d'affichage de la liste des adhérents
 */
public class ListAdherentFrame extends JFrame {

    private final AdherentService adherentService = new AdherentService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public ListAdherentFrame() {
        setTitle("Liste des Adhérents");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Création du panneau d'en-tête
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Création du tableau d'adhérents
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        // Création du panneau de boutons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        loadAdherents();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // Titre
        JLabel titleLabel = new JLabel("Liste des Adhérents");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.WEST);

        // Champ de recherche
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
        });

        JLabel searchLabel = new JLabel("Rechercher: ");
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Création du modèle de table
        String[] columnNames = {"Nom", "Prénom", "Téléphone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre les cellules non éditables
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        // Bouton Retour
        JButton backButton = new JButton("← Retour");
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(e -> dispose());

        // Bouton Actualiser
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.addActionListener(e -> loadAdherents());

        panel.add(refreshButton);
        panel.add(backButton);

        return panel;
    }

    private void loadAdherents() {
        // Vider le tableau avant d'ajouter les nouveaux adhérents
        tableModel.setRowCount(0);

        // Charger les adhérents depuis le service
        List<Adherent> adherents;
        try {
            adherents = adherentService.listerAdherents();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des adhérents.\nVérifiez le fichier adherents.xml.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifier si la liste est vide
        if (adherents.isEmpty()) {
            if (tableModel.getRowCount() == 0) { // Évite d'afficher plusieurs messages
                JOptionPane.showMessageDialog(this,
                        "Aucun adhérent trouvé.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Ajouter les adhérents au tableau
            for (Adherent adherent : adherents) {
                tableModel.addRow(new Object[]{
                        adherent.getNom(),
                        adherent.getPrenom(),
                        adherent.getTelephone1(),
                        adherent.getCourriel()
                });
            }

            // Mettre à jour le titre avec le nombre d'adhérents
            setTitle("Liste des Adhérents - " + adherents.size() + " adhérent(s)");
        }
    }

    private void filterTable() {
        String text = searchField.getText().trim().toLowerCase();
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
            } catch (Exception e) {
                sorter.setRowFilter(null); // Désactive le filtre si une erreur survient
            }
        }
    }

    public static void main(String[] args) {
        // Appliquer un look and feel moderne
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new ListAdherentFrame());
    }
}