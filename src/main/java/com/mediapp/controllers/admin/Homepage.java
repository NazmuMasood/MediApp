package com.mediapp.controllers.admin;

import com.mediapp.controllers.auth.AdminLogin;
import com.mediapp.controllers.auth.UserSession;
import com.mediapp.controllers.helperClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
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
    private Button patients_button,
            drugs_button,
            doctors_button;

    String username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username = AdminLogin.user.getUsername();
        profile_hlink.setText(username);

        homepage_anchorPane.setOnMousePressed(e -> homepage_anchorPane.requestFocus());

        profile_hlink.setOnAction(e -> System.out.println("Username is " + username));
        logout_hlink.setOnAction(e -> logoutHandler());

        drugs_button.setOnAction(e -> new helperClass().loadPage("/views/admin/admin_drugs.fxml"));
        //patients_button.setOnAction(e -> new helperClass().loadPage("/views/admin/admin_patients.fxml"));
        //doctors_button.setOnAction(e -> new helperClass().loadPage("/views/admin/admin_doctors.fxml"));
    }

    public static void logoutHandler(){
        new helperClass().goToMainMenu();
        //Clears the session variable
        AdminLogin.user.cleanUserSession();
        System.out.println("After logging out, User is : @"+AdminLogin.user.getUsername());
    }

}


