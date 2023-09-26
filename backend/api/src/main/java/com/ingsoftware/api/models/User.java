package com.ingsoftware.api.models;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "ApplicationUser")
@Data
//LOGICA DE NEGOCIO LISTA PARA TRABAJAR
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //Se pide en DTO
    @Column(unique = true, nullable = false)
    private String rut; //Se pide en DTO
    private String firstname; //Se pide en DTO
    private String middlename; //Se pide en DTO
    private String lastname; //Se pide en DTO
    private String email; //Se pide en DTO
    private String password;
    @ManyToOne
    @JoinColumn(name = "idcareer")
    private Carrera idcareer; //Se pide en DTO
    private Date careerEntry; //Se pide en DTO
    private String RefreshToken;
    private String phoneNumber; //Se pide en DTO
    private String tokenChangePassword;
    private Timestamp RefreshTokenExpirationDate;
    private boolean isActive;
    private boolean isLockout;
    private int passwordAttempt; //Se pide en DTO
    private UUID tranferDataId;
    @OneToOne()
    @JoinColumn(referencedColumnName = "id")
    private Rol rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority(getRol().getName()));
        return list;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return getFirstname() + " " + ((getMiddlename().isBlank() || getMiddlename().isEmpty()) ? "." : getMiddlename()) + " " + getLastname();
    }

    public User(Integer id, String rut, String firstname, @Nullable String middlename, String lastname, String email, String password, Carrera idcareer , Date careerEntry, String phoneNumber) {
        this.id = id;
        this.rut = rut;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.idcareer = idcareer;
        this.careerEntry = careerEntry;
        this.phoneNumber = phoneNumber;
    }

    //Constructor vacio con finalidad de buena practica :v
    public User() {

    }
}
