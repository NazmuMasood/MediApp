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
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.scene.control.Hyperlink;

public class DoctorSignup implements Initializable {
    @FXML
    private TextField
            user_fname_field,
            user_lname_field,
            username_field,
            sex_field,
            age_field,
            degree_field,
            specialization_field;
    @FXML
    private PasswordField
            password_field,
            confirm_password_field;
    @FXML
    private Button signup_button;
    @FXML
    private Label signup_message_label;
    @FXML
    private AnchorPane signup_anchorPane;
    @FXML
    private Hyperlink main_menu_hlink;
    @FXML
    private Hyperlink login_hlink;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signup_anchorPane.setOnMousePressed(e -> signup_anchorPane.requestFocus());

        signup_button.setOnAction(e -> signupHandler());
        main_menu_hlink.setOnAction(e -> goToMainMenu());
        login_hlink.setOnAction(e -> goToLoginPage());

        user_fname_field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost [a-zA-Z].*
                clearMessage();
                if (!user_fname_field.getText().matches("^[a-zA-Z]+$")) {
                    // when it not matches the pattern
                    // set the textField empty
                    //username_field.setText("");
                    sendMessage("first name can contain only alphabets");
                    user_fname_field.requestFocus();
                }
            }
        });
        user_lname_field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost [a-zA-Z].*
                clearMessage();
                if (!user_lname_field.getText().matches("^[a-zA-Z]+$")) {
                    // when it not matches the pattern
                    // set the textField empty
                    //username_field.setText("");
                    sendMessage("last name can contain only alphabets");
                    user_lname_field.requestFocus();
                }
            }
        });
        username_field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost [a-zA-Z].*
                clearMessage();
                if (!username_field.getText().matches("^[a-zA-Z0-9]+$")) {
                    // when it not matches the pattern
                    // set the textField empty
                    //username_field.setText("");
                    sendMessage("username can contain only alphabets");
                    username_field.requestFocus();
                }
            }
        });

        password_field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                clearMessage();
                if (password_field.getText().length() < 7) {
                    password_field.setText("");
                    sendMessage("password should be at least 7 characters");
                    password_field.requestFocus();
                }
            }
        });

        confirm_password_field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                clearMessage();
                String password = password_field.getText();
                if (!password.matches("") || !password.matches(null)) {
                    if (!confirm_password_field.getText().matches(password)) {
                        confirm_password_field.setText("");
                        sendMessage("Passwords don't match");
                        //confirm_password_field.requestFocus();
                    }
                }
            }
        });

        sex_field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                clearMessage();
                if (!sex_field.getText().matches("\\bmale|Male|female|Female\\b")) {
                    sex_field.setText("");
                    sendMessage("Please use only male or female");
                    sex_field.requestFocus();
                }
            }
        });

        age_field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                clearMessage();
                if (!age_field.getText().matches("(?<=\\s|^)\\d+(?=\\s|$)")) {
                    age_field.setText("");
                    sendMessage("Please use only numbers for age");
                    age_field.requestFocus();
                }
            }
        });

        degree_field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                clearMessage();
                if (!degree_field.getText().matches("\\bMBBS|FCPS|MRCS|FRCS|MD|mbbs|fcps|mrcs|md|frcs\\b")) {
                    degree_field.setText("");
                    sendMessage("Please use a valid degree");
                    degree_field.requestFocus();
                }
            }
        });

        specialization_field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                clearMessage();
                if (!specialization_field.getText().matches(
                        "\\bmedicine|surgery|gynae|pediatrics|cardiology|hepatology|urology|neurology|hematology\\b"
                )) {
                    specialization_field.setText("");
                    sendMessage("Please use valid specialization");
                    specialization_field.requestFocus();
                }
            }
        });

    }

/*
        //triedToDoSomething
        @FXML
        private MenuButton sex_field;
        Boolean cbCheck1 = false; Boolean cbCheck2 = false;

        public void triedToDoSomething(){
        // create the data to show in the CheckComboBox
        final ObservableList<String> strings = FXCollections.observableArrayList("Item 1","Item 2");

       */
