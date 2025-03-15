package main.java.m2l.model;

public class Category {
    private String nom;
    private int ageMin;
    private int ageMax;

    // Constructeur
    public Category(String nom, int ageMin, int ageMax) {
        this.nom = nom;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
    }

    // Getters et setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getAgeMin() { return ageMin; }
    public void setAgeMin(int ageMin) { this.ageMin = ageMin; }

    public int getAgeMax() { return ageMax; }
    public void setAgeMax(int ageMax) { this.ageMax = ageMax; }
}
