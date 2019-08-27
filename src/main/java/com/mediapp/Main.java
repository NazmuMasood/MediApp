package com.mediapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage parentWindow;
    @Override
    public void start(Stage primaryStage) throws Exception{
        parentWindow = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("/views/main_menu.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/appIconSmall.png")));
        primaryStage.setTitle("MediApp");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
