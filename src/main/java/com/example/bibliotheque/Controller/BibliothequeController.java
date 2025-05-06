package com.example.bibliotheque.Controller;
import com.example.bibliotheque.Model.Bibliotheque;
import com.example.bibliotheque.Model.Emprunteur;
import com.example.bibliotheque.Model.Livre;
import com.example.bibliotheque.Model.Auteur;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import utils.BookExporter;
//import utils.LibraryMySql;

public class BibliothequeController {
    //public MenuItem itemOuvrir;
    //Define the tableColumns who contains the elements
    //FXML to lier les elements definis dans la vue au controleur java
    //Definition of tableau who shows les objets de type Livre
    @FXML private TableView<Livre> tableView;
//Getter pour la récupération vu que tableView est déclaré private
    public TableView<Livre> getTableView() {
        return tableView;
    }
    //Define the column of tableView
    @FXML private TableColumn<Livre, String> titleColumn;
    @FXML private TableColumn<Livre, String> authorColumn;
    @FXML private TableColumn<Livre, String> presentationColumn;
    @FXML private TableColumn<Livre, Integer> parutionColumn;
    @FXML private TableColumn<Livre, Integer> colonneColumn;
    @FXML private TableColumn<Livre, Integer> rangeeColumn;
    @FXML private TableColumn<Livre, String> pathImageColumn;
    @FXML  private TableColumn<Livre, String> statutColumn;
    @FXML private TableColumn<Livre, Void> actionsColumn;
//TextField=Champs de saisie des attributs
    @FXML private TextField titreField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField presentationField;
    @FXML private TextField parutionField;
    @FXML private TextField colonneField;
    @FXML private TextField rangeeField;
    @FXML private TextField pathImageField;
    @FXML private Label errorLabel;
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem saveAsMenuItem;
    // CheckBox pour indiquer si le livre est emprunté
    @FXML private CheckBox emprunteCheckBox;
    @FXML private Emprunteur emprunteurTemporaire = null; // Stockage temporaire de l'emprunteur
    @FXML private boolean empruntValide = false; // Indique si le formulaire a été validé

    private String xmlFilePath = "src/main/resources/Biblio.xml";

    // Déclaration et initialisation d'une liste observable de type Livre.
// FXCollections.observableArrayList() crée une liste dynamique qui
// surveille les modifications apportées à son contenu (ajouts, suppressions, modifications).
// Cette liste sera  utilisée ici  pour alimenter la TableView et mettre à jour automatiquement notre interface user(View).
private ObservableList<Livre> livresObservable = FXCollections.observableArrayList();

//Objet who contains la collection(ensemble) des livres
    private Bibliotheque bibliotheque;
//Désérialisation du fichier XML
// JAXB and Unmarshaller seront Utilisés pour lire notre  fichier XML et charger les données dans l'objet bibliotheque.
public BibliothequeController() {
    try {
        JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
        File fichierXML = new File(xmlFilePath);
        //createUnmarshaller cree un outil de conversion de XMl en java et unmarshal fait la conversion
        if (fichierXML.exists()) {
            bibliotheque = (Bibliotheque) context.createUnmarshaller().unmarshal(fichierXML);
        } else {
            bibliotheque = new Bibliotheque();
        }
    } catch (JAXBException e) {
        e.printStackTrace();
    }
}
//FXML pour faire la liason entre la vue et le controller
    @FXML
    public void initialize() {
    //Liaison des données
    //sellCellValueFactory permet de lier chaque column à son attribut correspondant dans la table Livre
    //PropertyValueFactory  lie la propriété de l'objet Livre  à une colonne du TableView.
        titleColumn.setCellValueFactory(new PropertyValueFactory<Livre,String>("titre"));
        authorColumn.setCellValueFactory(cellData -> {
            Auteur auteur = cellData.getValue().getAuteur();
            return new javafx.beans.property.SimpleStringProperty(auteur.getNom() + " " + auteur.getPrenom());
        });
        presentationColumn.setCellValueFactory(new PropertyValueFactory<>("presentation"));
        parutionColumn.setCellValueFactory(new PropertyValueFactory<>("parution"));
        colonneColumn.setCellValueFactory(new PropertyValueFactory<>("colonne"));
        rangeeColumn.setCellValueFactory(new PropertyValueFactory<>("rangee"));
        pathImageColumn.setCellValueFactory(new PropertyValueFactory<>("pathImage"));
        // Configurer la colonne "Statut" pour la gestion des emprunts
        statutColumn.setCellValueFactory(cellData -> {
            boolean emprunte = cellData.getValue().isEmprunte();

            return new javafx.beans.property.SimpleStringProperty(emprunte ? "Indisponible" : "Disponible");
        });

        ChargerBibliothequeAView(bibliotheque);
        // Ajouter la colonne d'actions avec les boutons
        ajouterBoutonsActions();
    }
    @FXML
    public void handleOpen() throws JAXBException {
        // Code pour gérer l'action "Ouvrir"
        File xmlStream;
        Bibliotheque b;
        JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        FileDialog fd = new FileDialog((java.awt.Frame)null, "Choose a file", FileDialog.LOAD);
        fd.setFile("*.xml");
        fd.setVisible(true);
        String filename = fd.getFile();
        if (filename == null) {
            System.out.println("You cancelled the choice");
            return;
        }
        else
            System.out.println("You chose " + fd.getDirectory() + filename);

        xmlFilePath = fd.getDirectory() + fd.getFile();
        xmlStream = new File(xmlFilePath);
        b =  (Bibliotheque) unmarshaller.unmarshal(xmlStream);
        ChargerBibliothequeAView(b);
    }

