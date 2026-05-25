package com.example.demo.dto.usuario;

import com.example.demo.model.usuario.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioResponseDTO {


    private Long id;

    private String nombre;

    private String apellido;

    private String email;

    private RolUsuario rol;

    private LocalDateTime fechaCreacion;

}
