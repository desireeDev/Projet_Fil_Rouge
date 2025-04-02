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
        bibliotheque = new Bibliotheque();
        livreSelectionne = new Livre();
        livreSelectionne.setTitre("1984");
        livreSelectionne.setPresentation("Un roman dystopique");
        livreSelectionne.setParution(1949);
        livreSelectionne.setColonne(1);
        livreSelectionne.setRangee(1);
        livreSelectionne.setPathImage("/images/1984.png");
        livreSelectionne.setEmprunte(false);

        Livre autreLivre1 = new Livre();
        autreLivre1.setTitre("Le Petit Prince");

        Livre autreLivre2 = new Livre();
        autreLivre2.setTitre("Moby Dick");

        bibliotheque.addLivre(autreLivre1);
        bibliotheque.addLivre(livreSelectionne);
        bibliotheque.addLivre(autreLivre2);
    }

    @Test
    public void testSupprimerLivreSelectionne() {
        assertTrue(bibliotheque.getLivres().contains(livreSelectionne), "Le livre devrait être présent dans la bibliothèque avant suppression.");

        bibliotheque.deleteLivre(livreSelectionne);

        assertFalse(bibliotheque.getLivres().contains(livreSelectionne), "Le livre devrait être supprimé de la bibliothèque.");
        assertEquals(2, bibliotheque.getLivres().size(), "Le nombre de livres dans la bibliothèque devrait être de 2 après suppression.");
    }

    @Test
    public void testAjouterLivreValide() {
        Livre livre = new Livre();
        livre.setTitre("Nouveau Livre");
        livre.setPresentation("Présentation valide");
        livre.setParution(2020);
        livre.setColonne(3);
        livre.setRangee(2);
        livre.setPathImage("/images/nouveau.png");
        livre.setEmprunte(false);

        bibliotheque.addLivre(livre);

        assertTrue(bibliotheque.getLivres().contains(livre), "Le livre devrait être ajouté à la bibliothèque.");
    }

    @Test
    public void testAjouterLivreChampsVides() {
        Livre livre = new Livre();

        bibliotheque.addLivre(livre);

        assertTrue(bibliotheque.getLivres().contains(livre), "Même un livre avec des champs vides peut être ajouté.");
    }

    @Test
    public void testAjouterLivreDateNonNumerique() {
        Livre livre = new Livre();
        livre.setTitre("Livre Test");
        livre.setPresentation("Présentation valide");
        livre.setParution(-1); // Valeur incorrecte pour la parution
        livre.setColonne(1);
        livre.setRangee(1);

        bibliotheque.addLivre(livre);

        assertTrue(bibliotheque.getLivres().contains(livre), "Le livre avec une date invalide est ajouté, à moins d'une validation côté logique.");
    }

    @Test
    public void testAjouterLivreDateFuture() {
        Livre livre = new Livre();
        livre.setTitre("Livre Futur");
        livre.setPresentation("Présentation valide");
        livre.setParution(3000); // Date dans le futur
        livre.setColonne(1);
        livre.setRangee(1);

        bibliotheque.addLivre(livre);

        assertTrue(bibliotheque.getLivres().contains(livre), "Le livre est ajouté malgré une date future, sauf si une validation existe.");
    }

    @Test
    public void testAjouterLivreColonneRangeeInvalide() {
        Livre livre = new Livre();
        livre.setTitre("Livre Invalide");
        livre.setColonne(10); // Colonne invalide
        livre.setRangee(10); // Rangée invalide

        bibliotheque.addLivre(livre);

        assertTrue(bibliotheque.getLivres().contains(livre), "Le livre est ajouté malgré une colonne et une rangée invalides.");
    }

    @Test
    public void testAjouterLivreDuplique() {
        Livre livre = new Livre();
        livre.setTitre("1984"); // Même titre que livreSelectionne
        livre.setPresentation("Un roman dystopique");
        livre.setParution(1949);
        livre.setColonne(1);
        livre.setRangee(1);

        bibliotheque.addLivre(livre);

        assertTrue(bibliotheque.getLivres().contains(livre), "Le livre en double est ajouté sans vérification d'unicité.");
    }

    @Test
    public void testAjouterLivreEmprunte() {
        Livre livre = new Livre();
        livre.setTitre("Livre Emprunté");
        livre.setPresentation("Présentation valide");
        livre.setParution(2022);
        livre.setColonne(2);
        livre.setRangee(3);
        livre.setEmprunte(true);

        bibliotheque.addLivre(livre);

        assertTrue(bibliotheque.getLivres().contains(livre), "Le livre emprunté devrait être ajouté correctement.");
    }

//Tests sur la modification du livre
    @Test
    public void testModificationLivre() {
        // Vérifier que le livre est bien dans la bibliothèque avant la modification
        assertTrue(bibliotheque.getLivres().contains(livreSelectionne), "Le livre doit exister avant modification.");
        // Modifier le titre du livre
        livreSelectionne.setTitre("1984 - Nouvelle Édition");

        // Vérifier que la mise à jour est bien prise en compte dans la bibliothèque
        assertTrue(bibliotheque.getLivres().contains(livreSelectionne), "Le livre doit toujours être présent après modification.");
        assertEquals("1984 - Nouvelle Édition", livreSelectionne.getTitre(), "Le titre du livre doit être mis à jour.");
    }
    // Test sur les valeurs numériques invalides
    @Test
    public void testModificationValeursNumeriquesInvalides() {
        livreSelectionne.setParution(-1); // Valeur invalide
        livreSelectionne.setColonne(-5);  // Valeur invalide

        boolean updated = bibliotheque.updateLivre(livreSelectionne);
        assertFalse(updated, "La modification devrait être annulée.");
    }

    // Mise à jour du statut du livre
    @Test
    public void testMiseAJourStatutLivre() {
        // S'assurer que le livre existe dans la bibliothèque
        assertTrue(bibliotheque.getLivres().contains(livreSelectionne), "Le livre devrait être présent dans la bibliothèque avant modification.");

        // Mettre à jour le statut du livre pour qu'il soit emprunté
        livreSelectionne.setEmprunte(true);
        boolean updated = bibliotheque.updateLivre(livreSelectionne);

        // Vérification que le statut a bien été modifié
        assertTrue(updated, "La mise à jour du statut du livre devrait réussir.");
        assertTrue(livreSelectionne.isEmprunte(), "Le livre devrait être marqué comme emprunté.");
    }
    // 5️⃣ Un seul champ modifié
    @Test
    public void testModificationUnSeulChamp() {
        // Vérifier que le livre est déjà dans la bibliothèque avant la modification
        assertTrue(bibliotheque.getLivres().contains(livreSelectionne), "Le livre doit être présent dans la bibliothèque avant la modification.");

        // Effectuer la modification de la colonne
        livreSelectionne.setColonne(2);
        bibliotheque.updateLivre(livreSelectionne);

        // Vérifier que seule la colonne a été modifiée
        assertEquals(2, bibliotheque.getLivres().stream()
                .filter(livre -> livre.getTitre().equals(livreSelectionne.getTitre()))
                .findFirst()
                .get()
                .getColonne(), "Seule la colonne devrait être modifiée.");
    }

}
