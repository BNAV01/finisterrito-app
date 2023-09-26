package com.ingsoftware.api.DTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CarreraDTO {

    private int id;

    private String nombreCarrera;

    private List<UserDTO> estudiantes;

}
