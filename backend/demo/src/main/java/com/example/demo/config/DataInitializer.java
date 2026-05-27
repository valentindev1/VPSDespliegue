package com.example.demo.config;

import com.example.demo.model.usuario.RolUsuario;
import com.example.demo.model.usuario.Usuario;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    //Variables de entorno

    @Value("${ADMIN_EMAIL:admin@demo.com}")
    private String adminEmail;

    @Value("${ADMIN_NAME:admin}")
    private String adminName;

    @Value("${ADMIN_LASTNAME:admin}")
    private String adminLastname;

    @Value("${ADMIN_PASS:admin123456}")
    private String adminPass;


    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository) {
        return args -> {
            if (repository.findAll().isEmpty()) {

                Usuario admin = new Usuario();
                admin.setEmail(adminEmail);
                admin.setNombre(adminName);
                admin.setApellido(adminLastname);
                admin.setPassword(passwordEncoder.encode(adminPass));
                admin.setRol(RolUsuario.ADMIN);

                repository.save(admin);

                System.out.println("Administrador inicial creado desde variables de entorno.");
            }
        };
    }
}
