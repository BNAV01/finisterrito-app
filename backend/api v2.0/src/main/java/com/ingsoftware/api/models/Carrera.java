package com.ingsoftware.api.models;


import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombreCarrera;

    @OneToMany(mappedBy = "idcareer")
    private List<User> estudiantes;

    private boolean estado;

}
