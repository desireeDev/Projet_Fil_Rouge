package com.example.bibliotheque.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import utils.DatabaseConnection;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField registerNameField;
    @FXML private TextField registerFirstNameField;
    @FXML private TextField registerUserField;
    @FXML private PasswordField registerPasswordField;
    @FXML private PasswordField registerRepasswordField;
    @FXML private ComboBox<String> registerRoleComboBox;

    @FXML
    public void initialize() {
        if (registerRoleComboBox != null) {
            registerRoleComboBox.getItems().addAll("Admin", "User");
        }
    }

    @FXML
    private void onRegister(ActionEvent event) {
        if (registerNameField == null || registerFirstNameField == null || registerUserField == null ||
                registerPasswordField == null || registerRepasswordField == null || registerRoleComboBox == null) {
            System.err.println("Certains champs du formulaire d'inscription ne sont pas chargés.");
            return;
        }

        String name = registerNameField.getText();
        String firstName = registerFirstNameField.getText();
        String username = registerUserField.getText();
        String password = registerPasswordField.getText();
        String rePassword = registerRepasswordField.getText();
        String role = registerRoleComboBox.getValue();

        if (name.isEmpty() || firstName.isEmpty() || username.isEmpty() ||
                password.isEmpty() || rePassword.isEmpty() || role == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        if (!password.equals(rePassword)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas !");
            return;
        }

        boolean success = DatabaseConnection.register(name, firstName, username, password, role);
        if (success) {
            showAlert("Succès", "Inscription réussie !");
            clearRegisterFields();
            onConnect(event);  // redirige vers login
        } else {
            showAlert("Erreur", "Échec de l'inscription. Nom d'utilisateur peut-être déjà pris.");
        }
    }

    private void clearRegisterFields() {
        registerNameField.clear();
        registerFirstNameField.clear();
        registerUserField.clear();
        registerPasswordField.clear();
        registerRepasswordField.clear();
        registerRoleComboBox.getSelectionModel().clearSelection();
    }
    public void onConnect(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/example/bibliotheque/View/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loginRoot);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Ici tu peux éventuellement afficher une alerte d'erreur si besoin
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
