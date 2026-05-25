package com.example.demo.dto.categoria;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;
}
