package com.example.demo.dto.usuario;

import com.example.demo.model.usuario.RolUsuario;
import jakarta.validation.constraints.*;
import lombok.Data;

//Esto sera lo que llega desde angular para la creacion del usuario
@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "El correo electrónico es obligatorio y no puede estar vacío.")
    @Email(message = "El formato del correo electrónico no es válido.")
    private String email;


    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;


    @NotBlank(message = "El apellido es obligatorio.")
    private String apellido;


    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres para ser segura.")
    private String password;



}
