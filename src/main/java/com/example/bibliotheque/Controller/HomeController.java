package com.example.bibliotheque.Controller;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private void ouvrirBibliotheque() {
        try {
            // Charger le fichier Bibliotheque.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bibliotheque/View/Biblio.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène pour afficher la bibliothèque
            Stage stage = new Stage();
            stage.setTitle("Gestion de la Bibliothèque");
            stage.setScene(new Scene(root));
            stage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
