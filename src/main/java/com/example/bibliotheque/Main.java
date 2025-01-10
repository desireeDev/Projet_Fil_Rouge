package com.example.bibliotheque;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;




import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage)throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/Biblio.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Ma Bibliothèque");
        stage.setScene(scene);
        stage.show();
    }
}