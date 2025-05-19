package com.example.biblio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class PeticionClubLecturaDto extends PeticionAlquilerDto{
    @NotNull(message = "El ID de la sala es obligatorio")
    private String salaId;
    private String observaciones;
    @NotNull(message = "Es obligatorio elegir el tiempo")
    private String franjaHoraria;

    private String id;
// Getters y Setters

    @Override
    public String getLibroId() {
        return super.getLibroId();
    }

    @Override
    public String getUserId() {
        return super.getUserId();
    }

    @Override
    public LocalDateTime getFechaRecogida() {
        return super.getFechaRecogida();
    }

    @Override
    public LocalDateTime getFechaDevolucion() {
        return super.getFechaDevolucion();
    }

    public @NotNull(message = "El ID de la sala es obligatorio") String getSalaId() {
        return salaId;
    }

    public void setSalaId(@NotNull(message = "El ID de la sala es obligatorio") String salaId) {
        this.salaId = salaId;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public @NotNull(message = "Es obligatorio elegir el tiempo") String getFranjaHoraria() {
        return franjaHoraria;
    }

    public void setFranjaHoraria(@NotNull(message = "Es obligatorio elegir el tiempo") String franjaHoraria) {
        this.franjaHoraria = franjaHoraria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
