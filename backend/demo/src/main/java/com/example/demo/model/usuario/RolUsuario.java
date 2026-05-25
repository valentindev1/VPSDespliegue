package com.example.demo.model.usuario;

public enum RolUsuario {

    ADMIN,      // Acceso total al dashboard (tú)
    EDITOR,     // Solo puede crear/editar noticias de obras, no borrar ni crear usuarios
    USER
}
