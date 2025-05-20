package com.example.biblio.dto;

import com.example.biblio.model.ClubLectura;

import java.time.LocalDate;
import java.util.ArrayList;

public class  RespuestaSalaOcupadaDto {

    ArrayList<ClubLectura> clubesLectura;
    String salaId;
    LocalDate fecha;
    public RespuestaSalaOcupadaDto(ArrayList<ClubLectura> clubesLectura, String salaId) {
        this.clubesLectura = clubesLectura;
        this.salaId = salaId;
    }

    public RespuestaSalaOcupadaDto() {
        this.clubesLectura  = new ArrayList<>();
    }

    public void addClubLectura(ClubLectura clubLectura) {
        this.clubesLectura.add(clubLectura);
    }



    public String getSalaId() {
        return salaId;
    }

    public void setSalaId(String salaId) {
        this.salaId = salaId;
    }

    public ArrayList<ClubLectura> getClubesLectura() {
        return clubesLectura;
    }

    public void setClubesLectura(ArrayList<ClubLectura> clubesLectura) {
        this.clubesLectura = clubesLectura;
    }
// Getters y Setters


    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "RespuestaSalaOcupadaDto{" +
                "clubesLectura=" + clubesLectura +
                ", salaId='" + salaId + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
