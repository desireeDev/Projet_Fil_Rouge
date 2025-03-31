package com.example.tests;

import com.example.bibliotheque.Model.Bibliotheque;
import com.example.bibliotheque.Model.Livre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LivreTest {

    private Bibliotheque bibliotheque;
    private Livre livreSelectionne;

    @BeforeEach
    public void setUp() {
        // Initialisation de la bibliothèque avec quelques livres
        bibliotheque = new Bibliotheque();

        // Création d'un livre qui sera supprimé
        livreSelectionne = new Livre();
        livreSelectionne.setTitre("1984");
        livreSelectionne.setPresentation("Un roman dystopique");
        livreSelectionne.setParution(1949);
        livreSelectionne.setColonne(1);
        livreSelectionne.setRangee(1);
        livreSelectionne.setPathImage("/images/1984.png");
        livreSelectionne.setEmprunte(false);

        // Ajouter des livres à la bibliothèque
        Livre autreLivre1 = new Livre();
        autreLivre1.setTitre("Le Petit Prince");

        Livre autreLivre2 = new Livre();
        autreLivre2.setTitre("Moby Dick");

        bibliotheque.addLivre(autreLivre1);
        bibliotheque.addLivre(livreSelectionne); // Livre qui sera supprimé
        bibliotheque.addLivre(autreLivre2);
    }

    @Test
    public void testSupprimerLivreSelectionne() {
        // Given : Un livre est sélectionné dans la liste
        assertTrue(bibliotheque.getLivres().contains(livreSelectionne), "Le livre devrait être présent dans la bibliothèque avant suppression.");

        // When : L'utilisateur clique sur "Supprimer"
        bibliotheque.deleteLivre(livreSelectionne);

        // Then : Le livre est retiré de la bibliothèque
        assertFalse(bibliotheque.getLivres().contains(livreSelectionne), "Le livre devrait être supprimé de la bibliothèque.");

        // And : Le livre est retiré de l'affichage
        assertEquals(2, bibliotheque.getLivres().size(), "Le nombre de livres dans la bibliothèque devrait être de 2 après suppression.");
    }
}

