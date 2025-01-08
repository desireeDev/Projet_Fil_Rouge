package com.example.bibliotheque.Model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessType;

//Classe Livre
@XmlRootElement(name="Livre")
//Les attributs de la classe Livre
public class Livre {
    private long id;
    private String titre;
    private String auteur;
    private String nom;
    private String prenom;
    private String presentation;
    private String parution;
    private String colone;
    private String rangee;
    

}



