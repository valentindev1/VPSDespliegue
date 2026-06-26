package com.example.demo.controller;


import com.example.demo.dto.categoria.CategoriaRequestDTO;
import com.example.demo.dto.categoria.CategoriaResponseDTO;
import com.example.demo.service.categoria.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categoria")

@RequiredArgsConstructor

public class CategoriaController {

    private final CategoriaService categoriaService;

    // =========================
    // CREAR
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public CategoriaResponseDTO crear(@Valid @RequestBody CategoriaRequestDTO dto) {
        return categoriaService.crear(dto);
    }

    // =========================
    // LISTAR
    // =========================

    @GetMapping("/listar")
    public List<CategoriaResponseDTO> listar() {
        return categoriaService.listar();
    }

    // =========================
    // OBTENER POR ID
    // =========================

    @GetMapping("/{id}")
    public CategoriaResponseDTO obtenerPorId(@PathVariable Long id) {
        return categoriaService.obtenerPorId(id);
    }

    // =========================
    // ELIMINAR
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
    }


}


