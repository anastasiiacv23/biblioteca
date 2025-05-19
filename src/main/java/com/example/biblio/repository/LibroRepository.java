package com.example.biblio.repository;
import com.example.biblio.model.Libro;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroRepository extends MongoRepository<Libro,String>{

    // Encuentra libros por autor exacto
    List<Libro> findByAutor(String autor);
    // Encuentra libros que contengan "fragmento" en el ttulo (LIKE %fragmento %)

    List<Libro> findByTituloLikeIgnoreCase(String fragmento);
    // Encuentra libros publicados antes de un ao

    List<Libro> findByAnioPublicacionLessThan(int anio);

    List<Libro> findByAutorAndAnioPublicacion(String autor, Integer anio);

    List<Libro>findLibroByAutorContaining(String autor);
    List<Libro> findByAnioPublicacion(Integer anio);


    @Aggregation(pipeline = {
            "{ $search: { " +
                    "index: 'default', " +
                    "text: { " +
                    "query: ?0, " +
                    "path: 'titulo' " +  // Solo busca en el campo 'titulo'
                    "} " +
                    "} }"
    })
    List<Libro> buscarPorTituloExacto(String titulo);



}
