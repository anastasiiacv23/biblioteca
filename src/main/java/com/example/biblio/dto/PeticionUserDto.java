package com.example.biblio.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class PeticionUserDto {
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 4, max = 20, message = "El username debe tener entre 4 y 20 caracteres")
    private String username;
    @NotBlank(message = "La contrasea es obligatoria")
    @Size(min = 8, message = "La contrasea debe tener al menos 8 caracteres")
    private String password;
    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}