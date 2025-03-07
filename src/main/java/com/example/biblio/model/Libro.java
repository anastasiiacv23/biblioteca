package com.example.biblio.model;
import jakarta.persistence.*;
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String autor;

    @Column(nullable = true)
    private Integer anioPublicacion;


    public Libro() {
// Constructor vacio
    }
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User usuario; // Usuario que alquila el libro (puede ser null si est
    //disponible)

    public Libro(Long id, String titulo, String autor, int anioPublicacion) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public User getUser() { return usuario; }
    public void setUser(User user) { this.usuario = user; }


}
