package com.example.bibliotheque.Controller;

import com.example.bibliotheque.Model.Bibliotheque;
import com.example.bibliotheque.Model.Emprunteur; // Keep if still used for Livre model, even if not for full details
import com.example.bibliotheque.Model.Livre;
import com.example.bibliotheque.Model.Auteur;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell; // Important for text field editing
import javafx.util.converter.IntegerStringConverter; // For integer columns
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.awt.FileDialog;

import utils.BookExporter;
import utils.DatabaseConnection;

/**
 * Contrôleur principal de l'application de gestion de bibliothèque.
 * Gère les interactions utilisateur avec l'interface graphique,
 * la logique métier liée aux livres et emprunteurs,
 * et la persistance des données via XML.
 */
public class BibliothequeController {

    // --- Section 1: Constantes de l'application ---
    private static final String DEFAULT_XML_PATH = "src/main/resources/Biblio.xml";
    private static final String EXPORT_DOCX_NAME = "Bibliotheque.docx";
    private static final int MIN_COL_ROW_VALUE = 1;
    private static final int MAX_COL_ROW_VALUE = 7; // Limites pour colonne et rangée

    // --- Section 2: Éléments FXML (UI) ---

    // TableView et ses colonnes
    @FXML private TableView<Livre> tableView;

    // Colonnes du tableau, liées aux propriétés de Livre
    @FXML private TableColumn<Livre,Integer> idColumn;
    @FXML private TableColumn<Livre, String> titleColumn;
    @FXML private TableColumn<Livre, String> authorColumn;
    @FXML private TableColumn<Livre, String> presentationColumn;
    @FXML private TableColumn<Livre, Integer> parutionColumn;
    @FXML private TableColumn<Livre, Integer> colonneColumn;
    @FXML private TableColumn<Livre, Integer> rangeeColumn;
    @FXML private TableColumn<Livre, String> pathImageColumn;
    @FXML private TableColumn<Livre, String> statutColumn;
    @FXML private TableColumn<Livre, Void> actionsColumn; // Colonne pour les boutons d'action (Modifier/Supprimer)

    // Champs de saisie pour l'ajout d'un livre
    @FXML private TextField titreField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField presentationField;
    @FXML private TextField parutionField;
    @FXML private TextField colonneField;
    @FXML private TextField rangeeField;
    @FXML private TextField pathImageField;
    @FXML private Label errorLabel; // Pour afficher les messages d'erreur ou de succès

    // MenuItems pour sauvegarde
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem saveAsMenuItem;

    // CheckBox pour indiquer si le livre est emprunté
    @FXML private CheckBox emprunteCheckBox;

    // --- Section 3: Variables d'état interne du contrôleur ---

    private boolean empruntValide = false; // Indique si la case "Livre emprunté" est cochée pour un nouvel ajout

    // Chemin actuel du fichier XML pour la sauvegarde/ouverture
    private String xmlFilePath = DEFAULT_XML_PATH;

    // Liste observable des livres, utilisée pour alimenter la TableView et mettre à jour l'interface automatiquement
    private ObservableList<Livre> livresObservable = FXCollections.observableArrayList();

    // Objet Bibliotheque contenant la collection principale des livres
    private Bibliotheque bibliotheque;

    // --- Section 4: Initialisation du contrôleur ---

    /**
     * Constructeur du contrôleur.
     * Tente de désérialiser la bibliothèque depuis le fichier XML par défaut au démarrage.
     * Si le fichier n'existe pas ou si la désérialisation échoue, une nouvelle bibliothèque vide est créée.
     */
    public BibliothequeController() {
        try {
            JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
            File fichierXML = new File(xmlFilePath);
            if (fichierXML.exists()) {
                bibliotheque = (Bibliotheque) context.createUnmarshaller().unmarshal(fichierXML);
            } else {
                bibliotheque = new Bibliotheque(); // Crée une nouvelle bibliothèque si le fichier n'existe pas
            }
        } catch (JAXBException e) {
            System.err.println("Erreur lors du chargement initial de la bibliothèque depuis XML: " + e.getMessage());
            e.printStackTrace();
            bibliotheque = new Bibliotheque(); // Assure que bibliotheque n'est jamais null
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible de charger la bibliothèque. Une nouvelle bibliothèque vide a été créée.");
        }
    }

