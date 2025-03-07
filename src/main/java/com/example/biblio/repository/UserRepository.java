package com.example.biblio.repository;

import com.example.biblio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

// la interfaz UserRepository hereda de JpaRepository,eso nos permite acceder a métodos de base de datos.
//Al extender JpaRepository, obtenemos automáticamente métodos CRUD:
//findById(Long id)
//save(User user)
//deleteById(Long id)
//findAll()

public interface UserRepository extends JpaRepository <User,Long> {

    boolean existsByUsername(String username);
    User findByUsername(String username);
    //Optional<User> findByUsername(String username);

}
