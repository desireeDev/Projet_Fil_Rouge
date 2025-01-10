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

import java.io.File;
import java.time.LocalDate;

public class BibliothequeController {
//Define the tableColumns
    @FXML private TableView<Livre> tableView;
    @FXML private TableColumn<Livre, String> titleColumn;
    @FXML private TableColumn<Livre, String> authorColumn;
    @FXML private TableColumn<Livre, String> presentationColumn;
    @FXML private TableColumn<Livre, Integer> parutionColumn;
    @FXML private TableColumn<Livre, Integer> colonneColumn;
    @FXML private TableColumn<Livre, Integer> rangeeColumn;
    @FXML private TableColumn<Livre, String> pathImageColumn;

    @FXML private TextField titreField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField presentationField;
    @FXML private TextField parutionField;
    @FXML private TextField colonneField;
    @FXML private TextField rangeeField;
    @FXML private TextField pathImageField;

    @FXML private Label errorLabel;

    private ObservableList<Livre> livresObservable;
    private Bibliotheque bibliotheque;

    public BibliothequeController() {
        try {
            JAXBContext context = JAXBContext.newInstance(Bibliotheque.class);
            //Code pour désérialiser le fichier XML
            bibliotheque = (Bibliotheque) context.createUnmarshaller().unmarshal(new File("src/main/resources/Biblio.xml"));
        } catch (JAXBException e) {
            bibliotheque = new Bibliotheque();
        }
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        authorColumn.setCellValueFactory(cellData -> {
            Auteur auteur = cellData.getValue().getAuteur();
            return new javafx.beans.property.SimpleStringProperty(auteur.getNom() + " " + auteur.getPrenom());
        });
        presentationColumn.setCellValueFactory(new PropertyValueFactory<>("presentation"));
        parutionColumn.setCellValueFactory(new PropertyValueFactory<>("parution"));
        colonneColumn.setCellValueFactory(new PropertyValueFactory<>("colonne"));
        rangeeColumn.setCellValueFactory(new PropertyValueFactory<>("rangee"));
        pathImageColumn.setCellValueFactory(new PropertyValueFactory<>("pathImage"));

        livresObservable = FXCollections.observableArrayList(bibliotheque.getLivres());
        tableView.setItems(livresObservable);
    }

    @FXML
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
                errorLabel.setText(" La date de parution ne peut pas dépasser l'année actuelle.");
                return;
            }

            //  Contraintes : Colonne et rangée doivent être entre 1 et 7
            if (colonne < 1 || colonne > 7 || rangee < 1 || rangee > 7) {
                errorLabel.setText("La  colonne et  la rangée doivent être comprises entre 1 et 7.");
                return;
            }

            //  Contrainte : Unicité sur Titre, Auteur et Date de Parution
            for (Livre livre : bibliotheque.getLivres()) {
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

            bibliotheque.addLivre(livre);
            livresObservable.add(livre);
            //Mise à jour de la table
            tableView.refresh();

            errorLabel.setText(" Ce livre a été bien ajoutée !");
        } catch (NumberFormatException e) {
            errorLabel.setText(" Veuillez entrer des valeurs numériques valides svp.");
        }
    }
    //Conception de la fonction deleteLivre
    @FXML
    public void deleteLivre(){
        Livre livretoDelete= new Livre();
      //  if(bibliotheque(livretodelelete))


    }

    @FXML
    public void updateLivre(){}
}
