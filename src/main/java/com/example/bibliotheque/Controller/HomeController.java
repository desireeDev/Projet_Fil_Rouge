package com.example.bibliotheque.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur pour la page d'accueil de l'application.
 * Gère la navigation vers la fenêtre principale de gestion de la bibliothèque.
 */
public class HomeController {

    // Chemin constant vers le fichier FXML de la bibliothèque.
    private static final String BIBLIO_FXML_PATH = "/com/example/bibliotheque/View/Biblio.fxml";
    private static final String APP_TITLE = "Gestion de la Bibliothèque";

    /**
     * Gère l'action d'ouverture de la fenêtre de gestion de la bibliothèque.
     * Cette méthode est appelée quand l'utilisateur interagit avec l'élément FXML.
     */
    @FXML
    private void openBiblio() {
        try {
            // Charge le fichier FXML de la bibliothèque.
            FXMLLoader loader = new FXMLLoader(getClass().getResource(BIBLIO_FXML_PATH));
            Parent root = loader.load();

            // Crée une nouvelle scène pour afficher l'interface de la bibliothèque.
            Scene scene = new Scene(root);

            // Configure une nouvelle fenêtre (Stage).
            Stage stage = new Stage();
            stage.setTitle(APP_TITLE);   // Définit le titre de la fenêtre.
            stage.setScene(scene);       // Attache la scène à la fenêtre.
            stage.setMaximized(true);    // Maximise la fenêtre au démarrage.

            // Affiche la fenêtre.
            stage.show();

        } catch (IOException e) {
            // En cas d'erreur de chargement du FXML, affiche la trace de la pile
            // dans la console pour le débogage.
            System.err.println("Erreur lors du chargement de la vue de la bibliothèque : " + e.getMessage());
            e.printStackTrace();
        }
    }
}