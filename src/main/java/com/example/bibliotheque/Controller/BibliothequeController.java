package com.example.bibliotheque.Controller;

import com.example.bibliotheque.Model.Bibliotheque;
// Keep Emprunteur if you plan to use it for 'rendre' (return) later
import com.example.bibliotheque.Model.Livre;
import com.example.bibliotheque.Model.Auteur;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.FileDialog; // Keep AWT FileDialog if you prefer it over JavaFX FileChooser for 'Open'
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional; // For confirmation dialogs

import utils.BookExporter;
// import utils.LibraryMySql; // Uncomment if you use it for DB operations

public class BibliothequeController {

    // --- FXML Elements (UI) ---
    @FXML private TableView<Livre> tableView;
    @FXML private TableColumn<Livre, String> titleColumn;
    @FXML private TableColumn<Livre, String> authorColumn;
    @FXML private TableColumn<Livre, String> presentationColumn;
    @FXML private TableColumn<Livre, Integer> parutionColumn;
    @FXML private TableColumn<Livre, Integer> colonneColumn;
    @FXML private TableColumn<Livre, Integer> rangeeColumn;
    @FXML private TableColumn<Livre, String> pathImageColumn;
    @FXML private TableColumn<Livre, String> statutColumn;
    @FXML private TableColumn<Livre, Void> actionsColumn;

    @FXML private TextField titreField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField presentationField;
    @FXML private TextField parutionField;
    @FXML private TextField colonneField;
    @FXML private TextField rangeeField;
    @FXML private TextField pathImageField;
    @FXML private Label errorLabel;
    @FXML private CheckBox emprunteCheckBox;

    // --- Internal State Variables ---
    private Bibliotheque bibliotheque;
    private ObservableList<Livre> livresObservable;
    private String xmlFilePath = "src/main/resources/Biblio.xml";

    // --- Constants for Validation ---
    private static final int MIN_COL_ROW_VALUE = 1;
    private static final int MAX_COL_ROW_VALUE = 7;

    /**
     * Constructor: Initializes the Bibliotheque object by loading data from XML.
     * If the XML file doesn't exist, a new empty Bibliotheque is created.
     */
    public BibliothequeController() {
        try {
            JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
            File fichierXML = new File(xmlFilePath);
            if (fichierXML.exists()) {
                Unmarshaller unmarshaller = context.createUnmarshaller();
                bibliotheque = (Bibliotheque) unmarshaller.unmarshal(fichierXML);
            } else {
                bibliotheque = new Bibliotheque();
            }
            // Initialize observable list with loaded books or an empty list
            livresObservable = FXCollections.observableArrayList(bibliotheque.getLivres());
        } catch (JAXBException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible de charger la bibliothèque : " + e.getMessage());
            bibliotheque = new Bibliotheque(); // Ensure bibliotheque is not null
            livresObservable = FXCollections.observableArrayList();
        }
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method sets up cell value factories for TableView columns and adds action buttons.
     */
    @FXML
    public void initialize() {
        setupTableColumns();
        tableView.setItems(livresObservable); // Bind ObservableList to TableView
        ajouterBoutonsActions();
        // Clear any initial error label text
        errorLabel.setText("");
    }

    /**
     * Sets up the cell value factories for each TableColumn.
     */
    private void setupTableColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        authorColumn.setCellValueFactory(cellData -> {
            Auteur auteur = cellData.getValue().getAuteur();
            return new javafx.beans.property.SimpleStringProperty(
                    auteur != null ? auteur.getNom() + " " + auteur.getPrenom() : "N/A"
            );
        });
        presentationColumn.setCellValueFactory(new PropertyValueFactory<>("presentation"));
        parutionColumn.setCellValueFactory(new PropertyValueFactory<>("parution"));
        colonneColumn.setCellValueFactory(new PropertyValueFactory<>("colonne"));
        rangeeColumn.setCellValueFactory(new PropertyValueFactory<>("rangee"));
        pathImageColumn.setCellValueFactory(new PropertyValueFactory<>("pathImage"));
        statutColumn.setCellValueFactory(cellData -> {
            boolean emprunte = cellData.getValue().isEmprunte();
            return new javafx.beans.property.SimpleStringProperty(emprunte ? "Indisponible" : "Disponible");
        });
    }

