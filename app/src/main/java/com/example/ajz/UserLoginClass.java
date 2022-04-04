package com.example.ajz;

public class UserLoginClass {
    private int id;
    private String name;
    private String user;
    private String password;
    private String admin;


    public int getID() {
        return id;
    }

    public String getName(){
        return name;

    }

    public String getUser(){
        return user;

    }
    public String getPassword(){
        return password;

    }
    public String getAdmin(){
        return admin;

    }

    public void setPassword (String password) {
        this.password = password;
    }


}