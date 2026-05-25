package com.example.demo.repository;


import com.example.demo.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    // Opcional: Para verificar si un correo ya está registrado antes de crear un admin nuevo
    boolean existsByEmail(String email);

}
