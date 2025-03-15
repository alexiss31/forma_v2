package main.java.m2l.model;

public class Club {
    private String nom;
    private String ville;
    private String adresse;
    private String telephone;
    private String email;

    // Constructeur
    public Club(String nom, String ville, String adresse, String telephone, String email) {
        this.nom = nom;
        this.ville = ville;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
    }

    // Getters et setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
