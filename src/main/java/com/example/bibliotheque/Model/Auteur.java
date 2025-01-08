package com.example.bibliotheque.Model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlType(propOrder = {"nom", "prenom"})
public class Auteur {

    private final SimpleStringProperty nom = new SimpleStringProperty();
    private final SimpleStringProperty prenom = new SimpleStringProperty();

    @XmlElement(name = "nom")
    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    @XmlElement(name = "prenom")
    public String getPrenom() {
        return prenom.get();
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public StringProperty prenomProperty() {
        return prenom;
    }
}
