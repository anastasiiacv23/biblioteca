package com.example.biblio.controller;

import com.example.biblio.model.ClubLectura;
import com.example.biblio.model.Sala;
import com.example.biblio.service.AlquilerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sala")
public class SalaController  {

    private final AlquilerService service;

    public SalaController(AlquilerService service) {
        this.service = service;
    }

    @GetMapping("/salasExistentes")
    public ResponseEntity<List<Sala>> findAllSalasExistentes() {
        List<Sala> salas = service.obtenerSalasExistentes();
        return ResponseEntity.ok(salas);
    }
}
