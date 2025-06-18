package com.example.bibliotheque.Controller;

import com.example.bibliotheque.Model.Livre;
import com.example.bibliotheque.Model.Auteur; // Assurez-vous d'importer Auteur
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate; // Pour la validation de l'année de parution

/**
 * Contrôleur pour le formulaire de modification d'un livre.
 * Permet de pré-remplir les champs avec les données d'un livre sélectionné
 * et de sauvegarder les modifications apportées.
 */
public class FormulaireModificationController {

    // --- Constantes pour les validations ---
    private static final int MIN_COL_ROW_VALUE = 1;
    private static final int MAX_COL_ROW_VALUE = 7;

    // --- Éléments FXML (UI) ---
    @FXML private TextField titreField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField presentationField;
    @FXML private TextField parutionField;
    @FXML private TextField colonneField;
    @FXML private TextField rangeeField;
    @FXML private TextField pathImageField;
    @FXML private Button saveButton;
    @FXML private Label messageLabel; // Pour afficher les messages à l'utilisateur
    @FXML private ComboBox<String> statutComboBox;

    // --- Variables d'état interne ---
    private Livre livreSelectionne; // Le livre en cours de modification
    private BibliothequeController mainController; // Référence au contrôleur principal pour rafraîchir la TableView

    /**
     * Initialise le formulaire avec les données du livre à modifier.
     * Cette méthode doit être appelée après le chargement du FXML.
     *
     * @param livre Le {@link Livre} dont les informations doivent être affichées et modifiées.
     * @param controller La référence au {@link BibliothequeController} pour le rafraîchissement du tableau.
     */
    public void initialiserFormulaire(Livre livre, BibliothequeController controller) {
        this.livreSelectionne = livre;
        this.mainController = controller;

        // Pré-remplir les champs de texte avec les valeurs actuelles du livre.
        // Utilisation de l'opérateur ternaire pour gérer les cas où l'auteur pourrait être null.
        titreField.setText(livre.getTitre());
        nomField.setText(livre.getAuteur() != null ? livre.getAuteur().getNom() : "");
        prenomField.setText(livre.getAuteur() != null ? livre.getAuteur().getPrenom() : "");
        presentationField.setText(livre.getPresentation());
        parutionField.setText(String.valueOf(livre.getParution()));
        colonneField.setText(String.valueOf(livre.getColonne()));
        rangeeField.setText(String.valueOf(livre.getRangee()));
        pathImageField.setText(livre.getPathImage());

        // Initialiser le ComboBox pour le statut.
        // Il est bon de vider les items existants avant d'ajouter, pour éviter les doublons si la méthode est appelée plusieurs fois.
        statutComboBox.getItems().clear();
        statutComboBox.getItems().addAll("Disponible", "Indisponible");
        // Définir la valeur sélectionnée en fonction du statut actuel du livre.
        // Si livre.isEmprunte() est vrai, alors le statut affiché est "Indisponible". Sinon, "Disponible".
        statutComboBox.setValue(livre.isEmprunte() ? "Indisponible" : "Disponible");
    }

    /**
     * Gère l'action de sauvegarde des modifications apportées au livre.
     * Cette méthode est appelée lorsque l'utilisateur clique sur le bouton "Sauvegarder".
     */
    @FXML
    public void sauvegarderModification() {
        messageLabel.setText(""); // Efface les messages précédents avant de tenter la sauvegarde.

        // 1. Valider les champs d'entrée. Si la validation échoue, on arrête.
        if (!validateInputFields()) {
            return;
        }

        try {
            // 2. Convertir les champs texte en numériques et valider leurs contraintes.
            int parution = Integer.parseInt(parutionField.getText().trim());
            int colonne = Integer.parseInt(colonneField.getText().trim());
            int rangee = Integer.parseInt(rangeeField.getText().trim());

            if (!validateNumericalConstraints(parution, colonne, rangee)) {
                return;
            }

            // 3. Mettre à jour les propriétés du livre sélectionné avec les nouvelles valeurs.
            updateLivreProperties(parution, colonne, rangee);

            // 4. Rafraîchir la TableView du contrôleur principal pour afficher les modifications.
            if (mainController != null) {
                mainController.getTableView().refresh();
            }

            // 5. Afficher un message de succès et fermer la fenêtre du formulaire.
            messageLabel.setText("Livre modifié avec succès !");
            closeFormWindow();

        } catch (NumberFormatException e) {
            // Gérer l'erreur si les champs numériques ne sont pas des nombres valides.
            messageLabel.setText("Erreur de saisie : L'année de parution, la colonne et la rangée doivent être des nombres.");
            System.err.println("Erreur de format numérique : " + e.getMessage());
        } catch (Exception e) {
            // Gérer toute autre exception inattendue.
            messageLabel.setText("Une erreur inattendue est survenue lors de la sauvegarde : " + e.getMessage());
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace(); // Pour un débogage plus approfondi.
        }
    }

