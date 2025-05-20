package com.example.biblio.dto;

import com.example.biblio.model.ClubLectura;

import java.time.LocalDate;

public class RespuestaDiaOcupadoDto {

    ClubLectura clubLectura;
    LocalDate fecha;

    public RespuestaDiaOcupadoDto() {

    }

    public ClubLectura getClubLectura() {
        return clubLectura;
    }

    public void setClubLectura(ClubLectura clubLectura) {
        this.clubLectura = clubLectura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
