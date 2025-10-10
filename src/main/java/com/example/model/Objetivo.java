package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "objetivos")
public class Objetivo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoObjetivo estado;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_limite")
    private LocalDateTime fechaLimite;
    
    @Column(name = "progreso")
    private Integer progreso = 0; // Porcentaje de 0 a 100
    
    @Column(name = "usuario_id")
    private String usuarioId; // ID del usuario que creó el objetivo
    
    @OneToMany(mappedBy = "objetivo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegistroProgreso> registrosProgreso = new ArrayList<>();

    // Enum para el estado del objetivo
    public enum EstadoObjetivo {
        ACTIVO, COMPLETADO, PAUSADO, CANCELADO
    }

    // Constructores
    public Objetivo() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoObjetivo.ACTIVO;
    }

    public Objetivo(String titulo, String descripcion, LocalDateTime fechaLimite, String usuarioId) {
        this();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.usuarioId = usuarioId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoObjetivo getEstado() {
        return estado;
    }

    public void setEstado(EstadoObjetivo estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDateTime fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Integer getProgreso() {
        return progreso;
    }

    public void setProgreso(Integer progreso) {
        this.progreso = Math.max(0, Math.min(100, progreso)); // Clamp entre 0 y 100
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<RegistroProgreso> getRegistrosProgreso() {
        return registrosProgreso;
    }

    public void setRegistrosProgreso(List<RegistroProgreso> registrosProgreso) {
        this.registrosProgreso = registrosProgreso;
    }

    // Métodos de utilidad
    public boolean estaCompletado() {
        return estado == EstadoObjetivo.COMPLETADO || progreso >= 100;
    }

    public boolean estaVencido() {
        return fechaLimite != null && LocalDateTime.now().isAfter(fechaLimite) && !estaCompletado();
    }

    public void actualizarProgreso(int nuevoProgreso) {
        setProgreso(nuevoProgreso);
        if (nuevoProgreso >= 100) {
            setEstado(EstadoObjetivo.COMPLETADO);
        }
    }

    @Override
    public String toString() {
        return "Objetivo{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", estado=" + estado +
                ", progreso=" + progreso +
                '}';
    }
}
