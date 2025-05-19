package com.example.biblio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PeticionAlquilerDto {
    @NotNull(message = "El ID del libro es obligatorio")
    private String libroId;
    @NotNull(message = "El ID del usuario es obligatorio")
    private String userId;
    @NotNull(message = "La fecha de recogida es obligatoria")
    private LocalDateTime fechaRecogida;
    @NotNull(message = "La fecha de devolucin es obligatoria")
    private LocalDateTime fechaDevolucion;
// Getters y Setters

    public PeticionAlquilerDto() {

    }
    public String getLibroId() {
        return libroId;
    }
    public String getUserId() {
        return userId;
    }
    public LocalDateTime getFechaRecogida() {
        return fechaRecogida;
    }
    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

}
