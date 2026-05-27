package com.example.demo.dto.articulo;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ArticuloResponseDTO {

    private Long id;

    private String titulo;

    private String contenido;

    private String resumen;

    private String slug;

    private Long categoriaId;

    private String categoria;

    private String autor;

    private Boolean publicada;

    private LocalDate fechaPublicacion;

    private List<String> imagenes;
}
