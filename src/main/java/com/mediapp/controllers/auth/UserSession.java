package com.mediapp.controllers.auth;

public final class UserSession {

    //private UserSession instance;

    private String username;
    private String privilege;

    public UserSession(String username, String privilege) {
        this.username = username;
        this.privilege = privilege;
    }

    /*public UserSession getInstance(String username, String privilege) {
        if(instance == null) {
            instance = new UserSession(username, privilege);
        }
        return instance;
    }*/

    public String getUsername() {
        return username;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void cleanUserSession() {
        username = "";// or null
        privilege = "";// or null
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + username + '\'' +
                ", privilege=" + privilege +
                '}';
    }
}
