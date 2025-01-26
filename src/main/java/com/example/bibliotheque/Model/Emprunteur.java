package com.example.bibliotheque.Model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

@XmlRootElement(name = "emprunteur") // Définit l'élément racine pour l'emprunteur dans le fichier XML
public class Emprunteur {

    // Propriétés observables JavaFX pour la liaison avec l'interface utilisateur
    private final SimpleStringProperty nomEmprunteur = new SimpleStringProperty(); // Nom de l'emprunteur
    private final SimpleStringProperty prenomEmprunteur = new SimpleStringProperty(); // Prénom de l'emprunteur
    private final ObjectProperty<LocalDate> dateEmprunt = new SimpleObjectProperty<>(); // Date d'emprunt
    private final ObjectProperty<LocalDate> dateRendu = new SimpleObjectProperty<>(); // Date prévue de retour

    @XmlElement(name = "nom") // Mappe le champ "nom" à l'élément <nom> dans le fichier XML
    public String getNom() {
        return nomEmprunteur.get();
    }

    public void setNom(String nom) {
        this.nomEmprunteur.set(nom);
    }

    public SimpleStringProperty nomProperty() {
        return nomEmprunteur;
    }

    @XmlElement(name = "prenom") // Mappe le champ "prenom" à l'élément <prenom> dans le fichier XML
    public String getPrenom() {
        return prenomEmprunteur.get();
    }

    public void setPrenom(String prenom) {
        this.prenomEmprunteur.set(prenom);
    }

    public SimpleStringProperty prenomProperty() {
        return prenomEmprunteur;
    }

    @XmlElement(name = "dateEmprunt") // Mappe le champ "dateEmprunt" à l'élément <dateEmprunt> dans le fichier XML
    @XmlJavaTypeAdapter(LocalDateAdapter.class) // Utilise un adaptateur pour gérer la sérialisation/désérialisation de LocalDate
    public LocalDate getDateEmprunt() {
        return dateEmprunt.get();
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt.set(dateEmprunt);
    }

    public ObjectProperty<LocalDate> dateEmpruntProperty() {
        return dateEmprunt;
    }

    @XmlElement(name = "dateRendu") // Mappe le champ "dateRendu" à l'élément <dateRendu> dans le fichier XML
    @XmlJavaTypeAdapter(LocalDateAdapter.class) // Utilise un adaptateur pour gérer la sérialisation/désérialisation de LocalDate
    public LocalDate getDateRendu() {
        return dateRendu.get();
    }

    public void setDateRendu(LocalDate dateRendu) {
        this.dateRendu.set(dateRendu);
    }

    public ObjectProperty<LocalDate> dateRenduProperty() {
        return dateRendu;
    }
}
