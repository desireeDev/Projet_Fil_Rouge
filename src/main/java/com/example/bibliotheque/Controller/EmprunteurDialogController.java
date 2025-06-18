package com.example.bibliotheque.Controller;

import com.example.bibliotheque.Model.Emprunteur;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDate;

public class EmprunteurDialogController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private DatePicker dateEmpruntPicker;

    @FXML
    private DatePicker dateRenduPicker;

    private Emprunteur emprunteur;

    private boolean confirmed = false;

    @FXML
    public void initialize() {
        // Initialisation par défaut des dates
        dateEmpruntPicker.setValue(LocalDate.now());
        dateRenduPicker.setValue(LocalDate.now().plusWeeks(2));
    }

    @FXML
    private void handlePreter() {
        // Récupérer les données saisies et créer un objet Emprunteur
        emprunteur = new Emprunteur();
        emprunteur.setNom(nomField.getText().trim());
        emprunteur.setPrenom(prenomField.getText().trim());
        emprunteur.setDateEmprunt(dateEmpruntPicker.getValue());
        emprunteur.setDateRendu(dateRenduPicker.getValue());

        confirmed = true; // Marquer comme validé

        // Afficher un message de confirmation
        showAlert("Succès", "Le livre a été bien prêté !");

        closeDialog();
    }

    @FXML
    private void handleAnnuler() {
        confirmed = false; // Marquer comme annulé
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    public Emprunteur getEmprunteur() {
        return confirmed ? emprunteur : null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}