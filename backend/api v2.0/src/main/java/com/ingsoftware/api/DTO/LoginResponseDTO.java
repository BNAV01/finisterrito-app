package com.ingsoftware.api.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    String authToken;
    String refreshToken;
}
