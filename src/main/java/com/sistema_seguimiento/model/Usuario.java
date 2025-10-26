package com.sistema_seguimiento.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Clase Usuario según el diagrama de clases
 * Entidad JPA compatible con Supabase/PostgreSQL
 */
@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, unique = true, length = 150)
    private String correo;
    
    @Column(nullable = false, length = 255)
    private String contrasena;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    
    @Column(columnDefinition = "boolean default true")
    private Boolean activo;
    
    /**
     * 🟢 FASE VERDE - Campo puntos para gamificación
     * @since 2.0
     */
    @Column(name = "puntos", nullable = false)
    private Integer puntos = 0;
    
    // Constructores
    public Usuario() {
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
        this.puntos = 0;
    }
    
    public Usuario(String nombre, String correo, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
    }
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
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
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
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
    
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    /**
     * 🟢 FASE VERDE - Getter para puntos
     * @return puntos del usuario
     */
    public Integer getPuntos() {
        return puntos;
    }
    
    /**
     * 🟢 FASE VERDE - Setter para puntos
     * @param puntos nuevos puntos
     */
    public void setPuntos(Integer puntos) {
        this.puntos = puntos != null ? puntos : 0;
    }
    
    /**
     * 🟢 FASE VERDE - Métodos adicionales para tests
     */
    public void setUsername(String nombre) {
        this.nombre = nombre;
    }
    
    public void setEmail(String correo) {
        this.correo = correo;
    }
    
    public void setPassword(String contrasena) {
        this.contrasena = contrasena;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", puntos=" + puntos +
                ", fechaRegistro=" + fechaRegistro +
                ", activo=" + activo +
                '}';
    }
}
