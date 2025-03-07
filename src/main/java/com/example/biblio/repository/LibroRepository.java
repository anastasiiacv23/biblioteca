package com.example.biblio.repository;
import com.example.biblio.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface LibroRepository extends JpaRepository <Libro,Long> {

    // Encuentra libros por autor exacto
    List<Libro> findByAutor(String autor);
    // Encuentra libros que contengan "fragmento" en el ttulo (LIKE %fragmento %)
    List<Libro> findByTituloContaining(String fragmento);
    // Encuentra libros publicados antes de un ao
    List<Libro> findByAnioPublicacionLessThan(int anio);

    List<Libro> findByAutorAndAnioPublicacion(String autor, Integer anio);

    List<Libro> findByAnioPublicacion(Integer anio);
}
