package com.example.bibliotheque;

import javafx.application.Application;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;



import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage)throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Ma Bibliothèque");
        stage.setScene(scene);
        stage.show();
    }
}
