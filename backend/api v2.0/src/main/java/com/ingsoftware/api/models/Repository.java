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
public class Repository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String docname;

    @Lob
    private String documento;

}
