package com.ingsoftware.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CarreraException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> carreraNotFoundException(CarreraException carreraException) {
        return new ResponseEntity<String>(carreraException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> userNotFoundException(UserException userException) {
        return new ResponseEntity<String>(userException.getMessage(), HttpStatus.NOT_FOUND /*404*/);
    }

    @ExceptionHandler(JwtResponseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> JWTRENotFoundException(JwtResponseException jwtResponseException) {
        return new ResponseEntity<String>(jwtResponseException.getMessage(), HttpStatus.NOT_FOUND /*404*/);

    }

    @ExceptionHandler(RolDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> RDENotFoundException(RolDoesntExistException rolDoesntExistException) {
        return new ResponseEntity<String>(rolDoesntExistException.getMessage(), HttpStatus.NOT_FOUND /*404*/);

    }



}