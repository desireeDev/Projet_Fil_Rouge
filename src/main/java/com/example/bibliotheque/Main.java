package com.example.bibliotheque;

import javafx.application.Application;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BorderPane layout= new BorderPane();
        MenuBar menuBar=new MenuBar();
        layout.setTop(menuBar);
        Menu File= new Menu("Fichier");
        Menu Edition= new Menu("Edition");
        Menu About= new Menu("About");
        Scene scene = new Scene(layout, 320, 240);
        menuBar.setUseSystemMenuBar(true);
        menuBar.getMenus().addAll(File,Edition,About);
    //création des menus item
        MenuItem item1= new MenuItem("Ouvrir");
        MenuItem item2= new MenuItem("Quitter");
   //Récupération des items pour le fichier
   File.getItems().addAll(item1,item2);
   // Upload du fichier CSS
        scene.getStylesheets().add(getClass().getResource("View/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Ma Bibliotheque");
        stage.show();



    }
}
