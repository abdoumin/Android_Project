package com.example.profile;

import java.util.HashMap;
import java.util.Map;

/**
 * user class to keep track of registered user
 * and their data (profile info)
 */

public class user {

    private String email;
    private String frstName;
    private String lastName;
    private String password;


    public user() {}

    public user(String frstName, String email) {
        this.frstName = frstName;
        this.email = email;
    }

    public user(String frstName, String email, String lastName,
                String password) {
        this.email = email;
        this.frstName = frstName;
        this.lastName = lastName;
        this.password = password;

    }

    public String getEmail() {
        return email;
    }

    public String getFrstName() {
        return frstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFrstName(String frstName) {
        this.frstName = frstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
/*    public HashMap<String, Object> getAsMap(){
        HashMap<String, Object> userAsMap = new HashMap<>();
        userAsMap.put("username",username);
        userAsMap.put("password",password);
        userAsMap.put("age",age);
        userAsMap.put("name",name);
        //Add or remove more key value pair
        return userAsMap;
    }*/


}
