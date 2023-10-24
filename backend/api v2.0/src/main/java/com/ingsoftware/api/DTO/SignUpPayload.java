package com.ingsoftware.api.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

public class SignUpPayload {
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private int rol;
    private String phoneNumber;
    @JsonProperty("active")
    private boolean isActive;
    @JsonProperty("lockout")
    private boolean isLockout;

    public SignUpPayload(String email, String firstName, @Nullable String middleName, String lastName, String password, Integer rol, String phoneNumber, boolean isActive, boolean isLockout) {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.password = password;
        this.rol = rol;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.isLockout = isLockout;
    }

    public SignUpPayload() {
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {return this.phoneNumber;}

    public String setPhoneNumber(){return this.phoneNumber;}

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName == null ? "." : middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public boolean isActive() {return isActive;}

    public boolean isLockout() {return isLockout;}
}
