package com.example.biblio.repository;

import com.example.biblio.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

// la interfaz UserRepository hereda de JpaRepository,eso nos permite acceder a métodos de base de datos.
//Al extender JpaRepository, obtenemos automáticamente métodos CRUD:
//findById(String id)
//save(User user)
//deleteById(String id)
//findAll()

public interface UserRepository extends MongoRepository<User,String> {
    boolean existsByUsername(String username);

   // User findByUsername(String username);
    Optional<User> findByUsername(String username);

}
