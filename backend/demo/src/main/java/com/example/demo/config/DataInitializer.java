package com.example.demo.config;

import com.example.demo.model.usuario.RolUsuario;
import com.example.demo.model.usuario.Usuario;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Esto le dice a Spring que esta clase contiene configuraciones al arrancar
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;


    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository) {
        return args -> {
            // Verificamos si la base de datos está vacía para evitar duplicados
            if (repository.findAll().isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@hormigonesdelsur.com");
                admin.setNombre("admin");
                admin.setApellido("admin");
                admin.setPassword(passwordEncoder.encode("admin123456"));
                admin.setRol(RolUsuario.ADMIN);
                repository.save(admin);
                System.out.println("Administrador inicial creado exitosamente.");
            }
        };
    }
}