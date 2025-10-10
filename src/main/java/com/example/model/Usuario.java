package com.example.model;

/**
 * Clase Usuario según el diagrama de clases
 */
public class Usuario {
    
    private String nombre;
    private String correo;
    private String contrasena;
    
    // Constructores
    public Usuario() {}
    
    public Usuario(String nombre, String correo, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }
    
    // Métodos de negocio
    public boolean establecerObjetivo(String titulo, String descripcion, String fechaInicio, String fechaFin) {
        // Lógica para establecer objetivo
        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }
        return true;
    }
    
    public Objetivo planificarObjetivo(Objetivo objetivo, String plan) {
        // Lógica para planificar objetivo
        return objetivo;
    }
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }
}
