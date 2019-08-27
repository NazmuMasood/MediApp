package com.mediapp.controllers.auth;

import com.mediapp.Main;
import com.mediapp.controllers.ConnectionClass;
import com.mediapp.controllers.CryptWithMD5;
import com.mediapp.controllers.helperClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdminLogin implements Initializable {
    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Button login_button;
    @FXML
    private Label login_message_label;
    @FXML
    private AnchorPane login_anchorPane;
    @FXML
    private Hyperlink main_menu_hlink;
    private String username; private String privilege; public static UserSession user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_anchorPane.setOnMousePressed(e -> login_anchorPane.requestFocus());

        login_button.setOnAction(e -> loginHandler());
        main_menu_hlink.setOnAction(e -> goToMainMenu());
    }

    public void loginHandler(){
        if(username_field.getText().isEmpty()){
            sendMessage("Username required"); return;
        }
        if(password_field.getText().isEmpty()){
            sendMessage("Password required"); return;
        }

        if (checkCredentials()){
            System.out.println("Login success");
            sendMessage("Login success");

            setUserSession(username_field.getText(),"admin");
            login();
        }
        else {
            System.out.println("Wrong username & password combination");
            sendMessage("Wrong username & password combination");
            clearAllFields();
        }

    }

    private boolean checkCredentials(){
        try {
            String userName = username_field.getText().trim();
            String password = password_field.getText().trim();
            String hashPassword = CryptWithMD5.cryptWithMD5(password);

            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            String url = "SELECT * FROM admins WHERE username = ? and password = ?";
            preparedStatement = connection.prepareStatement(url);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, hashPassword);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                return false;
            }else {
                return true;
            }
        }
        catch (Exception e){e.printStackTrace();}
        return false;
    }


    private void sendMessage(String message){
        login_message_label.setText("*"+message);
    }

    private void goToMainMenu(){
        System.out.println("Main menu hlink clicked from login page");
        new helperClass().goToMainMenu();
    }

    private void clearAllFields(){
        username_field.setText("");
        password_field.setText("");
    }

    private void login(){
        //Loading the admin homepage
        new helperClass().loadPage("/views/admin/homepage.fxml");
    }

    private void setUserSession(String username, String privilege){
        this.username = username;
        this.privilege = privilege;

        //Setting session variable for current logged-in user
        user = UserSession.getInstace(username, privilege);
    }

}
