/*package com.example.biblio.model;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name = "alquiler")
public class Alquiler {

@Id
//para que se crea automaticamente y sea autoincriment
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private LocalDate fechaRecogida;
private LocalDate fechaDevolucion;

 @ManyToOne
 @JoinColumn(name = "user_id") // Quien alquila
 private User user;

 @ManyToOne
@JoinColumn(name = "libro_id") // El libro alquilado
 private Libro libro;

 public Alquiler() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public LocalDate getFechaRecogida() {
  return fechaRecogida;
 }

 public void setFechaRecogida(LocalDate fechaRecogida) {
  this.fechaRecogida = fechaRecogida;
 }

 public LocalDate getFechaDevolucion() {
  return fechaDevolucion;
 }

 public void setFechaDevolucion(LocalDate fechaDevolucion) {
  this.fechaDevolucion = fechaDevolucion;
 }

 public User getUser() {
  return user;
 }

 public void setUser(User user) {
  this.user = user;
 }

 public Libro getLibro() {
  return libro;
 }

 public void setLibro(Libro libro) {
  this.libro = libro;
 }
}
*/