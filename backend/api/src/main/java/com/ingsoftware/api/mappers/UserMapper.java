package com.ingsoftware.api.mappers;


import com.ingsoftware.api.DTO.RolDTO;
import com.ingsoftware.api.DTO.UserDTO;
import com.ingsoftware.api.models.Carrera;
import com.ingsoftware.api.models.Carrera;
import com.ingsoftware.api.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserDTO createDTO(User user) {
        return UserDTO.builder()
                .userId(user.getId())
                .rut(user.getRut())
                .lastName(user.getLastname())
                .middleName(user.getMiddlename())
                .email(user.getEmail())
                .carreraDTO(
                        CarreraMapper.
                                createDTO(user.getIdcareer())
                )
                .careerEntry(user.getCareerEntry())
                .phoneNumber(user.getPhoneNumber())
                .isActive(user.isActive())
                .firstName(user.getFirstname())
                .isLockout(user.isLockout())
                .isActive(user.isActive())
                .passwordAttempt(user.getPasswordAttempt())
                .rol(
                        RolMapper.
                                createDTO(user.getRol())
                )
                .build();
    }
}
