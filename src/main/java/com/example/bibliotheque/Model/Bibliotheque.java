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
    public boolean updateLivre(Livre livreModifie) {
        // Vérifie si les valeurs de parution et colonne sont valides
        if (livreModifie.getParution() < 0 || livreModifie.getColonne() < 0) {
            return false; // Annule la modification si les valeurs sont invalides
        }

        for (Livre livre : livres) {
            if (livre.getTitre().equals(livreModifie.getTitre())) {
                // Mises à jour des attributs du livre
                livre.setPresentation(livreModifie.getPresentation());
                livre.setParution(livreModifie.getParution());
                livre.setColonne(livreModifie.getColonne());
                livre.setRangee(livreModifie.getRangee());
                livre.setPathImage(livreModifie.getPathImage());
                livre.setEmprunte(livreModifie.isEmprunte());
                return true; // Modification réussie
            }
        }

        return false; // Livre non trouvé
    }


}
