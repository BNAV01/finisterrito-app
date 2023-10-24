package com.ingsoftware.api.controllers;


import com.ingsoftware.api.DTO.CarreraDTO;
import com.ingsoftware.api.exceptions.CarreraException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/carrera")

public class CarreraController {

    private final com.ingsoftware.api.services.CarreraService carreraService;



    @GetMapping("") //llego a http://localhost/carrera
    public ResponseEntity index(){
        return new ResponseEntity("Hola mundo desde carrera", HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity postMapping(@RequestBody CarreraDTO carreraDTO) throws CarreraException{
        return new ResponseEntity(carreraService.create(carreraDTO), HttpStatus.OK);
    }



}