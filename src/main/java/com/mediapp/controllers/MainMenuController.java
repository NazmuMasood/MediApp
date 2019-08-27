package com.mediapp.controllers;

import com.mediapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private ImageView doctor_icon;
    @FXML
    private ImageView patient_icon;
    @FXML
    private ImageView management_icon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Program has been initialized");

        //Show title / info of the imageView
        //Not working, at least, for this project
        /*
        Tooltip.install(doctor_icon, new Tooltip("Doctor panel"));
        Tooltip.install(patient_icon, new Tooltip("Patient panel"));
        Tooltip.install(management_icon, new Tooltip("Management panel"));
        */

        doctor_icon.setPickOnBounds(true); // allows click on transparent areas
        doctor_icon.setOnMouseClicked(e -> getUserType("Doctor"));

        patient_icon.setPickOnBounds(true); // allows click on transparent areas
        patient_icon.setOnMouseClicked(e -> getUserType("Patient"));

        management_icon.setPickOnBounds(true); // allows click on transparent areas
        management_icon.setOnMouseClicked(e -> getUserType("Management"));
    }

    public void getUserType(String userType){
        try {
            System.out.println("User is : "+userType+" \nLoading login menu..");

            if(userType.equals("Doctor")){
                new helperClass().loadPage("/views/auth/doctor_login.fxml");
            }
            if(userType.equals("Patient")){
                new helperClass().loadPage("/views/auth/patient_login.fxml");
            }
            if(userType.equals("Management")){
                new helperClass().loadPage("/views/auth/admin_login.fxml");
            }


        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
