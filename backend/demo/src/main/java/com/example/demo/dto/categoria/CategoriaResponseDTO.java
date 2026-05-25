package com.example.demo.dto.categoria;


import lombok.Data;

@Data
public class CategoriaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String slug;

}
