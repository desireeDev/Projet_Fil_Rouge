package com.example.bibliotheque.Model;

import jakarta.xml.bind.annotation.XmlElement; // Annotation pour mapper les éléments XML aux champs Java
import jakarta.xml.bind.annotation.XmlRootElement; // Annotation pour indiquer la racine d'un document XML
import javafx.beans.property.*; // Propriétés observables JavaFX pour la liaison des données


@XmlRootElement(name = "livre") // Indique que cette classe représente l'élément racine <livre> dans le fichier XML
public class Livre {

    // Définition des propriétés JavaFX pour la liaison des données avec l'interface utilisateur
    private final SimpleStringProperty titre = new SimpleStringProperty(); // Titre du livre
    private final SimpleStringProperty presentation = new SimpleStringProperty();
    private final SimpleIntegerProperty parution = new SimpleIntegerProperty();
    private final SimpleIntegerProperty colonne = new SimpleIntegerProperty();
    private final SimpleIntegerProperty rangee = new SimpleIntegerProperty();
    private final SimpleStringProperty pathImage = new SimpleStringProperty();
    private final SimpleBooleanProperty emprunte = new SimpleBooleanProperty(); //statut du livre
    // Date prévue de retour
    private Auteur auteur; // Auteur du livre (objet séparé pour gérer les informations de l'auteur)
    private Emprunteur emprunteur;

    @XmlElement(name = "titre") // Mappe le champ "titre" à l'élément <titre> dans le fichier XML
    public String getTitre() {
        return titre.get(); // Retourne la valeur de la propriété titre
    }

    public void setTitre(String titre) {
        this.titre.set(titre); // Définit la valeur de la propriété titre
    }

    public SimpleStringProperty titreProperty() {
        return titre; // Retourne la propriété observée pour la liaison des données
    }

    @XmlElement(name = "auteur") // Mappe le champ "auteur" à l'élément <auteur> dans le fichier XML
    public Auteur getAuteur() {
        return auteur; // Retourne l'objet Auteur
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur; // Définit l'auteur du livre
    }

    @XmlElement(name = "emprunte")
    public boolean isEmprunte() {
        return emprunte.get();
    }

    public void setEmprunte(boolean emprunte) {
        this.emprunte.set(emprunte);
    }

    public SimpleBooleanProperty emprunteProperty() {
        return emprunte;
    }
//Emprunt
@XmlElement(name = "emprunteur") // Mappe le champ "emprunteur" à l'élément <emprunteur> dans le fichier XML
public Emprunteur getEmprunteur() {
    return emprunteur;
}

    public void setEmprunteur(Emprunteur emprunteur) {
        this.emprunteur = emprunteur;
    }

    @XmlElement(name = "presentation") // Mappe le champ "presentation" à l'élément <presentation> dans le fichier XML
    public String getPresentation() {
        return presentation.get(); // Retourne la valeur de la propriété présentation
    }

    public void setPresentation(String presentation) {
        this.presentation.set(presentation); // Définit la valeur de la présentation
    }

    public SimpleStringProperty presentationProperty() {
        return presentation; // Retourne la propriété observée pour la liaison des données
    }

    @XmlElement(name = "parution") // Mappe le champ "parution" à l'élément <parution> dans le fichier XML
    public int getParution() {
        return parution.get(); // Retourne l'année de parution
    }

    public void setParution(int parution) {
        this.parution.set(parution); // Définit l'année de parution
    }

    public SimpleIntegerProperty parutionProperty() {
        return parution; // Retourne la propriété observée pour la liaison des données
    }

    @XmlElement(name = "colonne") // Mappe le champ "colonne" à l'élément <colonne> dans le fichier XML
    public int getColonne() {
        return colonne.get(); // Retourne le numéro de colonne
    }

    public void setColonne(int colonne) {
        this.colonne.set(colonne); // Définit le numéro de colonne
    }

    public SimpleIntegerProperty colonneProperty() {
        return colonne; // Retourne la propriété observée pour la liaison des données
    }

    @XmlElement(name = "rangee") // Mappe le champ "rangee" à l'élément <rangee> dans le fichier XML
    public int getRangee() {
        return rangee.get(); // Retourne le numéro de rangée
    }

    public void setRangee(int rangee) {
        this.rangee.set(rangee); // Définit le numéro de rangée
    }

    public SimpleIntegerProperty rangeeProperty() {
        return rangee; // Retourne la propriété observée pour la liaison des données
    }

    @XmlElement(name = "pathImage") // Mappe le champ "pathImage" à l'élément <pathImage> dans le fichier XML
    public String getPathImage() {
        return pathImage.get(); // Retourne le chemin de l'image
    }

    public void setPathImage(String pathImage) {
        this.pathImage.set(pathImage); // Définit le chemin de l'image
    }

    public SimpleStringProperty pathImageProperty() {
        return pathImage; // Retourne la propriété observée pour la liaison des données
    }




}
