package com.example.biblio.controller;

import com.example.biblio.model.Libro;
import com.example.biblio.service.LibroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/libros")

public class LibroController {

    private final LibroService service;
    public LibroController(LibroService service) {
        this.service = service;
    }
    // 1. Listar todos
    @GetMapping
    public List<Libro> obtenerLibros() {
        return service.listarLibros();
    }
    // 2. Buscar uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibro(@PathVariable Long id) {
        Libro libro = service.buscarPorId(id);
        if (libro != null) {
            return ResponseEntity.ok(libro);
        }
        return ResponseEntity.notFound().build();
    }
    // 3. Crear Libro
    @PostMapping
    public ResponseEntity<Libro> crearLibro(@RequestBody Libro nuevo) {
        Libro creado = service.crearLibro(nuevo);
        return ResponseEntity.ok(creado);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id,
                                                 @RequestBody Libro detalles) {
        Libro actualizado = service.actualizarLibro(id, detalles);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }
    // 5. Eliminar Libro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        boolean eliminado = service.eliminarLibro(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public List<Libro> buscarLibros(
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) Integer anio) {
        List<Libro> resultados = service.buscarPorAutorYAnio(autor, anio);
        return service.buscarPorAutorYAnio(autor, anio);
        //return ResponseEntity.ok(resultados);
    }
}
