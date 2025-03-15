package main.java.m2l.services;

import main.java.m2l.model.Adherent;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

public class AdherentService {


    private static final String XML_FILE_PATH = "D:/Cours/2e_annee/AP/forma_v2/src/main/ressources/adherent.xml"; // Chemin vers le fichier XML

    // Méthode pour lister tous les adhérents depuis le fichier XML
    public List<Adherent> listerAdherents() {
        List<Adherent> adherents = new ArrayList<>();
        try {
            // Charger le document XML
            File file = new File(XML_FILE_PATH);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            // Normaliser le document
            doc.getDocumentElement().normalize();

            // Récupérer tous les éléments "adherent"
            NodeList nodeList = doc.getElementsByTagName("adherent");

            // Parcourir les éléments et créer des objets Adherent
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Extraire les informations de l'adherent
                    String nom = element.getElementsByTagName("nom").item(0).getTextContent();
                    String prenom = element.getElementsByTagName("prenom").item(0).getTextContent();
                    String genre = element.getElementsByTagName("genre").item(0).getTextContent();
                    String naissance = element.getElementsByTagName("naissance").item(0).getTextContent();
                    String nationalite = element.getElementsByTagName("nationalite").item(0).getTextContent();
                    String adresse = element.getElementsByTagName("adresse").item(0).getTextContent();
                    String codePostal = element.getElementsByTagName("code_postal").item(0).getTextContent();
                    String ville = element.getElementsByTagName("ville").item(0).getTextContent();
                    String telephone1 = element.getElementsByTagName("telephone1").item(0).getTextContent();
                    String courriel = element.getElementsByTagName("courriel").item(0).getTextContent();
                    String responsableLegal = element.getElementsByTagName("responsable_legal").item(0).getTextContent();
                    String armesPratique = element.getElementsByTagName("armes_pratique").item(0).getTextContent();
                    String lateralite = element.getElementsByTagName("lateralite").item(0).getTextContent();

                    // Créer un nouvel objet Adherent et l'ajouter à la liste
                    Adherent adherent = new Adherent(nom, prenom, genre, naissance, nationalite, adresse, codePostal, ville,
                            telephone1, courriel, responsableLegal, armesPratique, lateralite);
                    adherents.add(adherent);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return adherents;
    }

    // Méthode pour rechercher un adhérent par son nom
    public Adherent rechercherParNom(String nom) {
        List<Adherent> adherents = listerAdherents();
        for (Adherent adherent : adherents) {
            if (adherent.getNom().equalsIgnoreCase(nom)) {
                return adherent;
            }
        }
        return null; // Retourne null si aucun adhérent trouvé
    }

    public boolean modifierAdherent(String nom, Adherent nouvelAdherent) {
        try {
            File file = new File(XML_FILE_PATH);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("adherent");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String existingNom = element.getElementsByTagName("nom").item(0).getTextContent();

                    if (existingNom.equalsIgnoreCase(nom)) {
                        element.getElementsByTagName("nom").item(0).setTextContent(nouvelAdherent.getNom());
                        element.getElementsByTagName("prenom").item(0).setTextContent(nouvelAdherent.getPrenom());
                        element.getElementsByTagName("telephone1").item(0).setTextContent(nouvelAdherent.getTelephone1());
                        element.getElementsByTagName("courriel").item(0).setTextContent(nouvelAdherent.getCourriel());

                        // Sauvegarde dans le fichier XML
                        saveXMLChanges(doc);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Méthode pour supprimer un adhérent par son nom
    public boolean supprimerAdherent(String nom) {
        try {
            File file = new File(XML_FILE_PATH);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("adherent");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String existingNom = element.getElementsByTagName("nom").item(0).getTextContent();

                    if (existingNom.equalsIgnoreCase(nom)) {
                        element.getParentNode().removeChild(element);

                        // Sauvegarder les modifications dans le fichier XML
                        saveXMLChanges(doc);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void ajouterAdherent(Adherent adherent) {
        try {
            File file = new File("src/main/ressources/adherent.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;

            // Vérifier si le fichier existe
            if (file.exists()) {
                doc = builder.parse(file);
                doc.getDocumentElement().normalize();
            } else {
                // Créer un nouveau fichier XML si inexistant
                doc = builder.newDocument();
                Element rootElement = doc.createElement("Adherents");
                doc.appendChild(rootElement);
            }

            Element root = doc.getDocumentElement();
            Element adherentElement = doc.createElement("adherent");

            // Ajouter les balises XML avec les données
            adherentElement.appendChild(createElement(doc, "nom", adherent.getNom()));
            adherentElement.appendChild(createElement(doc, "prenom", adherent.getPrenom()));
            adherentElement.appendChild(createElement(doc, "genre", adherent.getGenre()));
            adherentElement.appendChild(createElement(doc, "naissance", adherent.getNaissance()));
            adherentElement.appendChild(createElement(doc, "nationalite", adherent.getNationalite()));
            adherentElement.appendChild(createElement(doc, "adresse", adherent.getAdresse()));
            adherentElement.appendChild(createElement(doc, "code_postal", adherent.getCodePostal()));
            adherentElement.appendChild(createElement(doc, "ville", adherent.getVille()));
            adherentElement.appendChild(createElement(doc, "telephone1", adherent.getTelephone1()));
            adherentElement.appendChild(createElement(doc, "courriel", adherent.getCourriel()));
            adherentElement.appendChild(createElement(doc, "responsable_legal", adherent.getResponsableLegal()));
            adherentElement.appendChild(createElement(doc, "armes_pratique", adherent.getArmesPratique()));
            adherentElement.appendChild(createElement(doc, "lateralite", adherent.getLateralite()));

            root.appendChild(adherentElement);

            // Sauvegarder les modifications
            saveXMLChanges(doc);

            System.out.println("Adhérent ajouté avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Element createElement(Document doc, String tag, String value) {
        Element element = doc.createElement(tag);
        element.appendChild(doc.createTextNode(value));
        return element;
    }



    // Méthode pour sauvegarder les modifications dans le fichier XML
    private void saveXMLChanges(Document doc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(XML_FILE_PATH));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
