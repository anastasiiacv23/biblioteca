package com.example.biblio.service;

//import com.example.biblio.model.Alquiler;
import com.example.biblio.model.Libro;
import com.example.biblio.model.User;

import com.example.biblio.repository.LibroRepository;
import com.example.biblio.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlquilerService {
   // private final AlquilerRepository alquilerRepository;
    private final UserRepository userRepository;
    private final LibroRepository libroRepository;


    public AlquilerService(  UserRepository userRepository, LibroRepository libroRepository, UserRepository userRepository1, LibroRepository libroRepository1) {
       // this.alquilerRepository = alquilerRepository;
        this.userRepository = userRepository1;
        this.libroRepository = libroRepository1;
    }


    public Libro alquilarLibro(Long libroId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Libro libro = libroRepository.findById(libroId).orElse(null);

        if ((user==null) || (libro==null)) {
            throw new RuntimeException("Usuario no registrado");
        }
        if (libro.getUser() != null) {
            throw new RuntimeException("El libro está alquilado a otro usuario");
        }


        libro.setUser(user);
        return libroRepository.save(libro);
        //Libro libro = libroRepository.findById(libroId).get();
        //Alquiler alquiler=alquilerRepository.findByLibroId(libroId);
        //if (alquiler!=null) {
         //   throw new RuntimeException("El libro ya está alquilado");
        //}
        //Alquiler nuevoAlquiler = new Alquiler();
       // nuevoAlquiler.setLibro(libro);
       // nuevoAlquiler.setUser(user);
        //return alquilerRepository.save(nuevoAlquiler);
    }

    //public Alquiler devolverLibro(Long libroId, Long userId) {
    //    User user = userRepository.findById(userId).get();
    //    Libro libro = libroRepository.findById(libroId).get();
     //   Alquiler alquiler=alquilerRepository.findByLibroIdAndUserId(libroId,userId);
     //   if (alquiler!=null) {
     //       throw new RuntimeException("El libro no está alquilado");
     //   }

     //   alquilerRepository.delete(alquiler);

     //   return alquiler;
  //  }


    public Libro devolverLibro(Long libroId, Long userId) {
        Optional<User> userOp = userRepository.findById(userId);
        if (!(userOp ==null)) {
            User user = userOp.get();
        }
        Optional<Libro> libroOp = libroRepository.findById(libroId);
        Libro libro=libroOp.get();
        libro.setUser(null);
        return libroRepository.save(libro);


    }





    //• Permitir a un usuario autenticado alquilar un libro (asignando el usuario al libro, con
    //libro.setUser(user)).
    //• Permitir que solo el usuario que ha alquilado el libro lo devuelva (estableciendo el campo
    //libro.setUser(null)).
}
