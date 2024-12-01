package com.example.librarysystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/librarysystem/Main.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Library System");
        Image icon = new Image("Book_Icon.png");

        Scene scene = new Scene(root, 1200, 700);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("C:\\Users\\User\\IdeaProjects\\Test1Dec1\\CAT-201-Asgmn1\\src\\main\\resources\\com\\example\\librarysystem\\Background.css")).toExternalForm());

        primaryStage.setScene(scene);

        primaryStage.getIcons().add(icon);
        primaryStage.setScene(new Scene(root, 1200, 700));


        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

