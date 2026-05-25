package com.example.demo.dto.articulo;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
public class ArticuloRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;

    @Size(max = 500, message = "El resumen no puede superar los 500 caracteres")
    private String resumen;

    @NotNull(message = "El estado de publicación es obligatorio")
    private Boolean publicada;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;


    @Size(max = 10, message = "Máximo 10 imágenes")
    private List<MultipartFile> imagenes;

}
