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

public class DoctorForgotPassword implements Initializable {
    @FXML
    private TextField username_field;
    @FXML
    private PasswordField new_password_field;
    @FXML
    private PasswordField confirm_new_password_field;
    @FXML
    private Button update_password_button;
    @FXML
    private Label forgot_password_message_label;
    @FXML
    private AnchorPane forgot_password_anchorPane;
    @FXML
    private Hyperlink main_menu_hlink;
    @FXML
    private Hyperlink signup_hlink;
    @FXML
    private Hyperlink login_hlink;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        forgot_password_anchorPane.setOnMousePressed(e -> forgot_password_anchorPane.requestFocus());

        update_password_button.setOnAction(e -> forgotPasswordHandler());
        main_menu_hlink.setOnAction(e -> goToMainMenu());
        signup_hlink.setOnAction(e -> goToSignupPage());
        login_hlink.setOnAction(e -> goToLoginPage());

        new_password_field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                clearMessage();
                if (new_password_field.getText().length() < 7) {
                    new_password_field.setText("");
                    sendMessage("Password should be at least 7 characters");
                    new_password_field.requestFocus();
                }
            }
        });

        confirm_new_password_field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                clearMessage();
                String password = new_password_field.getText();
                if (!password.matches("") || !password.matches(null)) {
                    if (!confirm_new_password_field.getText().matches(password)) {
                        confirm_new_password_field.setText("");
                        sendMessage("The passwords don't match");
                        //confirm_new_password_field.requestFocus();
                    }
                }
            }
        });
    }

    private void forgotPasswordHandler(){
        if(username_field.getText().isEmpty()){
            sendMessage("Username required"); return;
        }
        if(new_password_field.getText().isEmpty()){
            sendMessage("New password required"); return;
        }
        if(confirm_new_password_field.getText().isEmpty()){
            sendMessage("Confirm new password"); return;
        }

        if (checkUsername()){
            if (!matchOldPassword()) {
                if (updatePassword()) {
                    System.out.println("Password has been updated");
                    sendMessage("Password has been updated");
                }
                else {
                    sendMessage("Could not update password");
                }
                clearAllFields();
            }
            else {
                sendMessage("Please use a new password");
                clearAllFields();
            }
        }
        else {
            System.out.println("Wrong username");
            sendMessage("Wrong username");
            clearAllFields();
        }

    }

    private boolean checkUsername(){
        try {
            String userName = username_field.getText().trim();

            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            String url = "SELECT * FROM doctors WHERE username = ?";
            preparedStatement = connection.prepareStatement(url);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        }
        catch (Exception e){e.printStackTrace();}
        return false;
    }

    private boolean matchOldPassword(){
        try {
            String userName = username_field.getText().trim();
            String password = new_password_field.getText().trim();
            String hashPassword = CryptWithMD5.cryptWithMD5(password);

            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            String url = "SELECT * FROM passwords WHERE username = ? and password=?";
            preparedStatement = connection.prepareStatement(url);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, hashPassword);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        }
        catch (Exception e){e.printStackTrace();}
        return true;
    }

    private boolean updatePassword(){
        try {
            String userName = username_field.getText().trim();
            String password = new_password_field.getText().trim();
            String hashPassword = CryptWithMD5.cryptWithMD5(password);

            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            PreparedStatement preparedStatement = null;
            String url;

            url = "UPDATE doctors set password=? WHERE username = ?";
            preparedStatement = connection.prepareStatement(url);
            preparedStatement.setString(1, hashPassword);
            preparedStatement.setString(2, userName);
            preparedStatement.executeUpdate();

            url = "INSERT into passwords(username,password) values(?,?)";
            preparedStatement = connection.prepareStatement(url);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, hashPassword);
            preparedStatement.executeUpdate();

            return true;
        }
        catch (Exception e){e.printStackTrace();}
        return false;
    }


    private void sendMessage(String message){
        forgot_password_message_label.setText("*"+message);
    }
    private void clearMessage(){
        forgot_password_message_label.setText("");
    }

    private void goToMainMenu(){
        System.out.println("Main menu hlink clicked");
        new helperClass().goToMainMenu();
    }

    private void goToSignupPage(){
        System.out.println("Signup page hlink clicked");
        new helperClass().loadPage("/views/auth/doctor_signup.fxml");
    }

    private void goToLoginPage(){
        System.out.println("Login page hlink clicked");
        new helperClass().loadPage("/views/auth/doctor_login.fxml");
    }

    private void clearAllFields(){
        username_field.setText("");
        new_password_field.setText("");
        confirm_new_password_field.setText("");
    }
}
