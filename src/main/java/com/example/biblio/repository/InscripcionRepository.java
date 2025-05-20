package com.example.biblio.repository;

import com.example.biblio.model.ClubLectura;
import com.example.biblio.model.Inscripcion;
import com.example.biblio.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InscripcionRepository extends MongoRepository<Inscripcion,String>  {
boolean existsByUsuarioInscritoIdAndClubLectura_Id(String userId, String clubId);
int countByClubLectura_Id(String clubId);
    //encontrar todas inscripcioones futuras de usuario logeado
    List<Inscripcion> findByClubLecturaFechaRecogidaAfterAndUsuarioInscritoId(LocalDateTime fechaRecogida, String usuarioInscritoId);
    List<Inscripcion> findByClubLectura_Id( String clubId);

    List<Inscripcion> findByUsuarioInscritoIdAndClubLectura_FechaRecogidaBetween(String usuarioInscritoId, LocalDateTime fechaRecogida, LocalDateTime fechaDevolucion);
    //encontrar todos inscripciones de un club


    //encontrar clubs por


    //crear inscripcion

    //salir de inscripcion

    //
}
