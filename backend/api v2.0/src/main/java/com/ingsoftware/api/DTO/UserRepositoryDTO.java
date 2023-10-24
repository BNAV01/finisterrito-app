package com.ingsoftware.api.DTO;

import lombok.*;

import java.util.Date;

@Data
@Builder
public class UserRepositoryDTO {

    private int id;

    private UserDTO userDTO;

    private RepositoryDTO repository;

    private Date uploadDate;

}
