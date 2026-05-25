package com.example.demo.service.categoria;

import com.example.demo.dto.categoria.CategoriaRequestDTO;
import com.example.demo.dto.categoria.CategoriaResponseDTO;
import com.example.demo.model.articulo.Categoria;

import java.util.List;


public interface CategoriaService {

    CategoriaResponseDTO crear(CategoriaRequestDTO dto);

    List<CategoriaResponseDTO> listar();

    CategoriaResponseDTO obtenerPorId(Long id);

    void eliminar(Long id);
}
