package com.example.bibliotheque.Model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

@XmlRootElement(name = "livre")
public class Livre {

    private final SimpleStringProperty titre = new SimpleStringProperty();
    private final SimpleStringProperty presentation = new SimpleStringProperty();
    private final SimpleIntegerProperty parution = new SimpleIntegerProperty();
    private final SimpleIntegerProperty colonne = new SimpleIntegerProperty();
    private final SimpleIntegerProperty rangee = new SimpleIntegerProperty();
    private final SimpleStringProperty pathImage = new SimpleStringProperty();
    private Auteur auteur;

    @XmlElement(name = "titre")
    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    @XmlElement(name = "auteur")
    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    @XmlElement(name = "presentation")
    public String getPresentation() {
        return presentation.get();
    }

    public void setPresentation(String presentation) {
        this.presentation.set(presentation);
    }

    @XmlElement(name = "parution")
    public int getParution() {
        return parution.get();
    }

    public void setParution(int parution) {
        this.parution.set(parution);
    }

    @XmlElement(name = "colonne")
    public int getColonne() {
        return colonne.get();
    }

    public void setColonne(int colonne) {
        this.colonne.set(colonne);
    }

    @XmlElement(name = "rangee")
    public int getRangee() {
        return rangee.get();
    }

    public void setRangee(int rangee) {
        this.rangee.set(rangee);
    }
    @XmlElement(name = "pathImage")
    public String getPathImage() {
        return pathImage.get();
    }

    public void setPathImage(String pathImage) {
        this.pathImage.set(pathImage);
    }


}

