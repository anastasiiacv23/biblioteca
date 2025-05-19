package com.example.biblio.controller;

import com.example.biblio.model.Alquiler;
import com.example.biblio.model.Libro;
import com.example.biblio.service.AlquilerService;
import com.example.biblio.service.LibroService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import com.example.biblio.dto.PeticionAlquilerDto;
import com.example.biblio.dto.PeticionUserDto;


@RestController
@RequestMapping("/api/alquiler")
public class AlquilerController {
    private final AlquilerService service;

    public AlquilerController(AlquilerService service) {
        this.service = service;
    }

    /*@PostMapping("/alquilar")
    public ResponseEntity<Libro> alquilarLibro(@RequestParam String libroId,
                                               @RequestParam String userId) {

        Libro libro = service.alquilarLibro(libroId, userId);
        if (libro != null) {
            return ResponseEntity.ok(libro);
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/devolver")
    public ResponseEntity<Libro> devolverLibro(@RequestParam String libroId,
                                               @RequestParam String userId) {

        Libro libro = service.devolverLibro(libroId, userId);
        if (libro != null) {
            return ResponseEntity.ok(libro);
        }
        return ResponseEntity.badRequest().build();
    }

     */


    @PostMapping("/alquiler")
    public ResponseEntity<?> alquilarLibro(@Valid @RequestBody PeticionAlquilerDto peticion) {
        try {
            Alquiler alquiler = service.alquilarLibro(
                    peticion.getLibroId(),
                    peticion.getUserId(),
                    peticion.getFechaRecogida(),
                    peticion.getFechaDevolucion()
            );
            return ResponseEntity.ok(alquiler);
        } catch (Exception e) {
            e.setStackTrace(null);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

        //Para completar:
        @GetMapping("/activos")
        public ResponseEntity<List<Alquiler>> obtenerAlquileresActivos(@RequestParam String userId) {
            List<Alquiler> activos = service.obtenerAlquileresPresentes(userId);
            return ResponseEntity.ok(activos);
        }


    @GetMapping("/futuros")
    public ResponseEntity<List<Alquiler>> obtenerAlquileresFuturos(@RequestParam String userId) {
        List<Alquiler> futuros = service.obtenerAlquileresFuturos(userId);
        return ResponseEntity.ok(futuros);
    }



    @GetMapping("/historial")
    public ResponseEntity<List<Alquiler>> obtenerHistorialAlquiler(@RequestParam String userId) {
        List<Alquiler> historial = service.obtenerAlquileresPasados(userId);
        return ResponseEntity.ok(historial);
    }



    @GetMapping //este no tiene endpoint adicional, sera un GET a /api/alquiler?libroId=3
    public ResponseEntity<List<Alquiler>> obtenerPorLibro(@RequestParam String libroId) {
        List<Alquiler> historial = service.obtenerAlquileresDelLibro(libroId);
        return ResponseEntity.ok(historial);
    }



    //borrar la inscripcion de usuario logeado


    // 5. Eliminar alquiler
//    @DeleteMapping("/{id}")
  //  public ResponseEntity<Void> eliminarAlquiler(@PathVariable String id) {
    //    boolean eliminado = service.eliminarAlquiler(id);
      //  if (eliminado) {
        //    return ResponseEntity.noContent().build();
   //     }
    //    return ResponseEntity.notFound().build();
   // }


    //suspender club de lectura por irganizador
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAlquiler(@PathVariable String id) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            //comprobar que idUser de club es igual al persona iniciada
            //si tiene derecho a modificar entonces modificamos:
            boolean eliminado = service.eliminarAlquiler(username, id);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            }
            else{
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    

}
