package com.example.biblio.repository;

import com.example.biblio.model.Libro;
import com.example.biblio.model.Sala;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SalaRepository extends MongoRepository<Sala,String>{



}