    /**
     * Méthode d'initialisation appelée automatiquement par JavaFX après le chargement du FXML.
     * Configure les liaisons des colonnes du tableau, charge les données et ajoute les boutons d'action.
     */
    @FXML
    public void initialize() {
        // Le tableView ne sera PAS editable par défaut.
        // tableView.setEditable(true); // C'est maintenant géré par le bouton Modifier
        setupTableColumnBindings();
        setupEditableColumns(); // Configure les colonnes pour l'édition inline
        chargerBibliothequeAView(bibliotheque);
        ajouterBoutonsActions(); // Inclut maintenant les boutons Modifier et Supprimer
    }

    /**
     * Configure les liaisons des cellules de chaque colonne du TableView avec les propriétés
     * correspondantes de l'objet Livre.
     */
    private void setupTableColumnBindings() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        authorColumn.setCellValueFactory(cellData -> {
            Auteur auteur = cellData.getValue().getAuteur();
            return new SimpleStringProperty(auteur != null ? (auteur.getNom() + " " + auteur.getPrenom()) : "Inconnu");
        });
        presentationColumn.setCellValueFactory(new PropertyValueFactory<>("presentation"));
        parutionColumn.setCellValueFactory(new PropertyValueFactory<>("parution"));
        colonneColumn.setCellValueFactory(new PropertyValueFactory<>("colonne"));
        rangeeColumn.setCellValueFactory(new PropertyValueFactory<>("rangee"));
        pathImageColumn.setCellValueFactory(new PropertyValueFactory<>("pathImage"));
        statutColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isEmprunte() ? "Indisponible" : "Disponible"));
    }

    /**
     * Configure quelles colonnes sont éditables et comment elles gèrent les mises à jour.
     * Les CellFactories sont toujours configurées, mais l'édition n'est possible que si
     * tableView.setEditable(true) est activé.
     */
    private void setupEditableColumns() {
        // Titre
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setOnEditCommit(event -> {
            Livre livre = event.getRowValue();
            String newValue = event.getNewValue().trim();
            if (!newValue.isEmpty()) {
                livre.setTitre(newValue);
                showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Titre mis à jour.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Modification invalide", "Le titre ne peut pas être vide.");
                tableView.refresh(); // Revert to old value
            }
        });

        // Auteur (Nom et Prénom)
        // Pour une modification en ligne, nous allons cibler spécifiquement le nom et le prénom de l'auteur.
        // Cela nécessiterait des colonnes séparées pour Nom Auteur et Prénom Auteur dans le FXML.
        // Si vous ne voulez qu'une seule colonne "Auteur", l'édition directe du nom et prénom ensemble
        // est complexe et il est préférable de ne pas la rendre directement éditable ou de la faire via un popup.
        // POUR CET EXEMPLE, JE VAIS ASSUMER QUE VOUS N'AVEZ QU'UNE COLONNE "Auteur" ET DONC ELLE NE SERA PAS DIRECTEMENT EDITABLE.
        // SI VOUS VOULEZ QU'ELLE LE SOIT, IL FAUDRAIT CRÉER DES COLONNES NOM_AUTEUR ET PRÉNOM_AUTEUR DANS LE FXML.
        // authorColumn.setCellFactory(...) - Désactivé pour l'édition directe si c'est une colonne combinée.

        // Présentation
        presentationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        presentationColumn.setOnEditCommit(event -> {
            Livre livre = event.getRowValue();
            String newValue = event.getNewValue().trim();
            if (!newValue.isEmpty()) {
                livre.setPresentation(newValue);
                showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Présentation mise à jour.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Modification invalide", "La présentation ne peut pas être vide.");
                tableView.refresh();
            }
        });

        // Parution (Integer)
        parutionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        parutionColumn.setOnEditCommit(event -> {
            Livre livre = event.getRowValue();
            Integer newValue = event.getNewValue();
            if (newValue != null && newValue > 0 && newValue <= LocalDate.now().getYear()) {
                livre.setParution(newValue);
                showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Année de parution mise à jour.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Modification invalide", "L'année de parution doit être un nombre valide (ex: 2023) et ne peut pas être dans le futur.");
                tableView.refresh();
            }
        });

        // Colonne (Integer)
        colonneColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colonneColumn.setOnEditCommit(event -> {
            Livre livre = event.getRowValue();
            Integer newValue = event.getNewValue();
            if (newValue != null && newValue >= MIN_COL_ROW_VALUE && newValue <= MAX_COL_ROW_VALUE) {
                livre.setColonne(newValue);
                showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Colonne mise à jour.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Modification invalide", "La colonne doit être un nombre entre " + MIN_COL_ROW_VALUE + " et " + MAX_COL_ROW_VALUE + ".");
                tableView.refresh();
            }
        });

        // Rangée (Integer)
        rangeeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        rangeeColumn.setOnEditCommit(event -> {
            Livre livre = event.getRowValue();
            Integer newValue = event.getNewValue();
            if (newValue != null && newValue >= MIN_COL_ROW_VALUE && newValue <= MAX_COL_ROW_VALUE) {
                livre.setRangee(newValue);
                showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Rangée mise à jour.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Modification invalide", "La rangée doit être un nombre entre " + MIN_COL_ROW_VALUE + " et " + MAX_COL_ROW_VALUE + ".");
                tableView.refresh();
            }
        });

        // Path Image
        pathImageColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        pathImageColumn.setOnEditCommit(event -> {
            Livre livre = event.getRowValue();
            String newValue = event.getNewValue().trim();
            if (!newValue.isEmpty()) {
                livre.setPathImage(newValue);
                showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Chemin d'image mis à jour.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Modification invalide", "Le chemin de l'image ne peut pas être vide.");
                tableView.refresh();
            }
        });

        // Statut (ComboBox)
        statutColumn.setCellFactory(param -> new TableCell<Livre, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("Disponible", "Indisponible"));

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    comboBox.setValue(item); // Set current value
                    // Désactive le ComboBox si le tableau n'est pas en mode éditable
                    comboBox.setDisable(!tableView.isEditable()); // IMPORTANT: pour désactiver l'édition si le tableau n'est pas editable

                    comboBox.setOnAction(event -> {
                        Livre livre = getTableView().getItems().get(getIndex());
                        livre.setEmprunte(comboBox.getValue().equals("Indisponible"));
                        showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Statut mis à jour.");
                        getTableView().refresh(); // Refresh the row to ensure the new status is immediately reflected
                    });
                    setGraphic(comboBox);
                }
            }
        });
    }

    /**
     * Récupère la TableView.
     * @return La TableView des livres.
     */
    public TableView<Livre> getTableView() {
        return tableView;
    }

    /**
     * Charge les livres de l'objet {@code Bibliotheque} dans la {@code TableView}.
     * @param biblio L'objet Bibliotheque dont les livres doivent être chargés.
     */
    private void chargerBibliothequeAView(Bibliotheque biblio) {
        if (biblio != null) {
            livresObservable = FXCollections.observableArrayList(biblio.getLivres());
            tableView.setItems(livresObservable);
        }
    }

    /**
     * Ajoute une colonne "Actions" au TableView avec des boutons "Modifier" et "Supprimer"
     * pour chaque ligne de livre.
     */
    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifierButton = new Button("Modifier");
            private final Button supprimerButton = new Button("Supprimer");

            {
                modifierButton.setOnAction(event -> {
                    Livre selectedLivre = getTableView().getItems().get(getIndex());
                    if (selectedLivre != null) {
                        // Active l'édition pour cette ligne spécifique
                        tableView.edit(getIndex(), titleColumn); // Commence l'édition sur la colonne du titre
                        tableView.setEditable(true); // Rend le TableView éditable globalement
                        showAlert(Alert.AlertType.INFORMATION, "Mode Édition Activé", "Vous pouvez maintenant modifier les cellules du tableau. Appuyez sur Entrée pour valider ou Échap pour annuler.");
                    }
                });

                supprimerButton.setOnAction(event -> supprimerLivre());

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

    // --- Section 5: Gestion des fichiers (XML et Export) ---

    @FXML
    public void handleOpen() {
        FileDialog fd = new FileDialog((java.awt.Frame)null, "Ouvrir un fichier XML", FileDialog.LOAD);
        fd.setFile("*.xml");
        fd.setVisible(true);

        String directory = fd.getDirectory();
        String filename = fd.getFile();

        if (filename == null || directory == null) {
            System.out.println("Opération d'ouverture annulée.");
            return;
        }

        xmlFilePath = directory + filename;
        File selectedFile = new File(xmlFilePath);

        try {
            JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            bibliotheque = (Bibliotheque) unmarshaller.unmarshal(selectedFile);
            chargerBibliothequeAView(bibliotheque);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Bibliothèque chargée depuis " + xmlFilePath);
        } catch (JAXBException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur d'ouverture", "Impossible de charger le fichier XML : " + e.getMessage());
            System.err.println("Détail de l'erreur JAXB lors de l'ouverture : " + e.getMessage());
        }
    }

    @FXML
    public void handleLeft() {
        System.out.println("Fermeture de l'application. Au revoir !");
        System.exit(0);
    }

    @FXML
    public void handleExport() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exporter la bibliothèque en Word");
            fileChooser.setInitialFileName(EXPORT_DOCX_NAME);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Word", "*.docx"));
            File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());

            if (file != null) {
                BookExporter exporter = new BookExporter();
                exporter.booksToWord(tableView, file.getAbsolutePath(), file.getName());
                showAlert(Alert.AlertType.INFORMATION, "Exportation réussie", "Les données ont été exportées vers " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur d'exportation", "Impossible d'exporter les données : " + e.getMessage());
            System.err.println("Détail de l'erreur d'exportation : " + e.getMessage());
        }
    }

    @FXML
    public void handleSave() {
        try {
            // Optionnel : Désactiver l'édition du tableau avant de sauvegarder si vous voulez être sûr
            // tableView.setEditable(false);
            saveBibliothequeToXML(new File(xmlFilePath));
            showAlert(Alert.AlertType.INFORMATION, "Sauvegarde réussie", "Les données ont été sauvegardées dans " + xmlFilePath);
        } catch (JAXBException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de sauvegarde", "Impossible de sauvegarder les données : " + e.getMessage());
            System.err.println("Détail de l'erreur JAXB lors de la sauvegarde : " + e.getMessage());
        }
    }

    @FXML
    public void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder sous");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));
        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());

        if (file != null) {
            xmlFilePath = file.getAbsolutePath();
            handleSave();
        }
    }

    private void saveBibliothequeToXML(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(bibliotheque, file);
    }

    @FXML
    public void handleAbout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bibliotheque/View/about.fxml"));
            Parent root = loader.load();
            Stage aboutStage = new Stage();
            aboutStage.setTitle("À propos");
            aboutStage.setScene(new Scene(root));
            aboutStage.initModality(Modality.APPLICATION_MODAL);
            aboutStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page 'À propos'.");
            System.err.println("Détail de l'erreur de chargement FXML : " + e.getMessage());
        }
    }

    // --- Section 6: Gestion des livres (Ajout, Suppression, Modification) ---

    @FXML
    public void ajouterLivre() {
        errorLabel.setText("");

        String titre = titreField.getText().trim();
        String nomAuteur = nomField.getText().trim();
        String prenomAuteur = prenomField.getText().trim();
        String presentation = presentationField.getText().trim();
        String parutionText = parutionField.getText().trim();
        String colonneText = colonneField.getText().trim();
        String rangeeText = rangeeField.getText().trim();
        String pathImage = pathImageField.getText().trim();

        if (!validateInputFields(titre, nomAuteur, prenomAuteur, presentation, parutionText,
                colonneText, rangeeText, pathImage)) {
            return;
        }

        try {
            int parution = Integer.parseInt(parutionText);
            int colonne = Integer.parseInt(colonneText);
            int rangee = Integer.parseInt(rangeeText);

            if (!validateNumericalConstraints(parution, colonne, rangee)) {
                return;
            }

            if (!isBookUnique(titre, nomAuteur, parution)) {
                return;
            }

            createAndAddBook(titre, nomAuteur, prenomAuteur, presentation, parution,
                    colonne, rangee, pathImage, empruntValide);

            clearFormAndRefreshUI();

            errorLabel.setText("Ce livre a été bien ajouté !");

        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer des valeurs numériques valides pour l'année, la colonne et la rangée.");
        }
    }

    private boolean validateInputFields(String titre, String nom, String prenom, String presentation,
                                        String parutionText, String colonneText, String rangeeText, String pathImage) {
        if (titre.isEmpty() || nom.isEmpty() || prenom.isEmpty() || presentation.isEmpty() ||
                parutionText.isEmpty() || colonneText.isEmpty() || rangeeText.isEmpty() || pathImage.isEmpty()) {
            errorLabel.setText("Tous les champs doivent être remplis.");
            return false;
        }
        return true;
    }

    private boolean validateNumericalConstraints(int parution, int colonne, int rangee) {
        if (parution > LocalDate.now().getYear()) {
            errorLabel.setText("La date de parution ne doit pas dépasser l'année actuelle.");
            return false;
        }
        if (colonne < MIN_COL_ROW_VALUE || colonne > MAX_COL_ROW_VALUE ||
                rangee < MIN_COL_ROW_VALUE || rangee > MAX_COL_ROW_VALUE) {
            errorLabel.setText("La colonne et la rangée doivent être comprises entre " + MIN_COL_ROW_VALUE + " et " + MAX_COL_ROW_VALUE + ".");
            return false;
        }
        return true;
    }

    private boolean isBookUnique(String titre, String nomAuteur, int parution) {
        for (Livre livre : bibliotheque.getLivres()) {
            if (livre.getTitre().equalsIgnoreCase(titre) &&
                    livre.getAuteur().getNom().equalsIgnoreCase(nomAuteur) &&
                    livre.getParution() == parution) {
                errorLabel.setText("Oups ! Un livre avec ce titre, auteur et date de parution existe déjà.");
                return false;
            }
        }
        return true;
    }

    private void createAndAddBook(String titre, String nomAuteur, String prenomAuteur,
                                  String presentation, int parution, int colonne, int rangee,
                                  String pathImage, boolean empruntValide) {
        Livre nouveauLivre = new Livre();
        nouveauLivre.setTitre(titre);

        Auteur auteur = new Auteur();
        auteur.setNom(nomAuteur);
        auteur.setPrenom(prenomAuteur);
        nouveauLivre.setAuteur(auteur);

        nouveauLivre.setPresentation(presentation);
        nouveauLivre.setParution(parution);
        nouveauLivre.setColonne(colonne);
        nouveauLivre.setRangee(rangee);
        nouveauLivre.setPathImage(pathImage);

        nouveauLivre.setEmprunte(empruntValide);

        bibliotheque.addLivre(nouveauLivre);
        livresObservable.add(nouveauLivre);
    }

    private void clearFormAndRefreshUI() {
        tableView.refresh();
        titreField.clear();
        nomField.clear();
        prenomField.clear();
        presentationField.clear();
        parutionField.clear();
        colonneField.clear();
        rangeeField.clear();
        pathImageField.clear();
        emprunteCheckBox.setSelected(false);
        empruntValide = false;
    }

    @FXML
    public void supprimerLivre() {
        Livre livretoDelete = tableView.getSelectionModel().getSelectedItem();

        if (livretoDelete != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmer la suppression");
            confirmationAlert.setHeaderText("Supprimer le livre : \"" + livretoDelete.getTitre() + "\" ?");
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce livre de la bibliothèque ? Cette action est irréversible.");

            ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                bibliotheque.deleteLivre(livretoDelete);
                livresObservable.remove(livretoDelete);
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le livre \"" + livretoDelete.getTitre() + "\" a été supprimé avec succès.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Suppression annulée", "La suppression du livre a été annulée.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun livre sélectionné", "Veuillez sélectionner un livre dans le tableau pour le supprimer.");
        }
    }

    // --- Section 7: Gestion des emprunts ---

    @FXML
    private void handleEmprunteCheckBox() {
        if (emprunteCheckBox.isSelected()) {
            empruntValide = true;
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Livre marqué comme emprunté !");
        } else {
            empruntValide = false;
            showAlert(Alert.AlertType.INFORMATION, "Statut mis à jour", "Livre marqué comme disponible.");
        }
    }

    // --- Section 8: Méthodes utilitaires génériques ---

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}