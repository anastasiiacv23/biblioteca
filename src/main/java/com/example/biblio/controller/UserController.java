package com.example.biblio.controller;
import com.example.biblio.dto.PeticionUserDto;
import com.example.biblio.security.JwtUtil;
import com.example.biblio.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody PeticionUserDto peticionUserDto)
    {
        try {
            userService.register(peticionUserDto.getUsername(), peticionUserDto.getPassword
                ());
            return ResponseEntity.ok(Map.of("message", "Usuario registrado con éxito"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody PeticionUserDto peticionUserDto) {
        var user = userService.login(peticionUserDto.getUsername(), peticionUserDto.
                getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invlidas");
        }
        String token = jwtUtil.generateToken(userService.loadUserByUsername(user.getUsername()), user.getId());
        return ResponseEntity.ok(new TokenResponse(token));
    }
    static class TokenResponse {
        private final String token;
        public TokenResponse(String token) { this.token = token; }
        public String getToken() { return token; }
    }
}