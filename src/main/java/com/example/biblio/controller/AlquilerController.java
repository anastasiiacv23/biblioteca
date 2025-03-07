package com.example.biblio.controller;

import com.example.biblio.model.Libro;
import com.example.biblio.service.AlquilerService;
import com.example.biblio.service.LibroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alquiler")
public class AlquilerController {
    private final AlquilerService service;

    public AlquilerController(AlquilerService service) {
        this.service = service;
    }

    @PostMapping("/alquilar")
    public ResponseEntity<Libro> alquilarLibro(@RequestParam Long libroId,
                                               @RequestParam Long userId) {

        Libro libro = service.alquilarLibro(libroId, userId);
        if (libro != null) {
            return ResponseEntity.ok(libro);
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/devolver")
    public ResponseEntity<Libro> devolverLibro(@RequestParam Long libroId,
                                               @RequestParam Long userId) {

        Libro libro = service.devolverLibro(libroId, userId);
        if (libro != null) {
            return ResponseEntity.ok(libro);
        }
        return ResponseEntity.badRequest().build();
    }
}
