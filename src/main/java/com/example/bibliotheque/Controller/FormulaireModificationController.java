package com.example.bibliotheque.Controller;

import com.example.bibliotheque.Model.Livre;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
public class FormulaireModificationController {
    @FXML private TextField titreField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField presentationField;
    @FXML private TextField parutionField;
    @FXML private TextField colonneField;
    @FXML private TextField rangeeField;
    @FXML private TextField pathImageField;
    @FXML private Button saveButton;
    @FXML private Label messageLabel;
    private Livre livreSelectionne;
    private BibliothequeController mainController;

    public void initialiserFormulaire(Livre livre, BibliothequeController controller) {
        this.livreSelectionne = livre;
        this.mainController = controller;
        // Préremplir les champs avec les valeurs actuelles
        titreField.setText(livre.getTitre());
        nomField.setText(livre.getAuteur().getNom());
        prenomField.setText(livre.getAuteur().getPrenom());
        presentationField.setText(livre.getPresentation());
        parutionField.setText(String.valueOf(livre.getParution()));
        colonneField.setText(String.valueOf(livre.getColonne()));
        rangeeField.setText(String.valueOf(livre.getRangee()));
        pathImageField.setText(livre.getPathImage());
    }

    @FXML
    public void sauvegarderModification() {
        try {
            // Utilisation d'un opérateur ternaire pour éviter les valeurs null
            String titre = (titreField.getText() != null) ? titreField.getText().trim() : "";
            String nom = (nomField.getText() != null) ? nomField.getText().trim() : "";
            String prenom = (prenomField.getText() != null) ? prenomField.getText().trim() : "";
            String presentation = (presentationField.getText() != null) ? presentationField.getText().trim() : "";
            String parutionText = (parutionField.getText() != null) ? parutionField.getText().trim() : "";
            String colonneText = (colonneField.getText() != null) ? colonneField.getText().trim() : "";
            String rangeeText = (rangeeField.getText() != null) ? rangeeField.getText().trim() : "";
            String pathImage = (pathImageField.getText() != null) ? pathImageField.getText().trim() : "";
            // Conversion sécurisée des champs numériques
            int parution = Integer.parseInt(parutionText);
            int colonne = Integer.parseInt(colonneText);
            int rangee = Integer.parseInt(rangeeText);
            // Mise à jour des valeurs du livre sélectionné
            livreSelectionne.setTitre(titre);
            livreSelectionne.getAuteur().setNom(nom);
            livreSelectionne.getAuteur().setPrenom(prenom);
            livreSelectionne.setPresentation(presentation);
            livreSelectionne.setParution(parution);
            livreSelectionne.setColonne(colonne);
            livreSelectionne.setRangee(rangee);
            livreSelectionne.setPathImage(pathImage);
            // Rafraîchir la TableView dans le contrôleur principal
            mainController.getTableView().refresh();
            messageLabel.setText("Livre modifié avec succès !");
            // Fermer la fenêtre après sauvegarde
            ((Stage) saveButton.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            messageLabel.setText("Erreur : Veuillez entrer des valeurs numériques valides.");
        } catch (Exception e) {
            messageLabel.setText("Une erreur est survenue : " + e.getMessage());
        }
    }
}