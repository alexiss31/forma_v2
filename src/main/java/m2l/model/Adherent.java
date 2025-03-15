package main.java.m2l.model;

public class Adherent {
    private String nom;
    private String prenom;
    private String genre;
    private String naissance;
    private String nationalite;
    private String adresse;
    private String codePostal;
    private String ville;
    private String telephone1;
    private String courriel;
    private String responsableLegal;
    private String armesPratique;
    private String lateralite;

    // Constructeur
    public Adherent(String nom, String prenom, String genre, String naissance, String nationalite, String adresse,
                    String codePostal, String ville, String telephone1, String courriel, String responsableLegal,
                    String armesPratique, String lateralite, String categorie) {
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.naissance = naissance;
        this.nationalite = nationalite;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
        this.telephone1 = telephone1;
        this.courriel = courriel;
        this.responsableLegal = responsableLegal;
        this.armesPratique = armesPratique;
        this.lateralite = lateralite;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getNaissance() {
        return naissance;
    }

    public void setNaissance(String naissance) {
        this.naissance = naissance;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public String getResponsableLegal() {
        return responsableLegal;
    }

    public void setResponsableLegal(String responsableLegal) {
        this.responsableLegal = responsableLegal;
    }

    public String getArmesPratique() {
        return armesPratique;
    }

    public void setArmesPratique(String armesPratique) {
        this.armesPratique = armesPratique;
    }

    public String getLateralite() {
        return lateralite;
    }

    public void setLateralite(String lateralite) {
        this.lateralite = lateralite;
    }

}