    /**
     * Valide que tous les champs de saisie obligatoires ne sont pas vides.
     *
     * @return true si tous les champs requis sont remplis, false sinon.
     */
    private boolean validateInputFields() {
        if (titreField.getText().trim().isEmpty() ||
                nomField.getText().trim().isEmpty() ||
                prenomField.getText().trim().isEmpty() ||
                presentationField.getText().trim().isEmpty() ||
                parutionField.getText().trim().isEmpty() ||
                colonneField.getText().trim().isEmpty() ||
                rangeeField.getText().trim().isEmpty() ||
                pathImageField.getText().trim().isEmpty()) {

            messageLabel.setText("Veuillez remplir tous les champs obligatoires.");
            return false;
        }
        return true;
    }

    /**
     * Valide les contraintes numériques pour l'année de parution, la colonne et la rangée.
     *
     * @param parution L'année de parution saisie.
     * @param colonne Le numéro de colonne saisi.
     * @param rangee Le numéro de rangée saisi.
     * @return true si les valeurs sont valides selon les contraintes, false sinon.
     */
    private boolean validateNumericalConstraints(int parution, int colonne, int rangee) {
        if (parution > LocalDate.now().getYear()) {
            messageLabel.setText("L'année de parution ne peut pas être dans le futur.");
            return false;
        }
        if (colonne < MIN_COL_ROW_VALUE || colonne > MAX_COL_ROW_VALUE ||
                rangee < MIN_COL_ROW_VALUE || rangee > MAX_COL_ROW_VALUE) {
            messageLabel.setText("La colonne et la rangée doivent être comprises entre " + MIN_COL_ROW_VALUE + " et " + MAX_COL_ROW_VALUE + ".");
            return false;
        }
        return true;
    }

    /**
     * Met à jour les propriétés de l'objet {@link Livre} sélectionné avec les valeurs du formulaire.
     *
     * @param parution L'année de parution déjà validée.
     * @param colonne Le numéro de colonne déjà validé.
     * @param rangee Le numéro de rangée déjà validé.
     */
    private void updateLivreProperties(int parution, int colonne, int rangee) {
        // Mettre à jour les propriétés du livre. Utilise .trim() pour enlever les espaces superflus.
        livreSelectionne.setTitre(titreField.getText().trim());

        // Gérer la mise à jour de l'auteur : créer un nouvel Auteur si celui du livre est null.
        Auteur auteur = livreSelectionne.getAuteur();
        if (auteur == null) {
            auteur = new Auteur();
            livreSelectionne.setAuteur(auteur);
        }
        auteur.setNom(nomField.getText().trim());
        auteur.setPrenom(prenomField.getText().trim());

        livreSelectionne.setPresentation(presentationField.getText().trim());
        livreSelectionne.setParution(parution);
        livreSelectionne.setColonne(colonne);
        livreSelectionne.setRangee(rangee);
        livreSelectionne.setPathImage(pathImageField.getText().trim());

        // Mettre à jour le statut d'emprunt basé sur la sélection du ComboBox.
        // Si le statut sélectionné est "Indisponible", alors le livre est considéré comme emprunté (true).
        livreSelectionne.setEmprunte(statutComboBox.getValue().equals("Indisponible"));
    }

    /**
     * Ferme la fenêtre du formulaire de modification.
     */
    private void closeFormWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}