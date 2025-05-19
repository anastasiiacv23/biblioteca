package com.example.biblio.controller;

import com.example.biblio.dto.*;
import com.example.biblio.model.*;
import com.example.biblio.service.AlquilerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clubLectura")
public class ClubLecturaController {
    private final AlquilerService service;
        public ClubLecturaController(AlquilerService service) {
            this.service = service;
        }
        //para ver los clubs de lectura que ahora están presentes en afisha, no hace falt6a registrarse
    @GetMapping("/anunciados")
    public ResponseEntity<List<ClubLectura>> findByFechaRecogidaAfter() {
        List<ClubLectura> proximos = service.obtenerProximosClubSLEctura();
        return ResponseEntity.ok(proximos);
    }

    //para ver inscripciones con el acceso a la informacion de todos clubs donde  está apuntado el usuario logeado
    @GetMapping("/porUsuarioLogeado")
    public ResponseEntity<List<Inscripcion>> findByClubLecturaFechaRecogidaAfterAndUsuarioInscritoId(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Inscripcion> proximosDeUsuarioLogeado = service.obtenerProximosClubSLEcturaDEUsuarioLogeado(username);
        return ResponseEntity.ok(proximosDeUsuarioLogeado);
    }

    @GetMapping("/porOrganizador")
    public ResponseEntity<List<ClubLectura>> findByFechaRecogidaAfterAndUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ClubLectura> proximosDeOrganizador = service.obtenerProximosClubSLEcturaDEOrganizador(username);
        return ResponseEntity.ok(proximosDeOrganizador);
    }

    //para recoger los datos de disponobilidad
    @GetMapping("/empezarCrear")
    public ResponseEntity <List <RespuestaSalaOcupadaDto>>  obtenerFranjasHorarias(@RequestParam LocalDate dia) {
        List<RespuestaSalaOcupadaDto> salasocupados = service.obtenerFranjasHorarias(dia);
        return ResponseEntity.ok(salasocupados);
    }
    @GetMapping("/salasOcupadasMes")
    public ResponseEntity <HashMap<LocalDate, HashMap<String, RespuestaSalaOcupadaDto>>>  obtenerFranjasHorariasMes(@RequestParam LocalDate mes) {
        List<RespuestaSalaOcupadaDto> salasOcupadas = service.obtenerFranjasHorariasMes(mes);
        HashMap<LocalDate, HashMap<String, RespuestaSalaOcupadaDto>> salasMap = new HashMap<>();
        System.out.println("empezando iteración...: " + salasMap.size());

        for (RespuestaSalaOcupadaDto sala : salasOcupadas) {
            // Log para ver el estado de cada sala antes de la lógica de la comparación
            System.out.println("Iterando sala: " + sala.toString());

            if (sala.getSalaId() != null && sala.getFecha() != null) {
                // Log para verificar las condiciones del if
                System.out.println("sala.getSalaId(): " + sala.getSalaId() + " | sala.getFecha(): " + sala.getFecha());

                if (!salasMap.containsKey(sala.getFecha())) {
                    // Log cuando se añade una nueva fecha al mapa
                    System.out.println("No contiene la fecha. Añadiendo nueva entrada para la fecha: " + sala.getFecha());
                    salasMap.put(sala.getFecha(), new HashMap<>());
                } else {
                    // Log cuando ya existe la fecha y no se agrega una nueva
                    System.out.println("Ya existe la fecha en el mapa: " + sala.getFecha());
                }

                // Log para verificar la inserción del objeto en el HashMap
                System.out.println("Añadiendo sala con salaId: " + sala.getSalaId() + " a la fecha: " + sala.getFecha());
                salasMap.get(sala.getFecha()).put(sala.getSalaId(), sala);
            } else {
                // Log cuando alguna propiedad (SalaId o Fecha) es nula
                System.out.println("NULOS - SalaId o Fecha nulos: " + sala.toString());
            }
        }

// Log final para verificar el contenido completo del mapa
        System.out.println("Contenido final del mapa salasMap: " + salasMap.toString());

        return ResponseEntity.ok(salasMap);
    }

    @GetMapping("/disponibilidadLibroMes")
    public ResponseEntity <List <LocalDate>>  obtenerDisponibilidadLibroMes(@RequestParam String libroId, @RequestParam LocalDate mes) {
        ArrayList<LocalDate> diasOcupados = service.obtenerDisponibilidadMes(libroId, mes);
        return ResponseEntity.ok(diasOcupados);
    }

    @PostMapping("/registarClub")
    public  ResponseEntity<?> crearClub (@Valid @RequestBody PeticionClubLecturaDto peticion) {
        try {

            ClubLectura clubLectura = service.crearClub(
                    peticion.getLibroId(),
                    peticion.getUserId(),
                    peticion.getFechaRecogida(),
                    peticion.getFechaDevolucion(),
                    peticion.getSalaId(),
                    peticion.getObservaciones(),
                    peticion.getFranjaHoraria()

            );
            return ResponseEntity.ok(clubLectura);
        } catch (Exception e) {
            System.out.println("ERROR al crear club: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    @PostMapping("/apuntarse")
    public  ResponseEntity<?> apuntarse (@RequestBody PeticionClubLecturaDto club) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Inscripcion inscripcion = service.apuntarse(
                    club.getId(),
                    username
            );
            System.out.println("es un print desde controller"+username);
            return ResponseEntity.ok(inscripcion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

//borrar la inscripcion de usuario logeado
    @DeleteMapping("/{idInscripcion}")
    public  ResponseEntity<?> eliminar(@PathVariable("idInscripcion") String idInscripcion) {
                boolean eliminadoInscrip=service.borrarInscripcion(
                        idInscripcion);
                if (eliminadoInscrip) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.notFound().build();
    }


    //suspender el club como organizador


    //modificar club de lectura DERECHO SOLO PARA organizador
    @PutMapping("/modificarClub/{idClub}")
    public  ResponseEntity<?> actualizarClub (@Valid @RequestBody PeticionClubLecturaDto peticion, @PathVariable("idClub") String idClub) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            //comprobar que idUser de club es igual al persona iniciada
            //si tiene derecho a modificar entonces modificamos:
            ClubLectura clubActualizado = service.actualizarClub(
                    peticion.getLibroId(),
                    peticion.getUserId(),
                    peticion.getFechaRecogida(),
                    peticion.getFechaDevolucion(),
                    peticion.getSalaId(),
                    peticion.getObservaciones(),
                    peticion.getFranjaHoraria(),
                    username,
                    idClub


            );
            return ResponseEntity.ok(clubActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping //este no tiene endpoint adicional, sera un GET a /api/alquiler?libroId=3
    public ResponseEntity<List<Alquiler>> obtenerPorLibro(@RequestParam String libroId) {
        List<Alquiler> historial = service.obtenerAlquileresDelLibro(libroId);
        return ResponseEntity.ok(historial);
    }

    //El organizador del club puede editar  la nota

    @PutMapping("/modificarClubNota/{idClub}/")
    public  ResponseEntity<?> actualizarClubObservaciones (@PathVariable("idClub") String idClub, @Valid @RequestBody ActualizarNotaDto peticion) {
        // lógica para buscar la nota por id y actualizarla
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            //comprobar que idUser de club es igual al persona iniciada
            //si tiene derecho a modificar entonces modificamos:
            ClubLectura clubActualizado = service.actualizarClubObservaciones(
                    peticion.getObservaciones(),
                    username,
                    idClub
            );
            return ResponseEntity.ok(clubActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }






    //suspender club de lectura por irganizador
    @DeleteMapping("/suspender/{idClub}")
    public ResponseEntity<?> suspenderClub(@PathVariable String idClub) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            //comprobar que idUser de club es igual al persona iniciada
            //si tiene derecho a modificar entonces modificamos:
            boolean clubSespendido = service.suspenderClub(
                    username,
                    idClub
            );
            if (clubSespendido) {
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
