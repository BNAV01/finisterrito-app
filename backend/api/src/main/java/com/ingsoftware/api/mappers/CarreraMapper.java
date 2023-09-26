package com.ingsoftware.api.mappers;

import com.ingsoftware.api.DTO.CarreraDTO;
import com.ingsoftware.api.models.Carrera;

public class CarreraMapper {
    public static CarreraDTO createDTO(Carrera carrera){
        return CarreraDTO.builder()
                .id(carrera.getId())
                .nombreCarrera(carrera.getNombreCarrera())
                .estudiantes(carrera.getEstudiantes().stream().map(UserMapper::createDTO).toList())
                .build();
    }
}

