package com.ingsoftware.api.controllers;


import com.ingsoftware.api.DTO.GenericResponse;
import com.ingsoftware.api.DTO.LoginPayload;
import com.ingsoftware.api.DTO.SignUpPayload;
import com.ingsoftware.api.DTO.UserDTO;
import com.ingsoftware.api.exceptions.JwtResponseException;
import com.ingsoftware.api.exceptions.RolDoesntExistException;
import com.ingsoftware.api.exceptions.UserException;
import com.ingsoftware.api.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
public class AuthenticationController {


    @Autowired
    private AuthenticationService _authenticationService;

    public AuthenticationController() {
    }

    @PostMapping("login")
    public ResponseEntity<GenericResponse> DoLogIn(@RequestBody LoginPayload loginPayload) throws IOException {
        var response = _authenticationService.DoLogIn(loginPayload);
        return new ResponseEntity(response, response.getData().equals("Bad credentials") ? HttpStatus.UNAUTHORIZED : HttpStatus.OK);
    }

    @PostMapping("singUp")
    public ResponseEntity DoSingUp(@RequestBody SignUpPayload signUpPayload) throws UserException, RolDoesntExistException {
        return ResponseEntity.ok(_authenticationService.DoSignUp(signUpPayload));
    }

/*    @PostMapping("changePasswordStepOne")
    public ResponseEntity<GenericResponse> ChangePasswordStepOne(@RequestBody ChangePasswordDTO payload) {
        return ResponseEntity.ok(_authenticationService.changePasswordStepOne(payload));
    }

    @PostMapping("changePasswordStepTwo")
    public ResponseEntity<GenericResponse> ChangePasswordStepTwo(@RequestBody ChangePasswordDTO payload) {
        return ResponseEntity.ok(_authenticationService.changePasswordStepTwo(payload));
    }

    @PostMapping("changePasswordStepThree")
    public ResponseEntity<GenericResponse> ChangePasswordStepThree(@RequestBody ChangePasswordDTO payload) {
        return ResponseEntity.ok(_authenticationService.changePasswordStepThree(payload));
    }*/

    @PostMapping("refreshToken")
    public ResponseEntity<GenericResponse> RefreshToken(HttpServletRequest request) throws JwtResponseException, IOException {
        return ResponseEntity.ok(_authenticationService.RefreshToken(request));
    }
    @GetMapping("checkToken")
    public ResponseEntity<Boolean> ValidateToken(@RequestHeader("Authorization") String authorizationToken, @RequestHeader("RefreshToken") String refreshToken) throws JwtResponseException {
        return new ResponseEntity<>(_authenticationService.ValidateToken(authorizationToken, refreshToken), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<GenericResponse> getUserInfo(@PathVariable int userId) throws UserException {
        return ResponseEntity.ok(_authenticationService.getUserInfo(userId));
    }


    @GetMapping("allUsers")
    public ResponseEntity getAll() {
        return new ResponseEntity(_authenticationService.getAllUsers(), HttpStatus.OK);
    }
    @GetMapping("allRoles")
    public ResponseEntity getAllRoles() {
        return new ResponseEntity(_authenticationService.getAllRoles(), HttpStatus.OK);
    }

    @PutMapping("updateUser")
    public ResponseEntity updateUser(@RequestBody UserDTO payload) {
        return new ResponseEntity(_authenticationService.updateUser(payload), HttpStatus.OK);
    }

    @GetMapping("health")
    public ResponseEntity.BodyBuilder HealthCheck() {
        return ResponseEntity.ok();
    }

    @GetMapping("userByEmail/{email}")
    public ResponseEntity<GenericResponse> getByEmail(@PathVariable String email) throws UserException {
        return ResponseEntity.ok(_authenticationService.getByEmail(email));
    }

}
