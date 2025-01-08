package com.example.bibliotheque.Model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

//Classe Livre
@XmlRootElement(name = "Livre")
@XmlType(propOrder = {"id", "titre", "auteur", "presentation", "parution", "colonne", "rangee"})
public class Livre {
    private long id;
    private String titre;
    private Auteur auteur;  // Cle etrangére dans la table Livre
    private String presentation;
    private short parution;
    private short colonne;
    private byte rangee;

    // Getter et Setter pour id
    @XmlElement
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // Getter et Setter pour titre
    @XmlElement
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    // Getter et Setter pour auteur
    @XmlElement
    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    // Getter et Setter pour presentation
    @XmlElement
    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    // Getter et Setter pour parution
    @XmlElement
    public short getParution() {
        return parution;
    }

    public void setParution(short parution) {
        this.parution = parution;
    }

    // Getter et Setter pour colonne
    @XmlElement
    public short getColonne() {
        return colonne;
    }

    public void setColonne(short colonne) {
        this.colonne = colonne;
    }

    // Getter et Setter pour rangee
    @XmlElement
    public byte getRangee() {
        return rangee;
    }

    public void setRangee(byte rangee) {
        this.rangee = rangee;
    }
}
