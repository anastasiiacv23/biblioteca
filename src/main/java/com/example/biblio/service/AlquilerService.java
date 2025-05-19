package com.example.biblio.service;

//import com.example.biblio.model.Alquiler;
import com.example.biblio.dto.RespuestaDiaOcupadoDto;
import com.example.biblio.dto.RespuestaSalaOcupadaDto;
import com.example.biblio.model.*;

import com.example.biblio.repository.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlquilerService {
    private final AlquilerRepository alquilerRepository;
    private final UserRepository userRepository;
    private final LibroRepository libroRepository;
    private final ClubLecturaRepository clubLecturaRepository;
    private final SalaRepository salaRepository;
    private final InscripcionRepository inscripcionRepository;



    public AlquilerService(AlquilerRepository alquilerRepository, UserRepository userRepository, LibroRepository libroRepository, ClubLecturaRepository clubLecturaRepository, SalaRepository salaRepository,  InscripcionRepository inscripcionRepository) {
        this.alquilerRepository = alquilerRepository;
        this.userRepository = userRepository;
        this.libroRepository = libroRepository;
        this.clubLecturaRepository = clubLecturaRepository;
        this.salaRepository = salaRepository;
        this.inscripcionRepository = inscripcionRepository;

    }

    public Alquiler alquilarLibro(String libroId, String userId, LocalDateTime fechaRecogida, LocalDateTime fechaDevolucion) {
        User user = userRepository.findById(userId).orElse(null);
        Libro libro = libroRepository.findById(libroId).orElse(null);
        List<Alquiler> alquilerRegistrado = alquilerRepository.findByLibroIdAndFechaRecogidaLessThanEqualAndFechaDevolucionGreaterThanEqual(libroId, fechaDevolucion, fechaRecogida);

        if ((user == null) || (libro == null)) {
            throw new RuntimeException("Usuario no registrado");
        }
        /*if (libro.getUser() != null) {
            throw new RuntimeException("El libro está alquilado a otro usuario");
        }*/

        if (!alquilerRegistrado.isEmpty()) {
            throw new RuntimeException("El libro ya está alquilado en el rango de fechas solicitado.");
        }
        Alquiler alquiler = new Alquiler();
        alquiler.setLibro(libro);
        alquiler.setUserId(user.getId());
        alquiler.setFechaRecogida(fechaRecogida);
        alquiler.setFechaDevolucion(fechaDevolucion);
        return alquilerRepository.save(alquiler);
    }

    public List<Alquiler> obtenerAlquileresPresentes(String userId) {
        LocalDateTime today = LocalDateTime.now();
        return alquilerRepository.findTop5ByUserIdAndFechaRecogidaLessThanEqualAndFechaDevolucionGreaterThanOrderByFechaRecogidaDesc(
                userId, today, today
        );
    }

    public List<Alquiler> obtenerAlquileresFuturos(String userId) {
        LocalDateTime today = LocalDateTime.now();
        return alquilerRepository.findByUserIdAndFechaRecogidaAfterOrderByFechaRecogidaAsc(userId, today);
    }

    public List<Alquiler> obtenerAlquileresPasados(String userId) {
        LocalDateTime today = LocalDateTime.now();
        return alquilerRepository.findTop8ByUserIdAndFechaDevolucionBeforeOrderByFechaRecogidaDesc(userId, today);
    }

    public List<Alquiler> obtenerAlquileresDelLibro(String libroId) {
        return alquilerRepository.findByLibroId(libroId);
    }


    public List<ClubLectura> obtenerProximosClubSLEctura() {
        LocalDateTime fechaActual = LocalDateTime.now();
        List <ClubLectura>  todos =clubLecturaRepository.findByFechaRecogidaAfterAndSalaIdIsNotNullOrderByFechaRecogidaAsc(fechaActual);

        return todos.stream()
                .filter(club -> {
                    Sala sala = salaRepository.findById(club.getSalaId()).orElse(null);
                    if (sala == null) return false; // ignoramos si no hay sala válida

                    long inscritos = inscripcionRepository.countByClubLectura_Id(club.getId());
                    return inscritos < sala.getCapacidad(); // solo si no está lleno
                })
                .collect(Collectors.toList());

    }

    public List<RespuestaSalaOcupadaDto> obtenerFranjasHorariasMes(LocalDate fechaMes) {

        LocalDate fechaInicial = fechaMes.withDayOfMonth(1);

        LocalDate fechaFinal = fechaMes.plusMonths(1).withDayOfMonth(1);

        ArrayList<RespuestaSalaOcupadaDto> salas = new ArrayList<>();

        LocalDate current = fechaInicial;
        while (!current.isAfter(fechaFinal)) {

            List<RespuestaSalaOcupadaDto> lista = obtenerFranjasHorarias(current);
            salas.addAll(lista);
            current = current.plusDays(1);
        }
        return salas;
    }


        //retornamos un array list de dto de salas ocupadas
    public List<RespuestaSalaOcupadaDto> obtenerFranjasHorarias(LocalDate dia) {
        ZoneId madrid = ZoneId.of("Europe/Madrid");

        ZonedDateTime startOfDayMadrid = dia.atStartOfDay(madrid);
        ZonedDateTime endOfDayMadrid = dia.atTime(23, 59, 59).atZone(madrid);

        // Convertir a UTC y luego quitar zona para tener el "UTC plano"
        LocalDateTime from = startOfDayMadrid.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime to = endOfDayMadrid.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

        List<ClubLectura> reservas = clubLecturaRepository.findByFechaRecogidaBetweenAndSalaIdNotNullOrderBySalaId(from, to);
        ArrayList<RespuestaSalaOcupadaDto> salasOcupadas = new ArrayList<>();
        if (reservas == null || reservas.isEmpty()) {
            return salasOcupadas;
        }

        // Elimina la adición inicial antes del bucle
// System.out.println("Inicial salaId: " + reservas.get(0).getSalaId());

// Inicializa salaOcupadaDto como null
        RespuestaSalaOcupadaDto salaOcupadaDto = null;

        for (ClubLectura clubLectura : reservas) {
            System.out.println("Iterando clubLectura: " + clubLectura);
            System.out.println("clubLectura.getSalaId(): " + clubLectura.getSalaId());

            if (salaOcupadaDto == null ||
                    (clubLectura.getSalaId() != null && !clubLectura.getSalaId().equals(salaOcupadaDto.getSalaId()))) {

                if (salaOcupadaDto != null) {
                    System.out.println("Sala diferente detectada. Añadiendo salaOcupadaDto al listado: " + salaOcupadaDto);
                    salasOcupadas.add(salaOcupadaDto);
                }

                salaOcupadaDto = new RespuestaSalaOcupadaDto();
                salaOcupadaDto.setSalaId(clubLectura.getSalaId());
                salaOcupadaDto.setFecha(dia);

                System.out.println("Nueva salaOcupadaDto creada con salaId: " + salaOcupadaDto.getSalaId() + " y fecha: " + dia);
            }

            salaOcupadaDto.addClubLectura(clubLectura);
            System.out.println("ClubLectura añadido a salaOcupadaDto: " + clubLectura);
        }

// Añade la última salaOcupadaDto después del bucle
        if (salaOcupadaDto != null) {
            salasOcupadas.add(salaOcupadaDto);
        }

        return salasOcupadas;


    }


    //es para un post

    public ArrayList<LocalDateTime> obtenerFechaConFranja (FranjaHoraria franja, LocalDateTime dia){

        LocalDateTime horaInicio;
        LocalDateTime horaFin;
        switch (franja) {
            case MANANA:
                horaInicio = dia.withHour(9).withMinute(0);
                horaFin = dia.withHour(10).withMinute(55);
                break;
            case MEDIA_MANANA:
                horaInicio = dia.withHour(11).withMinute(0);
                horaFin = dia.withHour(12).withMinute(55);
                break;
            case MEDIO_DIA:
                horaInicio = dia.withHour(13).withMinute(0);
                horaFin = dia.withHour(14).withMinute(55);
                break;
            case TARDE:
                horaInicio = dia.withHour(15).withMinute(0);
                horaFin = dia.withHour(16).withMinute(55);
                break;
            case TARDE_NOCHE:
                horaInicio = dia.withHour(17).withMinute(0);
                horaFin = dia.withHour(19).withMinute(55);
                break;
            default:
                throw new IllegalArgumentException("nada " + franja);
        }

        ArrayList<LocalDateTime> fechaConFranjas = new ArrayList<>();
        fechaConFranjas.add(horaInicio);
        fechaConFranjas.add(horaFin);

        return fechaConFranjas;
    }

    public ClubLectura crearClub(String libroId, String userId, LocalDateTime fechaRecogida, LocalDateTime fechaDevolucion, String salaId, String observaciones, String franjaHoraria) {

        //encuentro usuario y libro a que pertencen crear club
        User user = userRepository.findById(userId).orElse(null);
        Libro libro = libroRepository.findById(libroId).orElse(null);
        ClubLectura clubLectura = new ClubLectura();

        //comprobacion que si libro esta ocupado o no durante el rango seleccionado

        FranjaHoraria franja = FranjaHoraria.valueOf(franjaHoraria);
        ArrayList<LocalDateTime> fFranjas = obtenerFechaConFranja(franja, fechaRecogida);
        LocalDateTime horaInicio = fFranjas.get(0);
        LocalDateTime horaFin = fFranjas.get(1);


        List<Alquiler> alquilerRegistrado = alquilerRepository.findByLibroIdAndFechaRecogidaLessThanEqualAndFechaDevolucionGreaterThanEqual(libroId, horaFin, horaInicio);
        //comprobar que la sala está libre en la franja horaria escogida:??? o comprobar que sala tienens este dia tal cual??
        //tendria que pasar aal bakend ya las fechas de recogida y devolucion con tiempo de 9 y 11
        //1=encontrar reservas en ese día para esa sala

        LocalDateTime fechaRecogidaInicio = fechaRecogida.withHour(0).withMinute(00).withSecond(00);
        LocalDateTime fechaDevolucionInicio = fechaDevolucion.withHour(23).withMinute(59).withSecond(59);
        List<ClubLectura> clubsExistentes = clubLecturaRepository.findBySalaIdAndFechaRecogidaBetween(salaId,fechaRecogidaInicio , fechaDevolucionInicio );

        if ((user == null) || (libro == null)) {
            throw new RuntimeException("Usuario no registrado");
        }
        /*if (libro.getUser() != null) {
            throw new RuntimeException("El libro está alquilado a otro usuario");
        }*/
//quitar!!!todos los alqiileres del mismo usuario para no tener conflicto
        if (!alquilerRegistrado.isEmpty()) {
            System.out.println("El libro ya está alquilado en el rango de fechas solicitado.");
            throw new RuntimeException("El libro ya está alquilado en el rango de fechas solicitado.");
        }
        System.out.println(clubsExistentes.toString());
        for (ClubLectura clubLecturaa : clubsExistentes) {
            // Comprobar si hay colapse de franjas horarias
            System.out.println("Club " + clubLecturaa.getId()+", Sala "+ clubLecturaa.getSalaId() + ", Franja club, "+ clubLecturaa.getFranjaHoraria() + ", franja " + franja + ", franjaEq "+ (franja == clubLecturaa.getFranjaHoraria()));
            if (clubLecturaa.getFranjaHoraria() == franja) {
                // Si la franja horaria ya está ocupada, devolver un mensaje de error
                throw new RuntimeException("La franja horaria seleccionada ya está ocupada en esta sala para la fecha seleccionada.");
            }
        }

        //comprobar que el usuario que esta creando club ya no tiene un club en el mismo dia en misma franja
        List<ClubLectura> clubesDelUsuarioEseDia = clubLecturaRepository.findByUserIdAndFechaRecogidaBetween(
                userId,
                fechaRecogidaInicio,
                fechaDevolucionInicio
        );

        for (ClubLectura clubUsuario : clubesDelUsuarioEseDia) {
            if (clubUsuario.getFranjaHoraria() == franja) {
                throw new RuntimeException("Ya estás organizando otro club en esta franja horaria.");
            }
        }

        //comprobar que el user que quiere crear club no está inscrito ya como participante en ningun club con misma franja
        List<Inscripcion> inscripcionesDelUsuarioEseDia = inscripcionRepository.findByUsuarioInscritoIdAndClubLectura_FechaRecogidaBetween(
                userId,
                fechaRecogidaInicio,
                fechaDevolucionInicio
        );

        for (Inscripcion inscripcion : inscripcionesDelUsuarioEseDia) {
            if (inscripcion.getClubLectura().getFranjaHoraria() == franja) {
                throw new RuntimeException("Ya estás participando en otro club en está franja horaria.");
            }
        }
        clubLectura.setLibro(libro);
        clubLectura.setUserId(user.getId());
        clubLectura.setFechaRecogida(horaInicio);
        clubLectura.setFechaDevolucion(horaFin);
        clubLectura.setSalaId(salaId);
        clubLectura.setFranjaHoraria(franja);
        clubLectura.setObservaciones(observaciones);


        ClubLectura nuevoClub = clubLecturaRepository.save(clubLectura);

// Inscribir automáticamente al organizador
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setUsuarioInscritoId(user.getId());
        inscripcion.setClubLectura(nuevoClub);
        inscripcionRepository.save(inscripcion);

        return nuevoClub;
    }

    public Inscripcion apuntarse(String idClub, String username) {
        System.out.println("es un print desde servicio de username"+username);
        User user = userRepository.findByUsername(username).get();
        if (user == null) {
            System.out.println("ERROR: Usuario no encontrado en BD para username: " + username);
            // Listar todos los usuarios para debug
            System.out.println("Usuarios existentes en BD:");
            userRepository.findAll().forEach(u -> System.out.println("- " + u.getUsername()+",,,"+username.equals(u.getUsername())));
            List<User> users = userRepository.findAll();
            for (User u : users) {
                System.out.println("Usuario en BD: [" + u.getUsername() + "]");
                System.out.println("-> equals(): " + username.equals(u.getUsername()));
                System.out.println("-> equalsIgnoreCase(): " + username.equalsIgnoreCase(u.getUsername()));
                System.out.println("-> trimmed equals(): " + username.trim().equals(u.getUsername().trim()));
            }

            throw new RuntimeException("Usuario no encontrado con username: " + username);

        }
        System.out.println("es un print desde servicio de usuario id que deberia obtener"+user.getId());

        ClubLectura clubLectura = clubLecturaRepository.findById(idClub).orElse(null);
        Sala salaEvento=salaRepository.findById(clubLectura.getSalaId()).orElse(null);


        boolean yaInscrito = inscripcionRepository.existsByUsuarioInscritoIdAndClubLectura_Id(user.getId(), clubLectura.getId());

        if(yaInscrito){
            throw new RuntimeException("El mismo usuario seo organizador o participante  no puede inscribirse dos veces en mismo club");
        }


        //un usuario no puede estar opuntado en dos clas que tienen solapaminetro en horarios:
        List<Inscripcion> inscripcionesUsuario = obtenerProximosClubSLEcturaDEUsuarioLogeado(username);
        for (Inscripcion insc : inscripcionesUsuario) {
            ClubLectura clubExistente = insc.getClubLectura();

            boolean mismaFecha = clubExistente.getFechaRecogida().toLocalDate()
                    .isEqual(clubLectura.getFechaRecogida().toLocalDate());

            boolean mismaFranja = clubExistente.getFranjaHoraria()
                    .equals(clubLectura.getFranjaHoraria());
            System.out.println(mismaFecha);
            System.out.println(mismaFranja);


            if (mismaFecha && mismaFranja) {
                throw new RuntimeException("Ya estás organizando/participando en otro club con el mismo horario.");

            }
        }


        int inscritos = inscripcionRepository.countByClubLectura_Id(idClub);
        if(salaEvento.getCapacidad()<=inscritos){
            throw new RuntimeException("La sala llena no cabe más gente");
        }
        ClubLectura clubNuevo=clubLecturaRepository.findById(idClub).orElse(null);
        Inscripcion nuevaInscripcion = new Inscripcion();
        nuevaInscripcion.setUsuarioInscritoId(user.getId());
        nuevaInscripcion.setClubLectura(clubNuevo);

        return inscripcionRepository.save(nuevaInscripcion);
    }

    //obtengo todos clubs de lectura donde usuario logeado es organizador
    public List<ClubLectura> obtenerProximosClubSLEcturaDEOrganizador(String username) {
        LocalDateTime fechaActual = LocalDateTime.now();
        User user = userRepository.findByUsername(username).orElse(null);
        return clubLecturaRepository.findByFechaRecogidaAfterAndUserIdAndSalaIdIsNotNull(fechaActual, user.getId());


    }

    //obtengo todas inscripcionses con informacion d eclub de usuario logeado
    public List<Inscripcion> obtenerProximosClubSLEcturaDEUsuarioLogeado(String username) {
        LocalDateTime fechaActual = LocalDateTime.now();
        User user = userRepository.findByUsername(username).orElse(null);
        return inscripcionRepository.findByClubLecturaFechaRecogidaAfterAndUsuarioInscritoId(fechaActual,user.getId());
    }
//borrar Inscripcion:
    public boolean borrarInscripcion(String idInscripcion) {
        if (inscripcionRepository.existsById(idInscripcion)) {
            inscripcionRepository.deleteById(idInscripcion);
            return true;
        }
        return false;
    }



    public ArrayList<LocalDate> obtenerDisponibilidadMes(String libroId, LocalDate fechaMes){

        LocalDateTime fechaInicial = fechaMes.withDayOfMonth(1).atStartOfDay();

        LocalDateTime fechaFinal = fechaMes.plusMonths(1).withDayOfMonth(1).atStartOfDay();


        List<Alquiler> listaOcupados = clubLecturaRepository.findByLibroIdAndFechaDevolucionBetweenAndFechaRecogidaBetweenAndSalaIdNull(libroId, fechaInicial, fechaFinal, fechaInicial, fechaFinal);


        //ArrayList<RespuestaDiaOcupadoDto> diasOcupados = new ArrayList<>();
        ArrayList<LocalDate> diasOcupados = new ArrayList<>();

        for (Alquiler alquiler : listaOcupados) {

            /*if(alquiler instanceof ClubLectura){
                ClubLectura clubLectura = (ClubLectura) alquiler;
                RespuestaDiaOcupadoDto diaOcupadoDto = new RespuestaDiaOcupadoDto();
                diaOcupadoDto.setFecha(clubLectura.getFechaRecogida().toLocalDate());
                diaOcupadoDto.setClubLectura(clubLectura);
            }
            else{*/
                LocalDate current = alquiler.getFechaRecogida().toLocalDate();
                LocalDate endDate = alquiler.getFechaDevolucion().toLocalDate();
                while (!current.isAfter(endDate)) {
                    /*RespuestaDiaOcupadoDto diaOcupadoDto = new RespuestaDiaOcupadoDto();
                    diaOcupadoDto.setFecha(current);
                    diasOcupados.add(diaOcupadoDto);*/
                    diasOcupados.add(current);
                    current = current.plusDays(1);
                }
            //}

        }
        return diasOcupados;

    }



    public ClubLectura actualizarClub(String libroId, String userId, LocalDateTime fechaRecogida, LocalDateTime fechaDevolucion, String salaId, String observaciones, String franjaHoraria, String username, String idClub) {
    //comprobar si es organizador porque solo organizador puede cambiar los datos incluso organizador puede suspender el club
        ClubLectura clubLectura=clubLecturaRepository.findById(idClub).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);


        if (!user.getId().equals(clubLectura.getUserId())) {
            throw new RuntimeException("No estás autorizado para actualizar este club.");
        }

            //esta autorizado
            Libro libro = libroRepository.findById(libroId).orElse(null);
            //comprobacion que si libro esta ocupado o no
            List<Alquiler> alquilerRegistrado = alquilerRepository.findByLibroIdAndFechaRecogidaLessThanEqualAndFechaDevolucionGreaterThanEqual(libroId, fechaDevolucion, fechaRecogida);
            //comprobar que la sala está libre en la franja horaria escogida:
            List<ClubLectura> clubsExistentes = clubLecturaRepository.findBySalaIdAndFechaRecogidaBetween(salaId, fechaDevolucion, fechaRecogida);
            if ((user == null) || (libro == null)) {
                throw new RuntimeException("Usuario no registrado");
            }
        /*if (libro.getUser() != null) {
            throw new RuntimeException("El libro está alquilado a otro usuario");
        }*/

            if (!alquilerRegistrado.isEmpty()) {
                throw new RuntimeException("El libro ya está alquilado en el rango de fechas solicitado.");
            }

            //simepre hay que comprobar si es igual o no el campo que acabo de venir
            FranjaHoraria franja = FranjaHoraria.valueOf(franjaHoraria);
            for (ClubLectura clubLecturaa : clubsExistentes) {
                // Comprobar si hay colapse de franjas horarias
                if (clubLecturaa.getFranjaHoraria() == franja && !clubLecturaa.getId().equals(clubLectura.getId())) {
                    // Si la franja horaria ya está ocupada, devolver un mensaje de error
                    throw new RuntimeException("La franja horaria seleccionada ya está ocupada en esta sala para la fecha seleccionada.");
                }
            }



       //Comprobar si cabe gente en nueva sala
        if (!clubLectura.getSalaId().equals(salaId)) {
            Sala nuevaSala = salaRepository.findById(salaId).orElse(null);
            int inscritos = inscripcionRepository.countByClubLectura_Id(idClub);
            if (nuevaSala==null || nuevaSala.getCapacidad() < inscritos) {
                throw new RuntimeException("La nueva sala no tiene capacidad suficiente para los inscritos actuales o no existe.");
            }
        }

            LocalDateTime dia = fechaRecogida;
            LocalDateTime horaInicio;
            LocalDateTime horaFin;
            switch (franja) {
                case MANANA:
                    horaInicio = dia.withHour(9).withMinute(0);
                    horaFin = dia.withHour(11).withMinute(0);
                    break;
                case MEDIA_MANANA:
                    horaInicio = dia.withHour(11).withMinute(0);
                    horaFin = dia.withHour(13).withMinute(0);
                    break;
                case MEDIO_DIA:
                    horaInicio = dia.withHour(13).withMinute(0);
                    horaFin = dia.withHour(15).withMinute(0);
                    break;
                case TARDE:
                    horaInicio = dia.withHour(15).withMinute(0);
                    horaFin = dia.withHour(17).withMinute(0);
                    break;
                case TARDE_NOCHE:
                    horaInicio = dia.withHour(17).withMinute(0);
                    horaFin = dia.withHour(20).withMinute(0);
                    break;
                default:
                    throw new IllegalArgumentException("nada " + franja);
            }

            clubLectura.setLibro(libro);
            clubLectura.setUserId(user.getId());
            clubLectura.setFechaRecogida(horaInicio);
            clubLectura.setFechaDevolucion(horaFin);
            clubLectura.setSalaId(salaId);
            clubLectura.setFranjaHoraria(franja);
            clubLectura.setObservaciones(observaciones);

        ClubLectura clubActualizado = clubLecturaRepository.save(clubLectura);

        List<Inscripcion> inscripciones = inscripcionRepository.findByClubLectura_Id(idClub);
        for (Inscripcion inscripcion : inscripciones) {
            inscripcion.setClubLectura(clubActualizado);
            inscripcionRepository.save(inscripcion);
        }
        return clubActualizado;
        }

    public List<Sala> obtenerSalasExistentes() {
        return salaRepository.findAll();
    }

    public boolean eliminarAlquiler(String username, String id) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) throw new RuntimeException("Usuario no encontrado");

        // Intentar encontrar primero si es un Club de Lectura
        Optional<ClubLectura> clubOpt = clubLecturaRepository.findByIdAndAndSalaIdNotNull(id);
        if (clubOpt.isPresent()) {
            ClubLectura clubLectura = clubOpt.get();
            if (!user.getId().equals(clubLectura.getUserId())) {
                throw new RuntimeException("No estás autorizado para suspender este club");
            }
            return this.suspenderClub(username, id); // reutiliza tu buen método
        }

        // 3. Lógica para Alquileres (nuevo enfoque)
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(id);
        if (alquilerOpt.isPresent()) {
            Alquiler alquiler = alquilerOpt.get();

            // 3.1. Verificar permisos
            if (!user.getId().equals(alquiler.getUserId())) {
                throw new RuntimeException("No autorizado para eliminar este alquiler");
            }

            // 3.2. Usar Instant para comparar en UTC
            // Conversión CORRECTA a Instant
            Instant ahora = Instant.now();
            Instant fechaRecogida = alquiler.getFechaRecogida()
                    .atOffset(ZoneOffset.UTC)
                    .toInstant();
            Instant fechaDevolucion = alquiler.getFechaDevolucion()
                    .atOffset(ZoneOffset.UTC)
                    .toInstant();


            if (fechaRecogida.isAfter(ahora)) {
                // Caso FUTURO: Eliminar
                alquilerRepository.delete(alquiler);
                return true;
            } else if (fechaRecogida.isBefore(ahora) && fechaDevolucion.isAfter(ahora)) {
                // Caso PRESENTE: Marcar como devuelto (en UTC)
                alquiler.setFechaDevolucion(ahora.atOffset(ZoneOffset.UTC).toLocalDateTime());
                alquilerRepository.save(alquiler);
                return true;
            } else {
                // Caso PASADO: No se puede modificar
                throw new RuntimeException("No se puede modificar un alquiler ya finalizado");
            }
        }
        // Si no es ni club ni alquiler, lanzar error
        throw new RuntimeException("No se encontró el alquiler o club con ese ID");
    }


    public ClubLectura actualizarClubObservaciones( String observaciones, String username, String idClub) {
        ClubLectura clubLectura=clubLecturaRepository.findById(idClub).orElse(null);
        User user = userRepository.findByUsername(username).get();
        if (!user.getId().equals(clubLectura.getUserId())) {
            throw new RuntimeException("No estás autorizado para actualizar este club.");
        }

        clubLectura.setObservaciones(observaciones);

        ClubLectura clubActualizado = clubLecturaRepository.save(clubLectura);
        return clubActualizado;
    }

    public boolean suspenderClub(String username, String idClub) {
        //encuentro club y encuentro usuario-organizador
        ClubLectura clubLectura=clubLecturaRepository.findById(idClub).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);

        if (!clubLectura.getUserId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para suspender este club");
        }

        List<Inscripcion> inscripciones = inscripcionRepository.findByClubLectura_Id(idClub);
        for (Inscripcion inscripcion : inscripciones) {
            inscripcionRepository.deleteById(inscripcion.getId());
        }
        clubLecturaRepository.deleteById(idClub);
        return true;
    }
}


    //obtener todos clubs creados por usuario que tiene secsion iniciada

    //obtener todos club donde el usuario está inscrito ahora mismo




    //  libro.setUser(user);
        //Libro libro = libroRepository.findById(libroId).get();
        //Alquiler alquiler=alquilerRepository.findByLibroId(libroId);
        //if (alquiler!=null) {
         //   throw new RuntimeException("El libro ya está alquilado");
        //}
        //Alquiler nuevoAlquiler = new Alquiler();
       // nuevoAlquiler.setLibro(libro);
       // nuevoAlquiler.setUser(user);
        //return alquilerRepository.save(nuevoAlquiler);


    //public Alquiler devolverLibro(String libroId, String userId) {
    //    User user = userRepository.findById(userId).get();
    //    Libro libro = libroRepository.findById(libroId).get();
     //   Alquiler alquiler=alquilerRepository.findByLibroIdAndUserId(libroId,userId);
     //   if (alquiler!=null) {
     //       throw new RuntimeException("El libro no está alquilado");
     //   }

     //   alquilerRepository.delete(alquiler);

     //   return alquiler;
  //  }


  /*  public Alquiler devolverLibro(String libroId, String userId) {
        Optional<User> userOp = userRepository.findById(userId);
        if (!(userOp ==null)) {
            User user = userOp.get();
        }
        Optional<Libro> libroOp = libroRepository.findById(libroId);
        Libro libro=libroOp.get();
        libro.setUser(null);
        return libroRepository.save(libro);


    }
*/

    //• Permitir a un usuario autenticado alquilar un libro (asignando el usuario al libro, con
    //libro.setUser(user)).
    //• Permitir que solo el usuario que ha alquilado el libro lo devuelva (estableciendo el campo
    //libro.setUser(null)).

