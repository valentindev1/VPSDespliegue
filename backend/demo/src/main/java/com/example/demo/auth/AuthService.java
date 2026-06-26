package com.example.demo.auth;


import com.example.demo.dto.usuario.UsuarioRequestDTO;
import com.example.demo.model.usuario.RolUsuario;
import com.example.demo.model.usuario.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    //  REGISTER
    public String register(UsuarioRequestDTO requestDTO) {

        if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(requestDTO.getEmail());
        usuario.setNombre(requestDTO.getNombre());
        usuario.setApellido(requestDTO.getApellido());

        //  CRÍTICO
        usuario.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        //  Seguridad: NO dejar que el cliente decida rol
        usuario.setRol(RolUsuario.USER);

        usuarioRepository.save(usuario);

        return jwtService.generateToken(usuario);
    }

    //  LOGIN
    public String login(String email, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return jwtService.generateToken(usuario);
    }

}
