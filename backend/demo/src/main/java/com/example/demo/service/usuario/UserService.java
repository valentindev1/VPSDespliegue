package com.example.demo.service.usuario;

import com.example.demo.dto.usuario.UsuarioRequestDTO;
import com.example.demo.dto.usuario.UsuarioResponseDTO;
import com.example.demo.model.usuario.RolUsuario;

import java.util.List;

public interface UserService {



    List<UsuarioResponseDTO> obtenerTodosLosUsuarios();

    UsuarioResponseDTO obtenerUsuarioPorId(Long id);


    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO requestDTO);
    UsuarioResponseDTO cambiarRol(Long id, RolUsuario nuevoRol);

    void eliminarUsuario(Long id);
}