/*      // Create the CheckComboBox with the data
        final CheckComboBox<String> checkComboBox = new CheckComboBox<>(strings);

        // and listen to the relevant events (e.g. when the selected indices or
        // selected items change).
        checkComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                //System.out.println(checkComboBox.getCheckModel().getSelectedItems());
                System.out.println("Hello");
            }
        });*//*


        CheckBox cb0 = new CheckBox("x");
        CustomMenuItem item0 = new CustomMenuItem(cb0);
        CheckBox cb1 = new CheckBox("y");
        CustomMenuItem item1 = new CustomMenuItem(cb1);
        item0.setHideOnClick(false);
        item1.setHideOnClick(false);
        sex_field.getItems().setAll(item0,item1);
        cb0.setOnAction(e -> cb0(cb0));
        cb1.setOnAction(e -> System.out.println("Y is selected"));

    }

    public void cb0(CheckBox cb0){
//        if (!cbCheck1){cbCheck1 = true; System.out.println("X is selected");}
//        else {cbCheck1 = false; System.out.println("X is de-selected");}
        if (cb0.isSelected()){cbCheck1 = true; System.out.println("X is selected");}
        else {cbCheck1 = false; System.out.println("X is de-selected");}
    }
*/


    private void signupHandler(){
        if(user_fname_field.getText().isEmpty()){
            sendMessage("First name required"); return;
        }
        if(user_lname_field.getText().isEmpty()){
            sendMessage("Last name required"); return;
        }
        if(username_field.getText().isEmpty()){
            sendMessage("Username required"); return;
        }
        if(sex_field.getText().isEmpty()){
            sendMessage("Sex required"); return;
        }
        if(age_field.getText().isEmpty()){
            sendMessage("Age required"); return;
        }
        if(degree_field.getText().isEmpty()){
            sendMessage("Degree required"); return;
        }
        if(specialization_field.getText().isEmpty()){
            sendMessage("Specialization required"); return;
        }
        if(password_field.getText().isEmpty()){
            sendMessage("Password required"); return;
        }
        if(confirm_password_field.getText().isEmpty()){
            sendMessage("Please confirm password"); return;
        }

        if (!userAlreadyExists()) {
            signup();
            clearAllFields();
            sendMessage("Signup success");
        }
        else {
            System.out.println("User already exists");
            sendMessage("User with that username already exists");
        }
    }

    private void sendMessage(String message){
        signup_message_label.setText("*"+message);
    }
    private void clearMessage(){
        signup_message_label.setText("");
    }

    public void goToMainMenu(){
        System.out.println("Main menu hlink clicked from signup page");
        new helperClass().goToMainMenu();
    }
    private void goToLoginPage(){
        System.out.println("Login page hlink clicked");
        new helperClass().loadPage("/views/auth/doctor_login.fxml");
    }

    private boolean userAlreadyExists(){
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            String url; Statement statement; ResultSet resultSet;

            //Checking for duplicate username in doctors table
            url = "SELECT * from doctors where username='"+username_field.getText()+"'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(url);
            if(resultSet.next()) {
                System.out.println("Duplicate value found!");
                return true;
            }

            //Checking for duplicate username in doctors table
            url = "SELECT * from patients where username='"+username_field.getText()+"'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(url);
            if(resultSet.next()) {
                System.out.println("Duplicate value found!");
                return true;
            }

            //Checking for duplicate username in doctors table
            url = "SELECT * from admins where username='"+username_field.getText()+"'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(url);
            if(resultSet.next()) {
                System.out.println("Duplicate value found!");
                return true;
            }

            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Database error");
            return true;
        }
    }

    private void signup(){
        try {
            String currentDateTime = getCurrentDateTime();
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            String url;

            String hashPassword = CryptWithMD5.cryptWithMD5(password_field.getText());

            url = "INSERT INTO doctors (user_fname,user_lname,username,password,age,sex,degree,specialization,add_date)" +
                    "VALUES ('"+user_fname_field.getText()+
                    "','"+user_lname_field.getText()+
                    "','"+username_field.getText()+
                    "','"+hashPassword+
                    "','"+age_field.getText()+
                    "','"+sex_field.getText()+
                    "','"+degree_field.getText()+
                    "','"+specialization_field.getText()+
                    "','"+currentDateTime+"')";

            Statement statement = connection.createStatement();
            statement.executeUpdate(url);

            PreparedStatement preparedStatement;
            url = "INSERT into passwords(username,password) values(?,?)";
            preparedStatement = connection.prepareStatement(url);
            preparedStatement.setString(1, username_field.getText());
            preparedStatement.setString(2, hashPassword);
            preparedStatement.executeUpdate();

            System.out.println("Signup success!");
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Signup error");
        }
    }

    private String getCurrentDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String s = dtf.format(now);
        System.out.println(s);
        return s;
    }

    private void clearAllFields(){
        user_fname_field.setText("");
        user_lname_field.setText("");
        username_field.setText("");
        age_field.setText("");
        sex_field.setText("");
        specialization_field.setText("");
        degree_field.setText("");
        password_field.setText("");
        confirm_password_field.setText("");
    }

}
