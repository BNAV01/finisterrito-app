package com.ingsoftware.api.DTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolDTO {
    int id;
    private String name;
}