    /**
     * Handles the "Open" menu action. Allows the user to select an XML file
     * and load a new library from it.
     */
    @FXML
    public void handleOpen() {
        FileDialog fd = new FileDialog((java.awt.Frame) null, "Choose a file", FileDialog.LOAD);
        fd.setFile("*.xml");
        fd.setVisible(true);
        String filename = fd.getFile();
        if (filename == null) {
            System.out.println("You cancelled the file selection.");
            return;
        }

        xmlFilePath = fd.getDirectory() + fd.getFile();
        File xmlFile = new File(xmlFilePath);

        try {
            JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            bibliotheque = (Bibliotheque) unmarshaller.unmarshal(xmlFile);
            chargerBibliothequeDansVue(bibliotheque);
            showAlert(Alert.AlertType.INFORMATION, "Ouverture réussie", "La bibliothèque a été chargée depuis : " + xmlFilePath);
        } catch (JAXBException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur d'ouverture", "Impossible d'ouvrir le fichier XML : " + e.getMessage());
        }
    }

    /**
     * Handles the "Quit" menu action. Exits the application.
     */
    @FXML
    public void handleLeft() {
        System.out.println("Application closed.");
        System.exit(0);
    }

    /**
     * Handles the "Export" menu action. Allows the user to export the library
     * data to a Word document.
     */
    @FXML
    public void handleExport() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exporter la Bibliothèque");
            fileChooser.setInitialFileName("Bibliotheque.docx");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Word", "*.docx"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                BookExporter exporter = new BookExporter();
                exporter.booksToWord(getTableView(), file.getAbsolutePath(), file.getName());
                showAlert(Alert.AlertType.INFORMATION, "Exportation réussie", "Les données ont été exportées dans " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur d'exportation", "Impossible d'exporter les données : " + e.getMessage());
        }
    }

    /**
     * Handles the "Save" menu action. Saves the current library data to the
     * default XML file path.
     */
    @FXML
    public void handleSave() {
        try {
            JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(xmlFilePath);
            marshaller.marshal(bibliotheque, file);

            showAlert(Alert.AlertType.INFORMATION, "Sauvegarde réussie", "Les données ont été sauvegardées dans " + xmlFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de sauvegarde", "Impossible de sauvegarder les données : " + e.getMessage());
        }
    }

    /**
     * Handles the "Save As" menu action. Allows the user to choose a new
     * location and filename to save the library data as an XML file.
     */
    @FXML
    public void handleSaveAs() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder sous");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                xmlFilePath = file.getAbsolutePath(); // Update default path for future saves
                JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                marshaller.marshal(bibliotheque, file);
                showAlert(Alert.AlertType.INFORMATION, "Sauvegarde réussie", "Les données ont été sauvegardées dans " + xmlFilePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de sauvegarde", "Impossible de sauvegarder les données : " + e.getMessage());
        }
    }

    /**
     * Handles the "About" menu action. Opens a new window displaying
     * information about the application.
     */
    @FXML
    public void handleAbout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bibliotheque/View/about.fxml"));
            Parent root = loader.load();
            Stage aboutStage = new Stage();
            aboutStage.setTitle("À propos");
            aboutStage.setScene(new Scene(root));
            aboutStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre 'À propos'.");
        }
    }

    /**
     * Handles the action when the "Emprunté" checkbox is toggled in the add form.
     * Displays a simple confirmation for borrowing.
     */
    @FXML
    private void handleEmprunteCheckBox() {
        // This simple checkbox only marks the book as borrowed without detailed borrower info in this view
        if (emprunteCheckBox.isSelected()) {
            showAlert(Alert.AlertType.INFORMATION, "Statut de l'emprunt", "Le livre sera marqué comme 'Indisponible' lors de l'ajout.");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Statut de l'emprunt", "Le livre sera marqué comme 'Disponible' lors de l'ajout.");
        }
        // No need for emprunteurTemporaire or empruntValide here if you're not collecting borrower details
        // directly in this form's context for a new book addition.
        // If you need to associate an Emprunteur with a NEW book, you'd integrate showEmprunteurDialog() here,
        // similar to how it was done before, and use the returned Emprunteur object.
        // For existing books, modification form handles setting the borrowed status.
    }

    /**
     * Adds a new book to the library based on the input fields.
     * Includes comprehensive validation for all fields.
     */
    @FXML
    public void ajouterLivre() {
        errorLabel.setText(""); // Clear previous errors

        // 1. Validate required fields are not empty
        if (!validateRequiredFields()) {
            return;
        }

        try {
            int parution = Integer.parseInt(parutionField.getText().trim());
            int colonne = Integer.parseInt(colonneField.getText().trim());
            int rangee = Integer.parseInt(rangeeField.getText().trim());

            // 2. Validate numerical constraints
            if (!validateNumericalConstraints(parution, colonne, rangee)) {
                return;
            }

            // 3. Validate uniqueness (Title, Author, Parution Year)
            if (isBookDuplicate(titreField.getText().trim(), nomField.getText().trim(), parution)) {
                errorLabel.setText("Oups ! Un livre avec ce titre, auteur et date de parution existe déjà.");
                return;
            }

            // 4. Create and populate new Livre object
            Livre nouveauLivre = new Livre();
            nouveauLivre.setTitre(titreField.getText().trim());

            Auteur auteur = new Auteur();
            auteur.setNom(nomField.getText().trim());
            auteur.setPrenom(prenomField.getText().trim());
            nouveauLivre.setAuteur(auteur);

            nouveauLivre.setPresentation(presentationField.getText().trim());
            nouveauLivre.setParution(parution);
            nouveauLivre.setColonne(colonne);
            nouveauLivre.setRangee(rangee);
            nouveauLivre.setPathImage(pathImageField.getText().trim());
            nouveauLivre.setEmprunte(emprunteCheckBox.isSelected()); // Set borrowed status from checkbox

            // 5. Add to model and observable list
            bibliotheque.addLivre(nouveauLivre);
            livresObservable.add(nouveauLivre);
            tableView.refresh(); // Ensure TableView updates

            // 6. Clear input fields and show success message
            clearInputFields();
            errorLabel.setText("Ce livre a été bien ajouté !");

        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer des valeurs numériques valides pour l'année, colonne et rangée.");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Une erreur inattendue est survenue : " + e.getMessage());
        }
    }

    /**
     * Validates that all required text fields for adding a book are not empty.
     *
     * @return true if all fields are filled, false otherwise.
     */
    private boolean validateRequiredFields() {
        if (titreField.getText().trim().isEmpty() ||
                nomField.getText().trim().isEmpty() ||
                prenomField.getText().trim().isEmpty() ||
                presentationField.getText().trim().isEmpty() ||
                parutionField.getText().trim().isEmpty() ||
                colonneField.getText().trim().isEmpty() ||
                rangeeField.getText().trim().isEmpty() ||
                pathImageField.getText().trim().isEmpty()) {
            errorLabel.setText("Tous les champs doivent être remplis.");
            return false;
        }
        return true;
    }

    /**
     * Validates numerical constraints for parution year, column, and row.
     *
     * @param parution The parsed parution year.
     * @param colonne The parsed column number.
     * @param rangee The parsed row number.
     * @return true if numerical constraints are met, false otherwise.
     */
    private boolean validateNumericalConstraints(int parution, int colonne, int rangee) {
        if (parution > LocalDate.now().getYear()) {
            errorLabel.setText("La date de parution ne doit pas être dans le futur.");
            return false;
        }
        if (colonne < MIN_COL_ROW_VALUE || colonne > MAX_COL_ROW_VALUE ||
                rangee < MIN_COL_ROW_VALUE || rangee > MAX_COL_ROW_VALUE) {
            errorLabel.setText("La colonne et la rangée doivent être comprises entre " + MIN_COL_ROW_VALUE + " et " + MAX_COL_ROW_VALUE + ".");
            return false;
        }
        return true;
    }

    /**
     * Checks if a book with the given title, author, and parution year already exists.
     *
     * @param titre The title to check.
     * @param nomAuteur The author's last name to check.
     * @param parution The parution year to check.
     * @return true if a duplicate is found, false otherwise.
     */
    private boolean isBookDuplicate(String titre, String nomAuteur, int parution) {
        for (Livre livre : bibliotheque.getLivres()) {
            if (livre.getTitre().equalsIgnoreCase(titre) &&
                    livre.getAuteur() != null &&
                    livre.getAuteur().getNom().equalsIgnoreCase(nomAuteur) &&
                    livre.getParution() == parution) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all input fields in the add book form.
     */
    private void clearInputFields() {
        titreField.clear();
        nomField.clear();
        prenomField.clear();
        presentationField.clear();
        parutionField.clear();
        colonneField.clear();
        rangeeField.clear();
        pathImageField.clear();
        emprunteCheckBox.setSelected(false);
    }

    /**
     * Deletes the currently selected book from the library after a confirmation.
     */
    @FXML
    public void supprimerLivre() {
        Livre livreToDelete = tableView.getSelectionModel().getSelectedItem();

        if (livreToDelete != null) {
            // Confirmation dialog for deletion
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Supprimer le livre '" + livreToDelete.getTitre() + "' ?");
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce livre ? Cette action est irréversible.");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                bibliotheque.deleteLivre(livreToDelete);
                livresObservable.remove(livreToDelete);
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le livre a été supprimé.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un livre à supprimer.");
        }
    }

    /**
     * Adds an "Actions" column to the TableView with "Modifier" (Modify) and "Supprimer" (Delete) buttons.
     */
    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifierButton = new Button("Modifier");
            private final Button supprimerButton = new Button("Supprimer");

            {
                modifierButton.setOnAction(event -> {
                    Livre livreSelectionne = getTableView().getItems().get(getIndex());
                    if (livreSelectionne != null) {
                        ouvrirFormulaireModification(livreSelectionne);
                    }
                });

                supprimerButton.setOnAction(event -> {
                    // Directly call supprimerLivre to trigger the confirmation dialog
                    tableView.getSelectionModel().select(getIndex()); // Select the row before deleting
                    supprimerLivre();
                });

                modifierButton.setStyle("-fx-background-color: #eab676; -fx-text-fill: white;");
                supprimerButton.setStyle("-fx-background-color: #33636f; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(10, modifierButton, supprimerButton));
                }
            }
        });
    }

    /**
     * Updates the TableView with the books from the provided Bibliotheque object.
     *
     * @param newBibliotheque The Bibliotheque object to load into the TableView.
     */
    private void chargerBibliothequeDansVue(Bibliotheque newBibliotheque) {
        this.bibliotheque = newBibliotheque; // Update the main bibliotheque object
        livresObservable.setAll(bibliotheque.getLivres()); // Replace all items in observable list
        tableView.refresh();
    }

    /**
     * Opens the modification form for a selected book.
     *
     * @param livreSelectionne The Livre object to be modified.
     */
    private void ouvrirFormulaireModification(Livre livreSelectionne) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bibliotheque/View/Update.fxml"));
            Parent root = loader.load();

            FormulaireModificationController controller = loader.getController();
            // This is the crucial change: pass both the book and this controller directly
            controller.initialiserFormulaire(livreSelectionne, this);

            Stage stage = new Stage();
            stage.setTitle("Modifier le Livre");
            stage.setScene(new Scene(root));
            stage.setMaximized(true); // Maximize the window
            stage.initModality(Modality.APPLICATION_MODAL); // Make it modal
            stage.showAndWait(); // Block until the form is closed

            // After modification form closes, refresh the table to reflect changes
            tableView.refresh();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire de modification.");
        }
    }

    /**
     * Utility method to display alert messages to the user.
     *
     * @param alertType The type of alert (e.g., INFORMATION, ERROR, WARNING).
     * @param title The title of the alert dialog.
     * @param message The content message of the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Getter for the TableView. Used by other controllers (e.g., FormulaireModificationController)
     * to refresh the table.
     *
     * @return The TableView instance.
     */
    public TableView<Livre> getTableView() {
        return tableView;
    }
}