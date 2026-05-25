package com.example.demo.controller;

import com.example.demo.dto.usuario.UsuarioRequestDTO;
import com.example.demo.dto.usuario.UsuarioResponseDTO;
import com.example.demo.model.usuario.RolUsuario;
import com.example.demo.service.usuario.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {


    private final UserService userService;

    // ✅ SOLO ADMIN puede cambiar roles
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/rol")
    public ResponseEntity<UsuarioResponseDTO> cambiarRol(
            @PathVariable Long id,
            @RequestParam RolUsuario nuevoRol
    ) {
        return ResponseEntity.ok(userService.cambiarRol(id, nuevoRol));
    }

    // ✅ SOLO ADMIN puede listar todos los usuarios
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(userService.obtenerTodosLosUsuarios());
    }

    // ✅ SOLO ADMIN puede ver cualquier usuario
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(userService.obtenerUsuarioPorId(id));
    }

    // ✅ ADMIN o el propio usuario pueden actualizar
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO requestDTO
    ) {
        try {
            UsuarioResponseDTO usuarioActualizado = userService.actualizarUsuario(id, requestDTO);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ SOLO ADMIN puede eliminar usuarios
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        try {
            userService.eliminarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}