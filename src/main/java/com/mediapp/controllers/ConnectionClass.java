package com.mediapp.controllers;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
    public Connection getConnection(){
        String dbName = "mediapp";
        String username = "root";
        String password = "";

        try{
            Class.forName("com.mysql.jdbc.Driver");

            return DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,username,password);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
