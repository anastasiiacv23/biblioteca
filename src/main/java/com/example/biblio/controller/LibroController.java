package com.example.biblio.controller;

import com.example.biblio.model.Libro;
import com.example.biblio.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
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
    @Operation(summary = "Listar libros",

            description = "Devuelve una lista paginada de libros.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada exitosamente")
    })

    @GetMapping  //http://localhost:8080/api/libros?page=9&size=20
    public ResponseEntity<?> obtenerLibros(@Parameter(description = "Numero de pagina (comienza en 0)", example = "0")
                                               @RequestParam(defaultValue = "0") int page,
                                           @Parameter(description = "Cantidad de elementos por pagina", example = "20")
                                           @RequestParam(defaultValue = "20") int size) {

        Page<Libro> libros = service.listarLibros(page, size);
        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content
        }
        return ResponseEntity.ok(libros); // Devuelve 200 OK con la lista de libros
    }

    // 2. Buscar uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibro(@PathVariable String id) {
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
    public ResponseEntity<Libro> actualizarLibro(@PathVariable String id,
                                                 @RequestBody Libro detalles) {
        Libro actualizado = service.actualizarLibro(id, detalles);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }
    // 5. Eliminar Libro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable String id) {
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

    @GetMapping("/searchTitulo")
    public List<Libro> buscarLibros(
            @RequestParam(required = false) String titulo)
             {
        List<Libro> resultados = service.buscarPorTitulo(titulo);
        return service.buscarPorTitulo(titulo);
        //return ResponseEntity.ok(resultados);
    }





}
