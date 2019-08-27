module MediApp {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;
    requires mysql.connector.java;
    requires org.controlsfx.controls;
    requires com.jfoenix;

    opens com.mediapp;
    opens com.mediapp.controllers;
    opens com.mediapp.controllers.auth;
    opens com.mediapp.controllers.doctor;
    opens com.mediapp.controllers.patient;
    opens com.mediapp.controllers.admin;


}