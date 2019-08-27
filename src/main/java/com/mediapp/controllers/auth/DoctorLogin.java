package com.mediapp.controllers.auth;

import com.mediapp.controllers.ConnectionClass;
import com.mediapp.controllers.CryptWithMD5;
import com.mediapp.controllers.helperClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DoctorLogin implements Initializable {
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
    @FXML
    private Hyperlink signup_hlink;
    @FXML
    private Hyperlink forgot_password_hlink;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_anchorPane.setOnMousePressed(e -> login_anchorPane.requestFocus());

        login_button.setOnAction(e -> loginHandler());
        main_menu_hlink.setOnAction(e -> goToMainMenu());
        signup_hlink.setOnAction(e -> goToSignupPage());
        forgot_password_hlink.setOnAction(e -> setNewPassword());
    }

    public void loginHandler(){
        if(username_field.getText().isEmpty()){
            sendMessage("Username required"); return;
        }
        if(password_field.getText().isEmpty()){
            sendMessage("Password required"); return;
        }

        if (checkCredentials()){
            System.out.println("Login success : @"+username_field.getText());
            sendMessage("Login success : @"+username_field.getText());
           // login();
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

            String url = "SELECT * FROM doctors WHERE username = ? and password = ?";
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
        //System.out.println("Main menu hlink clicked");
        new helperClass().goToMainMenu();
    }

    private void goToSignupPage(){
        //System.out.println("Signup page hlink clicked");
        new helperClass().loadPage("/views/auth/doctor_signup.fxml");
    }

    private void setNewPassword(){
        //System.out.println("Forgot password hlink clicked");
        new helperClass().loadPage("/views/auth/doctor_forgot_password.fxml");
    }

    private void clearAllFields(){
        username_field.setText("");
        password_field.setText("");
    }
}
