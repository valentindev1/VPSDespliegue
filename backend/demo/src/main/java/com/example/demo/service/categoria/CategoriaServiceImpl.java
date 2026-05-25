package com.example.demo.service.categoria;

import com.example.demo.dto.categoria.CategoriaRequestDTO;
import com.example.demo.dto.categoria.CategoriaResponseDTO;
import com.example.demo.model.articulo.Categoria;
import com.example.demo.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {


    private final CategoriaRepository categoriaRepository;

    // =========================
    // CREAR
    // =========================
    @Override
    public CategoriaResponseDTO crear(CategoriaRequestDTO dto) {

        Categoria categoria = new Categoria();

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        // generar slug automáticamente
        categoria.setSlug(generarSlug(dto.getNombre()));

        Categoria guardada = categoriaRepository.save(categoria);

        return mapToDTO(guardada);
    }

    // =========================
    // LISTAR
    // =========================
    @Override
    public List<CategoriaResponseDTO> listar() {

        return categoriaRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // =========================
    // OBTENER POR ID
    // =========================
    @Override
    public CategoriaResponseDTO obtenerPorId(Long id) {

        Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        return mapToDTO(categoria);
    }

    // =========================
    // ELIMINAR
    // =========================
    @Override
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    // =========================
    // MAP TO DTO
    // =========================
    private CategoriaResponseDTO mapToDTO(Categoria categoria) {

        CategoriaResponseDTO dto = new CategoriaResponseDTO();

        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setSlug(categoria.getSlug());

        return dto;
    }

    // =========================
    // SLUG
    // =========================
    private String generarSlug(String nombre) {
        return nombre.toLowerCase().replaceAll("[^a-z0-9\\s]", "");


    }
}
