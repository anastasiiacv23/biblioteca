package com.example.biblio.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "iscripcion")
public class Inscripcion {
    @Id
    private String id;
    private String usuarioInscritoId;
    private ClubLectura clubLectura;

    public Inscripcion() {
    }

    public Inscripcion(String inscripcionId, String userId, ClubLectura clubLectura) {
        this.id = inscripcionId;
        this.usuarioInscritoId = userId;
        this.clubLectura = clubLectura;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioInscritoId() {
        return usuarioInscritoId;
    }

    public void setUsuarioInscritoId(String usuarioInscritoId) {
        this.usuarioInscritoId = usuarioInscritoId;
    }

    public ClubLectura getClubLectura() {
        return clubLectura;
    }

    public void setClubLectura(ClubLectura clubLectura) {
        this.clubLectura = clubLectura;
    }
}
