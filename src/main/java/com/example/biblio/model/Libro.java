package com.example.biblio.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


    @Document(collection = "libros")
    public class Libro {
        @Id
        private String id;
        //resto de atributos
        private String titulo;
        private String autor;
        private String isbn;
        private String editorial;

        private Integer anioPublicacion;

        public Libro() {
            this.id = String.valueOf(System.currentTimeMillis());
        }


        public Libro(String id, String titulo, String autor, Integer anioPublicacion) {
            this.id = id;
            this.titulo = titulo;
            this.autor = autor;
            this.anioPublicacion = anioPublicacion;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getAutor() {
            return autor;
        }

        public void setAutor(String autor) {
            this.autor = autor;
        }

        public Integer getAnioPublicacion() {
            return anioPublicacion;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public String getEditorial() {
            return editorial;
        }

        public void setEditorial(String editorial) {
            this.editorial = editorial;
        }

        public void setAnioPublicacion(Integer anioPublicacion) {
            this.anioPublicacion = anioPublicacion;
        }
    }

