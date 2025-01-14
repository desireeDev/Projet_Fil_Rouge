package com.example.bibliotheque.Controller;
//Les importations utiles
import com.example.bibliotheque.Model.Bibliotheque;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.awt.*;
import java.io.File;

public class MenuController {
    @FXML
    private void handleOpen() throws JAXBException {
        // Code pour gérer l'action "Ouvrir"
        String filePath;
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

        filePath = fd.getDirectory() + fd.getFile();
        xmlStream = new File(filePath);
        b =  (Bibliotheque) unmarshaller.unmarshal(xmlStream);
    }

    @FXML
    private void handleLeft() {
        // Code pour gérer l'action "Quitter"
        System.out.println("Bye Bye!");
        // Ferme l'application
        System.exit(0);
    }

    @FXML
    private void handleSave() {
        // Code pour gérer l'action "Sauvegarder"
        System.out.println("Sauvegarder clicked!");
    }
//Lien entre la vue et le controller
    @FXML
    private void handleSaveOn() {
        // Code pour gérer l'action "Sauvegarder sous"
        System.out.println("Sauvegarder sous!");
    }

    @FXML
    private void handleInfos() {
        // Code pour gérer l'action "Sauvegarder sous"
        System.out.println("Voici les informations!");
    }

    @FXML
    private void handleAbout() {
        // Code pour afficher un message d'info dans le menu "About"
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Ma Bibliothèque");
        alert.setContentText("Version 1.0");
        alert.showAndWait();
    }
}
