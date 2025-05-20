package com.example.biblio.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @JsonIgnore
    private String password; // No se almacena al embebirlo
    public User() {
    }
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
