package com.example.bibliotheque.Controller;
//Les importations utiles
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MenuController {

    @FXML
    private void handleOpen() {
        // Code pour gérer l'action "Ouvrir"
        System.out.println("Ouvrir!");
    }

    @FXML
    private void handleLeft() {
        // Code pour gérer l'action "Quitter"
        System.out.println("Quitter clicked!");
        // Ferme l'application
        System.exit(0);
    }

    @FXML
    private void handleSave() {
        // Code pour gérer l'action "Sauvegarder"
        System.out.println("Sauvegarder clicked!");
    }
//Lien entre la vue et le controller
    @FXML
    private void handleSaveOn() {
        // Code pour gérer l'action "Sauvegarder sous"
        System.out.println("Sauvegarder sous!");
    }

    @FXML
    private void handleInfos() {
        // Code pour gérer l'action "Sauvegarder sous"
        System.out.println("Voici les informations!");
    }

    @FXML
    private void handleAbout() {
        // Code pour afficher un message d'info dans le menu "About"
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Ma Bibliothèque");
        alert.setContentText("Version 1.0");
        alert.showAndWait();
    }
}
