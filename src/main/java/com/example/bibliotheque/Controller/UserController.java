package com.example.bibliotheque.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;


import java.io.IOException;
public class UserController {

/*
    @FXML
    private void onClose (ActionEvent event){
        Stage stage=(Stage)close.getScene().getWindow();
        stage.close();
    }*/

    @FXML
    private void onConnect(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page Home
            Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/bibliotheque/View/home.fxml"));

            // Obtenir la scène actuelle via l’événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Créer une nouvelle scène avec le contenu Home
            Scene scene = new Scene(homeRoot);

            // Afficher la nouvelle scène
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onRegister(ActionEvent event){
        try {
            // Charger le fichier FXML de la page Home
            Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/bibliotheque/View/Register.fxml"));

            // Obtenir la scène actuelle via l’événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Créer une nouvelle scène avec le contenu Home
            Scene scene = new Scene(homeRoot);

            // Afficher la nouvelle scène
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
