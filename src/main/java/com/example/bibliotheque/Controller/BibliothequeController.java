package com.example.bibliotheque.Controller;
import com.example.bibliotheque.Model.Bibliotheque;
import com.example.bibliotheque.Model.Livre;
import com.example.bibliotheque.Model.Auteur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import javafx.scene.layout.HBox;

import java.io.File;
import java.time.LocalDate;
public class BibliothequeController {
//Define the tableColumns who contains the elements
    //FXML to lier les elements definis dans la vue au controleur java
    //Definition of tableau who shows les objets de type Livre
    @FXML private TableView<Livre> tableView;
    //Define the column of tableView
    @FXML private TableColumn<Livre, String> titleColumn;
    @FXML private TableColumn<Livre, String> authorColumn;
    @FXML private TableColumn<Livre, String> presentationColumn;
    @FXML private TableColumn<Livre, Integer> parutionColumn;
    @FXML private TableColumn<Livre, Integer> colonneColumn;
    @FXML private TableColumn<Livre, Integer> rangeeColumn;
    @FXML private TableColumn<Livre, String> pathImageColumn;
    @FXML private TableColumn<Livre, Void> actionsColumn; // Nouvelle colonne d'actio
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
        File fichierXML = new File("src/main/resources/Biblio.xml");
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
//Convertit la liste de livres existante (chargée depuis le fichier XML) en une liste observable pour que les modifications soient prises en compte par l'interface graphiqu
        livresObservable = FXCollections.observableArrayList(bibliotheque.getLivres());
        //Associe la listeObservale à notre tableView
        tableView.setItems(livresObservable);
        // Ajouter la colonne d'actions avec les boutons
        ajouterBoutonsActions();
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
//Ajout du livre dans la biblio
            bibliotheque.addLivre(livre);

            livresObservable.add(livre);
            //Mise à jour de la table
            tableView.refresh();

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
    //****Conception de la fonction UpdateLivree***//
    @FXML
    public void updateLivre() {
        // Récupérer l'élément sélectionné dans notreTableView
        Livre livreaModifier = tableView.getSelectionModel().getSelectedItem();

        // Vérifier si un livre est bien sélectionné
        if (livreaModifier != null) {

            // ✅ Validation des champs pour s'assurer qu'ils ne sont pas vides
            if (titreField.getText().trim().isEmpty() ||
                    nomField.getText().trim().isEmpty() ||
                    prenomField.getText().trim().isEmpty() ||
                    presentationField.getText().trim().isEmpty() ||
                    parutionField.getText().trim().isEmpty() ||
                    colonneField.getText().trim().isEmpty() ||
                    rangeeField.getText().trim().isEmpty() ||
                    pathImageField.getText().trim().isEmpty()) {
                System.out.println("Tous les champs doivent être remplis !");
                return; // Arrêter la méthode si un champ est vide
            }
            //  Modification des propriétés du livre sélectionné avec les valeurs saisies
            livreaModifier.setTitre(titreField.getText());
            livreaModifier.getAuteur().setNom(nomField.getText());
            livreaModifier.getAuteur().setPrenom(prenomField.getText());
            livreaModifier.setPathImage(pathImageField.getText());
            livreaModifier.setPresentation(presentationField.getText());
            livreaModifier.setParution(Integer.parseInt(parutionField.getText()));

            // Conversion et validation des valeurs numériques pour Colonne et Rangée
            try {
                int colonne = Integer.parseInt(colonneField.getText().trim());
                int rangee = Integer.parseInt(rangeeField.getText().trim());

                // Contraintes : Colonne et Rangée doivent être entre 1 et 7
                if (colonne < 1 || colonne > 7 || rangee < 1 || rangee > 7) {
                    System.out.println("La colonne et la rangée doivent être entre 1 et 7.");
                    return;
                }
                livreaModifier.setColonne(colonne);
                livreaModifier.setRangee(rangee);
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer des valeurs numériques valides pour Colonne et Rangée.");
                return; // Arrêter l'exécution en cas d'erreur de conversion
            }
            // Mise à jour de la table
            tableView.refresh();

            // En cas de bug
            System.out.println("Tell me where is the error !");
        } else {
            System.out.println("Veuillez sélectionner un livre à modifier.");
        }
    }

    //Fonction pour les boutons
    /**
     * Ajoute une colonne "Actions" à la TableView contenant deux boutons : Modifier et Supprimer.
     * Chaque bouton est configuré avec un gestionnaire d'événements qui déclenche l'action correspondante.
     */
    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {

            // Création des boutons pour chaque ligne
            private final Button Modifier = new Button("Modifier");
            private final Button Supprimer = new Button("Supprimer");

            // Bloc d'initialisation des boutons et des événements
            {
                //  Gestion de l'événement pour le bouton Modifier
                Modifier.setOnAction(event -> {
                    // Récupère le livre sélectionné dans la ligne correspondante
                    Livre livreSelectionne = getTableView().getItems().get(getIndex());

                    // Appelle la méthode de pré-remplissage des champs avec les données du livre sélectionné
                    preRemplirChamps(livreSelectionne);

                    // Appelle directement la méthode de mise à jour
                    updateLivre();
                });

                //  Gestion de l'événement pour le bouton Supprimer
                Supprimer.setOnAction(event -> {
                    // Appelle directement la méthode de suppression existante
                    supprimerLivre();
                });

                // Application de styles CSS aux boutons pour un meilleur rendu visuel
                Modifier.setStyle("-fx-background-color: #eab676; -fx-text-fill: white;");
                Supprimer.setStyle("-fx-background-color: #33636f; -fx-text-fill: white;");
            }

            /**
             * La méthode updateItem est appelée automatiquement par JavaFX à chaque mise à jour de la cellule.
             * Elle décide si les boutons doivent être affichés ou non, en fonction de la présence de données.
             */
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                // ✅ Si la cellule est vide, ne pas afficher de boutons
                if (empty) {
                    setGraphic(null);
                } else {
                    // ✅ Sinon, affiche les boutons Modifier et Supprimer dans un conteneur horizontal (HBox)
                    setGraphic(new HBox(10, Modifier, Supprimer));
                }
            }
        });
    }
    /**
     * Pré-remplit les champs du formulaire avec les informations du livre sélectionné.
     * Cette méthode est utilisée lors de la modification d'un livre pour précharger les données existantes.
     * @param livretoUpdate Le livre sélectionné dans la TableView
     */
    private void preRemplirChamps(Livre livretoUpdate) {
        // ✅ Met à jour les champs de saisie avec les données du livre
        titreField.setText(livretoUpdate.getTitre());
        nomField.setText(livretoUpdate.getAuteur().getNom());
        prenomField.setText(livretoUpdate.getAuteur().getPrenom());
        presentationField.setText(livretoUpdate.getPresentation());
        parutionField.setText(String.valueOf(livretoUpdate.getParution()));
        colonneField.setText(String.valueOf(livretoUpdate.getColonne()));
        rangeeField.setText(String.valueOf(livretoUpdate.getRangee()));
        pathImageField.setText(livretoUpdate.getPathImage());
    }


}
