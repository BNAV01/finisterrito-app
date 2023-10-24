package com.ingsoftware.api.DTO;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    String email;
    String newPassword;
    String token;
}
