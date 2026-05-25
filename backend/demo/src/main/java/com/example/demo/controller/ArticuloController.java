package com.example.demo.controller;

import com.example.demo.dto.articulo.ArticuloRequestDTO;
import com.example.demo.dto.articulo.ArticuloResponseDTO;
import com.example.demo.service.articulo.ArticuloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articulo")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ArticuloController {

    private final ArticuloService articuloService;

    // =========================
    // ✅ CREAR (multipart)
    // =========================
    @PostMapping(value = "/crear", consumes = "multipart/form-data")
    public ArticuloResponseDTO crear(@ModelAttribute @Valid ArticuloRequestDTO dto) {
        return articuloService.crear(dto);
    }

    // =========================
    // ✅ LISTAR
    // =========================
    @GetMapping("/listar")
    public List<ArticuloResponseDTO> listar() {
        return articuloService.listar();
    }

    // =========================
    // ✅ OBTENER POR ID
    // =========================
    @GetMapping("/listar/{id}")
    public ArticuloResponseDTO obtenerPorId(@PathVariable Long id) {
        return articuloService.obtenerPorId(id);
    }

    // =========================
    // ✅ OBTENER POR SLUG
    // =========================
    @GetMapping("/listar/slug/{slug}")
    public ArticuloResponseDTO obtenerPorSlug(@PathVariable String slug) {
        return articuloService.obtenerPorSlug(slug);
    }

    // =========================
    // ✅ PRO: CATEGORIA + SLUG
    // =========================
    @GetMapping("/{categoriaSlug}/{articuloSlug}")
    public ArticuloResponseDTO obtenerPorCategoriaYSlug(
            @PathVariable String categoriaSlug,
            @PathVariable String articuloSlug) {

        return articuloService.obtenerPorCategoriaYSlug(categoriaSlug, articuloSlug);
    }

    // =========================
    // ✅ ACTUALIZAR (multipart también 🔥)
    // =========================
    @PutMapping(value = "/actualizar/{id}", consumes = "multipart/form-data")
    public ArticuloResponseDTO actualizar(
            @PathVariable Long id,
            @ModelAttribute @Valid ArticuloRequestDTO dto) {

        return articuloService.actualizar(id, dto);
    }

    // =========================
    // ✅ ELIMINAR
    // =========================
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        articuloService.eliminar(id);
    }
}
