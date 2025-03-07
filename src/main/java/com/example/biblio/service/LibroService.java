package com.example.biblio.service;
import com.example.biblio.model.Libro;
import com.example.biblio.repository.LibroRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class LibroService {
    private final LibroRepository repository;
    public LibroService(LibroRepository repository) {
        this.repository = repository;
    }
    public List<Libro> listarLibros() {
        return repository.findAll();
    }
    public Libro buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
    public Libro crearLibro(Libro libro) {
        return repository.save(libro);
    }
    public Libro actualizarLibro(Long id, Libro detalles) {Libro existente = repository.findById(id).orElse(null);
        if (existente != null) {
            existente.setTitulo(detalles.getTitulo());
            existente.setAutor(detalles.getAutor());
            existente.setAnioPublicacion(detalles.getAnioPublicacion());
            return repository.save(existente);
        }
        return null;
    }
    public boolean eliminarLibro(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }


    public List<Libro> buscarPorAutorYAnio(String autor, Integer anio) {
        if (autor != null && anio != null) {
            return repository.findByAutorAndAnioPublicacion(autor, anio);
        } else if (autor != null) {
            return repository.findByAutor(autor);
        } else if (anio != null) {
            return repository.findByAnioPublicacion(anio);
        }
        return repository.findAll(); // si no hay ningun filtro, buscamos todos
    }
}