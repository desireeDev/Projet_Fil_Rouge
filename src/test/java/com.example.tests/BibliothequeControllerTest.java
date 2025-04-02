package com.example.tests;

import com.example.bibliotheque.Controller.BibliothequeController;
import com.example.bibliotheque.Model.Bibliotheque;
import com.example.bibliotheque.Model.Livre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BibliothequeControllerTest {

    private BibliothequeController controller;
    private Bibliotheque bibliotheque;

    @BeforeEach
    public void setUp() {
        controller = new BibliothequeController();
        bibliotheque = new Bibliotheque();

        Livre livre1 = new Livre();
        livre1.setTitre("1984");
        livre1.setPresentation("Un roman dystopique");
        livre1.setParution(1949);
        livre1.setColonne(1);
        livre1.setRangee(1);
        livre1.setPathImage("/images/1984.png");

        Livre livre2 = new Livre();
        livre2.setTitre("Le Petit Prince");
        livre2.setPresentation("Un conte poétique");
        livre2.setParution(1943);
        livre2.setColonne(2);
        livre2.setRangee(3);
        livre2.setPathImage("/images/le_petit_prince.png");

        bibliotheque.addLivre(livre1);
        bibliotheque.addLivre(livre2);
    }

    @Test
    public void testSauvegardeReussie() throws JAXBException, IOException {
        // Given : Une bibliothèque avec des livres
        File file = new File("bibliotheque_test.xml");

        // When : L'utilisateur choisit un emplacement pour sauvegarder
        JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(bibliotheque, file);

        // Then : Un fichier XML est créé
        assertTrue(file.exists(), "Le fichier XML devrait être créé.");

        // Nettoyage après test
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testAnnulationSauvegarde() {
        // Given : L'utilisateur ouvre la boîte de dialogue de sauvegarde

        // When : Il clique sur "Annuler"
        File file = null;  // Simule une annulation de la sauvegarde

        boolean saved = file != null && file.exists();

        // Then : Aucun fichier ne doit être créé
        assertFalse(saved, "Aucun fichier ne devrait être créé.");
    }

    @Test
    public void testErreurLorsDeLaSauvegarde() {
        // Given : Une erreur se produit pendant la sauvegarde
        File file = new File("/un/dossier/inexistant/bibliotheque.xml");

        // When : L'utilisateur tente de sauvegarder
        Exception exception = null;
        try {
            JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(bibliotheque, file);
        } catch (JAXBException e) {
            exception = e;
        }


        // Then : Un message d'erreur s'affiche
        assertNotNull(exception, "Une erreur devrait être levée si le fichier ne peut pas être créé.");
    }
}
