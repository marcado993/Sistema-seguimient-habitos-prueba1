package com.habittracker.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String correo;
    private String contrasena;
    private List<Objetivo> objetivos;
    private String nombre;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void establecerObjetivo(String titulo, String descripcion, java.util.Date fechaInicio, java.util.Date fechaFin) {
        // TODO Implementación pendiente
    }

    public void planificarObjetivo(Objetivo objetivo, PlanificarObjetivo plan) {
        // TODO Implementación pendiente
    }

    // Constructor vacío
    public Usuario() {}

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
