package com.example.demo.service.articulo;


import com.example.demo.dto.articulo.ArticuloRequestDTO;
import com.example.demo.dto.articulo.ArticuloResponseDTO;

import java.util.List;

public interface ArticuloService {

    // Crear un artículo
    ArticuloResponseDTO crear(ArticuloRequestDTO dto);

    // Listar todos los artículos
    List<ArticuloResponseDTO> listar();

    // Obtener por ID
    ArticuloResponseDTO obtenerPorId(Long id);

    // Obtener por slug (clave para frontend)
    ArticuloResponseDTO obtenerPorSlug(String slug);


    ArticuloResponseDTO obtenerPorCategoriaYSlug(String categoriaSlug, String articuloSlug);


    // Actualizar artículo
    ArticuloResponseDTO actualizar(Long id, ArticuloRequestDTO dto);

    // Eliminar artículo
    void eliminar(Long id);
}
