package com.example.bibliotheque.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import utils.DatabaseConnection;
import com.example.bibliotheque.Session;

import java.io.IOException;

public class LoginController {

    @FXML private TextField loginUserField;
    @FXML private PasswordField loginPasswordField;

    @FXML
    private void onConnect(ActionEvent event) {
        if (loginUserField == null || loginPasswordField == null) {
            System.err.println("Champs de login non chargés.");
            return;
        }

        String username = loginUserField.getText();
        String password = loginPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez saisir votre nom d'utilisateur et mot de passe.");
            return;
        }

        if (DatabaseConnection.login(username, password)) {
            String role = DatabaseConnection.getRole(username);

            if (role == null) {
                showAlert("Erreur", "Impossible de récupérer le rôle utilisateur.");
                return;
            }

            // ✅ Stocke le rôle uniquement si valide
            Session.setCurrentUserRole(role);

            try {
                Parent root;

                if (role.equalsIgnoreCase("Admin")) {
                    root = FXMLLoader.load(getClass().getResource("/com/example/bibliotheque/View/home.fxml"));
                } else {
                    root = FXMLLoader.load(getClass().getResource("/com/example/bibliotheque/View/Home.fxml"));
                }

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors du chargement de la page.");
            }

        } else {
            showAlert("Erreur", "Identifiants incorrects !");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void onRegister(ActionEvent event) {
        try {
            Parent registerRoot = FXMLLoader.load(getClass().getResource("/com/example/bibliotheque/View/Register.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(registerRoot);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page d'inscription.");
        }
    }
    @FXML
    private void onUpdate (ActionEvent event){
        System.out.println("La fonctionnalité est à venir,soyez patients");
    }

}
