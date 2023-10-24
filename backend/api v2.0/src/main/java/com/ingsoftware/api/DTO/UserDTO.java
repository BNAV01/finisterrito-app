package com.ingsoftware.api.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

//Contraseña nunca viaja, es unidireccional entre Front --> Back, nunca al reves.
@Data
@Builder
public class UserDTO {
    private int userId;
    private String rut;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private CarreraDTO carreraDTO;
    private Date careerEntry;
    private int passwordAttempt;
    @JsonProperty("active")
    private boolean isActive;
    private boolean isLockout;
    private RolDTO rol;
}
