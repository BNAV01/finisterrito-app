package com.ingsoftware.api.models;

import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "idusuario")
    private User user;

    @OneToMany(mappedBy = "id")
    @JoinColumn(name = "idrepository")
    private Repository repository;

    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;


    @PrePersist
    public void prePersist() {
        uploadDate = new Date();
    }
}
