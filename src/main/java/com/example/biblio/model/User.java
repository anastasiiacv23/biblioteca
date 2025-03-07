package com.example.biblio.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  Asegura que el nombre de usuario sea único y obligatorio.
    @Column(unique = true, nullable = false)
    private String username;
    // no puede ser nulo la contraseña
    @Column(nullable = false)
    private String password;

    public User() { }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    /*@ManyToMany
    @JoinTable(
            name="historial_alquiler",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns =  @JoinColumn(name = "libro_id")
    )
    @JsonIgnore
    private List<Libro> historialAlquiler=new ArrayList<>();
*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
