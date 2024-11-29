package com.example.librarysystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();

            // Set up the scene
            Scene scene = new Scene(root);
            Image icon = new Image("Book_Icon.png");
            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("Library System");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Print any loading exceptions
        }
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
