package com.ingsoftware.api.DTO;
public class LoginPayload {
    private String email;
    private String password;

    public LoginPayload(String userName, String password) {
        this.email = userName;
        this.password = password;
    }

    public LoginPayload() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
