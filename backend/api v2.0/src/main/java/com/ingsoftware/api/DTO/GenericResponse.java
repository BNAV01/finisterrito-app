package com.ingsoftware.api.DTO;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GenericResponse {
    private Object data;
    private Object error;
}