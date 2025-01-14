package com.example.bibliotheque.Model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;
//La classe qui gère la liste des livres

@XmlRootElement(name = "bibliotheque")
public class Bibliotheque {
    private List<Livre> livres = new ArrayList<>();

    @XmlElement(name = "livre")
    public List<Livre> getLivres() {
        return livres;
    }

    public void setLivres(List<Livre> livres) {
        this.livres = livres;
    }

    public void addLivre(Livre livre) {
        this.livres.add(livre);
    }

    public void deleteLivre(Livre livre) {
        this.livres.remove(livre);
    }
}
