package com.example.demo.auth;


import com.example.demo.dto.usuario.UsuarioRequestDTO;
import com.example.demo.model.usuario.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {

        String token = authService.login(
                request.get("email"),
                request.get("password")
        );

        return ResponseEntity.ok(token);
    }

}
