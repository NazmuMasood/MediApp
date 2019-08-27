package com.mediapp.controllers;

import com.mediapp.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class helperClass {
    public void goToMainMenu(){
        System.out.println("Going back to main menu");
        try {
            Parent root1 = FXMLLoader.load(getClass().getResource("/views/main_menu.fxml"));
            Stage stage = Main.parentWindow;
            stage.setScene(new Scene(root1));
            stage.show();

            /** For learning purpose
            //Parent root1 = FXMLLoader.load(getClass().getClassLoader().getResource("../views/main_menu.fxml"));

            //FXMLLoader fxmlLoader = new FXMLLoader();
            //fxmlLoader.setLocation(getClass().getResource("/main_menu.fxml"));

            //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../main_menu.fxml"));
            //Parent root1 = fxmlLoader.load();
             **/
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadPage(String url){
        System.out.println("Loading requested page...: "+url);
        try{
            Parent root1 = FXMLLoader.load(getClass().getResource(url));
            Stage stage = Main.parentWindow;
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void executeSqlQuery(String query){
//        ConnectionClass connectionClass = new ConnectionClass();
//        Connection connection = connectionClass.getConnection();
//        String url = query;
//
//
//
//    }
}
