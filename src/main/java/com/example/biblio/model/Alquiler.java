package com.example.biblio.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Document(collection = "alquileres")
 public class Alquiler {
  @Id
  private String id;
  private String userId;
  private Libro libro;
  private LocalDateTime fechaRecogida;
  private LocalDateTime fechaDevolucion;

  public Alquiler() {
  }

 public Alquiler(String userId, Libro libro, LocalDateTime fechaRecogida, LocalDateTime fechaDevolucion) {
  this.userId = userId;
  this.libro = libro;
  this.fechaRecogida = fechaRecogida;
  this.fechaDevolucion = fechaDevolucion;
 }

 public String getId() {
  return id;
 }
 public void setId(String id) {
  this.id = id;
 }

 public LocalDateTime getFechaRecogida() {
  return fechaRecogida;
 }

 public void setFechaRecogida(LocalDateTime fechaRecogida) {
  this.fechaRecogida = fechaRecogida;
 }

 public LocalDateTime getFechaDevolucion() {
  return fechaDevolucion;
 }

 public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
  this.fechaDevolucion = fechaDevolucion;
 }

 public String getUserId() {
   return userId;
  }

  public void setUserId(String userId) {
   this.userId = userId;
  }

  public Libro getLibro() {
  return libro;
 }

 public void setLibro(Libro libro) {
  this.libro = libro;
 }
}
