package com.example.biblio.repository;

import com.example.biblio.model.Alquiler;
import com.example.biblio.model.ClubLectura;
import com.example.biblio.model.Sala;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClubLecturaRepository extends MongoRepository<ClubLectura,String> {

    //encuentro todos clubs de lectura de un libro en concreto
    List<ClubLectura> findByLibroId(String libroId);

    //encuentro todos clubs de ahora creados

    //todos clubs de lectura anunciados para proximo
    List<ClubLectura> findByFechaRecogidaAfterAndSalaIdIsNotNullOrderByFechaRecogidaAsc(LocalDateTime fechaActual);

    //encuentro todos clubs de lectura proximos creados por X usuario
    List<ClubLectura> findByFechaRecogidaAfterAndUserId(LocalDateTime fechaActual, String userId);
    //encuentro todos clubs de lectura proximos creados por X usuario
    List<ClubLectura> findByFechaRecogidaAfterAndUserIdAndSalaIdIsNotNull(LocalDateTime fechaActual, String userId);

    //es un boolen para borrar alquilerees etc.
    boolean existsByIdAndSalaIdIsNotNull(String id);

    //encientro todos club de lectura creados por user en una concreta fecha
    List<ClubLectura> findByUserIdAndFechaRecogidaBetween(String userId, LocalDateTime start, LocalDateTime end);


    List<Alquiler> findByLibroIdAndFechaDevolucionBetweenAndFechaRecogidaBetweenAndSalaIdNull(String libroId, LocalDateTime fdeva, LocalDateTime fdevb, LocalDateTime freca, LocalDateTime frecd);

    //encuentro todas salas disponibles para la fecha escogida por usuario//
    //todo club de ese dia, pero esto me va a dar lista ordenada por salas si hay una sala con 5 clubs de lectura no lo retorno , en servicio lo reccorremos y buscamos los que tienen 5 clubs , es un contador
    List<ClubLectura> findByFechaRecogidaBetweenAndSalaIdNotNullOrderBySalaId(LocalDateTime inicioDia, LocalDateTime finDia);

    //es para capa de seguridad; en el moneto de post en servicio para reservar sala para club
    //todos clublectura en sala concreta del dia conreto para despues saber que franjas estan ocupadas
    //si vienen 5 club eso significa que no hay nada ese dia
    List<ClubLectura> findBySalaIdAndFechaRecogidaBetween(String salaId, LocalDateTime inicioDia, LocalDateTime finDia);

    Optional<ClubLectura> findByIdAndAndSalaIdNotNull(String id);

    List<ClubLectura>  findByIdAndFechaRecogidaLessThanEqualAndFechaDevolucionGreaterThanEqual(String libroId, LocalDateTime fechaDevolucion, LocalDateTime fechaRecogida);
    List<Alquiler> findTop5ByUserIdAndFechaRecogidaLessThanEqualAndFechaDevolucionGreaterThanOrderByFechaRecogidaDesc(String userId, LocalDateTime
            fechaRecogida, LocalDateTime fechaDevolucion);
    List<Alquiler> findByUserIdAndFechaRecogidaAfterOrderByFechaRecogidaAsc(String userId, LocalDateTime
            today);
    List<Alquiler>findTop8ByUserIdAndFechaDevolucionBeforeOrderByFechaRecogidaDesc(String userId, LocalDateTime fechaRecogida);
}
