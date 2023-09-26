package com.ingsoftware.api.mappers;


import com.ingsoftware.api.DTO.RolDTO;
import com.ingsoftware.api.models.Rol;

public class RolMapper {
    public static RolDTO createDTO(Rol rol) {
        return RolDTO.builder()
                .id(rol.getId())
                .name(rol.getName())
                .build();
    }
}