    @FXML
    public void handleLeft() {
        // Code pour gérer l'action "Quitter"
        System.out.println("Bye Bye!");
        // Ferme l'application
        System.exit(0);
    }
//Fonction pour l'exportation
    @FXML
    public void handleExport() {
        // Code pour gérer l'action  d'Exportation
        System.out.println("Voulez vous exportez!");

        try {
            //LibraryMySql qs = new LibraryMySql("jdbc:mysql://127.0.0.1:3306/ProjetFilRouge", "root", "password");

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exporter");
            fileChooser.setInitialFileName("Bibliotheque.docx");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers word", "*.docx"));
            // Ouvre une boîte de dialogue pour choisir un fichier
            File file = fileChooser.showSaveDialog(null);

            System.out.println(file.getName());
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getCanonicalPath());

            if (file != null) {
                BookExporter a = new BookExporter();
                a.booksToWord(getTableView(), file.getAbsolutePath(), file.getName());

                // Confirmation
                showAlert(Alert.AlertType.INFORMATION, "Sauvegarde réussie", "Les données ont été sauvegardées dans " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur d'export", "Impossible d'exporter les données : " + e.getMessage());
        }
    }

    // Méthode pour sauvegarder dans un fichier XML
    @FXML
    public void handleSave() {
        try {
            // Crée un contexte JAXB pour la classe Bibliotheque
            JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Spécifie un fichier par défaut
            File file = new File(xmlFilePath);
            marshaller.marshal(bibliotheque, file);

            // Confirmation
            showAlert(Alert.AlertType.INFORMATION, "Sauvegarde réussie", "Les données ont été sauvegardées dans " + xmlFilePath);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de sauvegarde", "Impossible de sauvegarder les données : " + e.getMessage());
        }
    }

    // Méthode pour sauvegarder dans un fichier XML
    @FXML
    public void handleSaveAs() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder sous");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));
            // Ouvre une boîte de dialogue pour choisir un fichier
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                xmlFilePath =  file.getAbsolutePath();
                System.out.println("xmlFilePath : " + xmlFilePath);
                JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                marshaller.marshal(bibliotheque, file);

                // Confirmation
                showAlert(Alert.AlertType.INFORMATION, "Sauvegarde réussie", "Les données ont été sauvegardées dans " + xmlFilePath);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de sauvegarde", "Impossible de sauvegarder les données : " + e.getMessage());
        }
    }

    // Méthode utilitaire pour afficher des messages à l'utilisateur
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //isaac
    @FXML
    public void handleAbout() {
        try {
            // Charger le fichier about.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bibliotheque/View/about.fxml"));
            Parent root = loader.load();
            // Créer une nouvelle fenêtre
            Stage aboutStage = new Stage();
            aboutStage.setTitle("À propos");
            aboutStage.setScene(new Scene(root));
            aboutStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//Fonction pour gerer l'emprunt
    @FXML
    private void handleEmprunteCheckBox() {
        if (emprunteCheckBox.isSelected()) {
            // Si l'emprunteur a déjà été défini, ne pas rouvrir le DialogPanel
            if (empruntValide) {
                showAlert(Alert.AlertType.INFORMATION, "Info", "Le livre est déjà marqué comme emprunté.");
                return;
            }
            // Sinon, afficher la boîte de dialogue
            emprunteurTemporaire = showEmprunteurDialog(); // Ouvre la boîte de dialogue
            if (emprunteurTemporaire != null) {
                empruntValide = true; // Marquer comme validé
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Le livre a été bien emprunté !");
            } else {
                emprunteCheckBox.setSelected(false); // Décocher si l'utilisateur annule
            }
        } else {
            // Réinitialiser si la case est décochée
            emprunteurTemporaire = null;
            empruntValide = false;
        }
    }

//Creation de la fonction showEmprunteurDialog
    private Emprunteur showEmprunteurDialog() {
        try {
            // Charger la boîte de dialogue depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bibliotheque/View/emprunteur_dialog.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Prêter un livre");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            // Récupérer le contrôleur
            EmprunteurDialogController controller = loader.getController();

            // Afficher et attendre l'interaction utilisateur
            dialogStage.showAndWait();

            // Retourner l'objet Emprunteur si confirmé, sinon null
            return controller.getEmprunteur();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @FXML
    //****Conception de la fonction addLivre***//
    public void ajouterLivre() {
        errorLabel.setText("");
        // ✅ Validation des champs vides
        if (titreField.getText().trim().isEmpty() || nomField.getText().trim().isEmpty() ||
                prenomField.getText().trim().isEmpty() || presentationField.getText().trim().isEmpty() ||
                parutionField.getText().trim().isEmpty() || colonneField.getText().trim().isEmpty() ||
                rangeeField.getText().trim().isEmpty() || pathImageField.getText().trim().isEmpty()) {
            errorLabel.setText(" Tous les champs doivent être remplis.");
            return;
        }

        try {
            int parution = Integer.parseInt(parutionField.getText().trim());
            int colonne = Integer.parseInt(colonneField.getText().trim());
            int rangee = Integer.parseInt(rangeeField.getText().trim());

            //Contrainte : Date de parution ne doit pas dépasser l'année actuelle
            if (parution > LocalDate.now().getYear()) {
                errorLabel.setText(" La date de parution ne doit pas dépasser l'année actuelle.");
                return;
            }

            //  Contraintes : Colonne et rangée doivent être entre 1 et 7
            if (colonne < 1 || colonne > 7 || rangee < 1 || rangee > 7) {
                errorLabel.setText("La  colonne et  la rangée doivent être comprises entre 1 et 7.");
                return;
            }

            //  Contrainte : Unicité sur Titre, Auteur et Date de Parution
            //Boucle parcourant tous les livres présents dans la biblio
            for (Livre livre : bibliotheque.getLivres()) {
                //equalsIgnoreCase est une méthode qui permet de comparer deux chaines de caractere
                if (livre.getTitre().equalsIgnoreCase(titreField.getText().trim()) &&
                        livre.getAuteur().getNom().equalsIgnoreCase(nomField.getText().trim()) &&
                        livre.getParution() == parution) {
                    errorLabel.setText("Oups Un livre avec ce titre, auteur et date de parution existe déjà.");
                    return;
                }
            }
            //Ajout du livre avec toutes les contraintes respectées
            Livre livre = new Livre();
            //Update du titre
            livre.setTitre(titreField.getText().trim());
            //Update des Informations de l'auteur
            Auteur auteur = new Auteur();
            auteur.setNom(nomField.getText().trim());
            auteur.setPrenom(prenomField.getText().trim());
            livre.setAuteur(auteur);
            //Update du champ de Presentation
            livre.setPresentation(presentationField.getText().trim());
            livre.setParution(parution);
            livre.setColonne(colonne);
            livre.setRangee(rangee);
            livre.setPathImage(pathImageField.getText().trim());

            // Vérifier si la case "Livre emprunté" est cochée
            if (empruntValide && emprunteurTemporaire != null) {
                livre.setEmprunteur(emprunteurTemporaire); // Associer l'emprunteur temporaire
                livre.setEmprunte(true); // Marquer comme emprunté
            } else {
                livre.setEmprunte(false); // Sinon, le livre est disponible
            }
            //Ajout du livre dans la biblio
            bibliotheque.addLivre(livre);
            livresObservable.add(livre);
            //Mise à jour de la table
            tableView.refresh();

            // Effacer les champs du formulaire
            titreField.clear();
            nomField.clear();
            prenomField.clear();
            presentationField.clear();
            parutionField.clear();
            colonneField.clear();
            rangeeField.clear();
            pathImageField.clear();
            emprunteCheckBox.setSelected(false); // Réinitialiser la Check
            // Réinitialiser l'état d'emprunt temporaire
            emprunteurTemporaire = null;
            empruntValide = false;
            errorLabel.setText(" Ce livre a été bien ajoutée !");
        } catch (NumberFormatException e) {
            errorLabel.setText(" Veuillez entrer des valeurs numériques valides svp.");
        }
    }
    //****Conception de la fonction deleteLivre***//
    @FXML
    public void supprimerLivre() {
        // Récupère le livre sélectionné dans la tableView
        Livre livretoDelete = tableView.getSelectionModel().getSelectedItem();

        // Vérifie si un livre a bien été sélectionné
        if (livretoDelete != null) {

            // Supprimer le livre de l'objet bibliotheque (le modèle de données principal)
            bibliotheque.deleteLivre(livretoDelete);

            // Supprimer le livre de la liste observable pour mettre à jour l'affichage dans la TableView
            livresObservable.remove(livretoDelete);

        } else {
            // Si aucun livre n'est sélectionné, afficher un message dans la console
            System.out.println("Pas de livre à supprimer d'accordr ?OK.");
        }
    }
    //Fonction pour les boutons
    /**
     * Ajoute une colonne "Actions" à la TableView contenant deux boutons : Modifier et Supprimer.
     * Chaque bouton est configuré avec un gestionnaire d'événements qui déclenche l'action correspondante.
     */
    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifierButton = new Button("Modifier");
            private final Button supprimerButton = new Button("Supprimer");

            {
                // Gestion de l'événement pour le bouton Modifier
                modifierButton.setOnAction(event -> {
                    Livre livreSelectionne = getTableView().getItems().get(getIndex());
                    if (livreSelectionne != null) {
                        ouvrirFormulaireModification(livreSelectionne);
                    } else {
                        errorLabel.setText("Veuillez sélectionner un livre à modifier.");
                    }
                });

                // Gestion de l'événement pour le bouton Supprimer
                supprimerButton.setOnAction(event -> {
                    supprimerLivre();
                });
                // Application de styles CSS aux boutons pour un meilleur rendu visuel
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
     *
     * @param bibliotheque La bibliotheque a rajouter dans la TableView
     */
    private void ChargerBibliothequeAView(Bibliotheque bibliotheque) {
        //Convertit la liste de livres existante (chargée depuis le fichier XML) en une liste observable pour que les modifications soient prises en compte par l'interface graphiqu
        livresObservable = FXCollections.observableArrayList(bibliotheque.getLivres());
        //Associe la listeObservale à notre tableView
        tableView.setItems(livresObservable);
    }

//**Fonction pour l'ouverture du fichier XML"
private void ouvrirFormulaireModification(Livre livreSelectionne) {
    try {
        // Charger le fichier FXML du formulaire
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bibliotheque/View/Update.fxml"));
        Parent root = loader.load();
        // Obtenir le contrôleur et lui passer l'objet sélectionné
        FormulaireModificationController controller = loader.getController();
        controller.initialiserFormulaire(livreSelectionne, this);
        // Créer et afficher une fenêtre modale
        Stage stage = new Stage();
        stage.setTitle("Modifier le Livre");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);//Maximisez la taille
        stage.initModality(Modality.APPLICATION_MODAL);  // Fenêtre modale
        stage.showAndWait();  // Bloque jusqu'à fermeture
    } catch (IOException e) {
        e.printStackTrace();
    }
}


}
