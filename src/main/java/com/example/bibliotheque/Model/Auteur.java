package com.example.bibliotheque.Model;
// Importation de l'annotation pour la sérialisation XML
import jakarta.xml.bind.annotation.XmlElement;
// Importation de l'annotation pour définir l'ordre des propriétés dans le XML
import jakarta.xml.bind.annotation.XmlType;

// Importation de la classe SimpleStringProperty utilisée pour la gestion des propriétés observables
import javafx.beans.property.SimpleStringProperty;

// Importation de la classe StringProperty pour lier les propriétés du modèle avec l'interface
import javafx.beans.property.StringProperty;

/**
 * La classe Auteur représente l'auteur d'un livre avec son nom et son prénom.
 * Utilisation de JavaFX Properties pour la gestion de données dynamiques dans l'interface graphique.
 */
// Définit l'ordre des éléments dans la sérialisation XML (nom d'abord, puis prénom)
@XmlType(propOrder = {"nom", "prenom"})
public class Auteur {
    // Déclaration des propriétés observables pour le nom et le prénom de l'auteur
    private final SimpleStringProperty nom = new SimpleStringProperty();
    private final SimpleStringProperty prenom = new SimpleStringProperty();

    /**
     * Getter pour la propriété `nom`.
     * Utilisé lors de la sérialisation XML grâce à l'annotation @XmlElement.
     * @return La valeur actuelle de la propriété `nom`.
     */
    @XmlElement(name = "nom")
    public String getNom() {
        return nom.get();  // Retourne la valeur actuelle de la propriété `nom`
    }
    /**
     * Setter pour la propriété `nom`.
     * @param nom Nouvelle valeur pour le nom de l'auteur.
     */
    public void setNom(String nom) {
        this.nom.set(nom);  // Met à jour la valeur de la propriété `nom`
    }

    /**
     * Permet de récupérer l'objet `StringProperty` lui-même.
     * Utile pour la liaison de données dans JavaFX (binding).
     * @return La propriété `nom` en tant qu'objet StringProperty.
     */
    public StringProperty nomProperty() {
        return nom;
    }
    /**
     * Getter pour la propriété `prenom`.
     */
    @XmlElement(name = "prenom")
    public String getPrenom() {
        return prenom.get();  // Retourne la valeur actuelle de la propriété `prenom`
    }

    /**
     * Setter pour la propriété `prenom`.
     */
    public void setPrenom(String prenom) {
        this.prenom.set(prenom);  // Met à jour la valeur de la propriété `prenom`
    }

    /**
     * Permet de récupérer l'objet `StringProperty` lui-même.
     * Utile pour la liaison de données dans JavaFX (binding).
     * @return La propriété `prenom` en tant qu'objet StringProperty.
     */
    public StringProperty prenomProperty() {
        return prenom;
    }
}
