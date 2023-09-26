package com.ingsoftware.api.services;


import com.ingsoftware.api.DTO.CarreraDTO;
import com.ingsoftware.api.exceptions.CarreraException;
import com.ingsoftware.api.models.Carrera;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CarreraService {
    //GetById DTO

    private final com.ingsoftware.api.repository.ICarreraRepository ICarreraRepository;

    //Create

    public CarreraDTO create(CarreraDTO carreraDTO){
        Carrera carrera = Carrera.builder()

                .nombreCarrera(carreraDTO.getNombreCarrera())
                .build();

        carrera = ICarreraRepository.save(carrera);
        carreraDTO.setId(carrera.getId());
        return carreraDTO;
    }

    public CarreraDTO getById(int id) throws CarreraException {
        //Siempre trabajar con modelos y DTO en conjunto
        //Se trabaja con modelos y se devuelven DTO's
        Optional<Carrera> carrera = ICarreraRepository.findById(id);
        if(carrera.isPresent()){
            return CarreraDTO.builder()
                    .id(carrera.get().getId())
                    .nombreCarrera(carrera.get().getNombreCarrera())
                    .build();

        }

        throw new CarreraException("Carrera no encontrada");

    }
    //GetAll
    public List<CarreraDTO> getAll(){

        List<Carrera> carreras = ICarreraRepository.findAll();
        List<CarreraDTO> carreraDTO = carreras.stream().map(carrera -> CarreraDTO.builder()
                .id(carrera.getId())
                .nombreCarrera(carrera.getNombreCarrera())
                .build()).collect(Collectors.toList());

        return carreraDTO;
    }


    //Update


    public CarreraDTO update(CarreraDTO carreraDTO) throws CarreraException{
        //IR A BUSCAR EL MODELO A LA BASE DE DATOS, LO MODIFICO Y LO GUARDO, RETORNO EL DTO, TENGO QUE VALIDAR SI EXISTE
        //O NO
        Optional<Carrera> carrera = ICarreraRepository.findById(carreraDTO.getId());
        if(carrera.isPresent()){
            Carrera carreraModelo = carrera.get();
            carreraModelo.setNombreCarrera(carreraDTO.getNombreCarrera());
            carreraModelo = ICarreraRepository.save(carreraModelo);
            return CarreraDTO.builder()
                    .id(carreraModelo.getId())
                    .nombreCarrera(carreraModelo.getNombreCarrera())
                    .build();
        }
        throw new CarreraException("Carrera no encontrada");
    }
    //Delete --> Desactivar xd

    public CarreraDTO delete(int id) throws CarreraException{
        Optional<Carrera> carrera = ICarreraRepository.findById(id);
        if(carrera.isPresent()){
            Carrera carreraModelo = carrera.get();
            carreraModelo.setEstado(false);
            carreraModelo = ICarreraRepository.save(carreraModelo);
            return CarreraDTO.builder()
                    .id(carreraModelo.getId())
                    .nombreCarrera(carreraModelo.getNombreCarrera())
                    .build();
        }
        throw new CarreraException("Carrera no encontrada");
    }
}
