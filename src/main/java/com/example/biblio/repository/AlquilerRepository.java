package com.example.biblio.repository;
import com.example.biblio.model.Alquiler;
import com.example.biblio.model.Libro;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface AlquilerRepository extends MongoRepository<Alquiler,String> {

    List<Alquiler>  findByLibroId(String libroId);
    List<Alquiler>  findByLibroIdAndFechaRecogidaLessThanEqualAndFechaDevolucionGreaterThanEqual(String libroId, LocalDateTime fechaDevolucion, LocalDateTime fechaRecogida);
    List<Alquiler> findTop5ByUserIdAndFechaRecogidaLessThanEqualAndFechaDevolucionGreaterThanOrderByFechaRecogidaDesc(String userId, LocalDateTime
            fechaRecogida, LocalDateTime fechaDevolucion);
    List<Alquiler> findByUserIdAndFechaRecogidaAfterOrderByFechaRecogidaAsc(String userId, LocalDateTime today);
    List<Alquiler>findTop20ByUserIdAndFechaDevolucionBeforeOrderByFechaRecogidaDesc(String userId, LocalDateTime fechaRecogida);


    List<Alquiler> findTop8ByUserIdAndFechaDevolucionBeforeOrderByFechaRecogidaDesc(String userId, LocalDateTime today);
}
