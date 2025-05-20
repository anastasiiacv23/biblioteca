package com.example.biblio.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "alquileres")
public class ClubLectura extends Alquiler {

    private String salaId;
    private String observaciones;
    private FranjaHoraria franjaHoraria;

    public ClubLectura() {
        //franjaHoraria = FranjaHoraria.MANANA;
    }

    public ClubLectura(String userId, Libro libro, LocalDateTime fechaRecogida, LocalDateTime fechaDevolucion, String salaId, String observaciones, FranjaHoraria franjaHoraria) {
        super(userId, libro, fechaRecogida, fechaDevolucion);
        this.salaId = salaId;
        this.observaciones = observaciones;
        this.franjaHoraria = franjaHoraria;
        //this.franjaHoraria = FranjaHoraria.valueOf(franjaHoraria);
        //this.franjaHoraria.name()
    }

    public String getSalaId() {
        return salaId;
    }

    public void setSalaId(String salaId) {
        this.salaId = salaId;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public FranjaHoraria getFranjaHoraria() {
        return franjaHoraria;
    }

    public void setFranjaHoraria(FranjaHoraria franjaHoraria) {
        this.franjaHoraria = franjaHoraria;
    }

    @Override
    public String toString() {
        return "ClubLectura{" +
                "salaId='" + salaId + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", franjaHoraria=" + franjaHoraria +
                '}';
    }
}
