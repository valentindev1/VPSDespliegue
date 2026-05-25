package com.example.demo.service.usuario;


import com.example.demo.dto.usuario.UsuarioRequestDTO;
import com.example.demo.dto.usuario.UsuarioResponseDTO;
import com.example.demo.model.usuario.RolUsuario;
import com.example.demo.model.usuario.Usuario;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UserService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ SOLO ADMIN puede ver todos los usuarios
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    // ✅ SOLO ADMIN puede consultar cualquier usuario
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return mapearAResponseDTO(usuario);
    }

    // ✅ ADMIN o el propio usuario pueden actualizar
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Override
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO usuarioRequestDTO) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // 🔐 Validar email único si cambia
        if (!usuario.getEmail().equals(usuarioRequestDTO.getEmail()) &&
                usuarioRepository.existsByEmail(usuarioRequestDTO.getEmail())) {
            throw new RuntimeException("El correo ya está en uso.");
        }

        usuario.setEmail(usuarioRequestDTO.getEmail());
        usuario.setNombre(usuarioRequestDTO.getNombre());
        usuario.setApellido(usuarioRequestDTO.getApellido());

        // 🔐 Encriptar password si se actualiza
        if (usuarioRequestDTO.getPassword() != null && !usuarioRequestDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioRequestDTO.getPassword()));
        }

        // ❌ NO se permite modificar el rol aquí

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return mapearAResponseDTO(usuarioActualizado);
    }

    // ✅ SOLO ADMIN puede cambiar roles
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO cambiarRol(Long id, RolUsuario nuevoRol) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setRol(nuevoRol);
        usuarioRepository.save(usuario);

        return mapearAResponseDTO(usuario);
    }

    // ✅ SOLO ADMIN puede eliminar usuarios
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void eliminarUsuario(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Error: No se puede eliminar. Usuario no encontrado.");
        }

        usuarioRepository.deleteById(id);
    }

    // ✅ Mapper seguro
    private UsuarioResponseDTO mapearAResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        return dto;
    }
}
