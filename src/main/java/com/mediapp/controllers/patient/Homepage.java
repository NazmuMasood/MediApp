package com.mediapp.controllers.patient;

import com.mediapp.controllers.auth.PatientLogin;
import com.mediapp.controllers.helperClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Homepage implements Initializable {
    @FXML
    private AnchorPane homepage_anchorPane;
    @FXML
    private Hyperlink profile_hlink,
            logout_hlink;
    @FXML
    private Button drugs_button,
            doctors_button;

    String username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username = PatientLogin.user.getUsername();
        profile_hlink.setText(username);

        homepage_anchorPane.setOnMousePressed(e -> homepage_anchorPane.requestFocus());

        profile_hlink.setOnAction(e -> System.out.println("Username is " + username));
        logout_hlink.setOnAction(e -> logoutHandler());

        drugs_button.setOnAction(e -> new helperClass().loadPage("/views/patient/patient_order.fxml"));

    }

    public static void logoutHandler(){
        new helperClass().goToMainMenu();
        //Clears the session variable
        PatientLogin.user.cleanUserSession();
        System.out.println("After logging out, User is : "+PatientLogin.user.getUsername());
    }
}
