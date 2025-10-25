package com.sistema_seguimiento.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "objetivo")
public class Objetivo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoObjetivo estado;
    
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "progreso_actual")
    private Integer progresoActual = 0;
    
    @Column(name = "meta")
    private Integer meta = 100;
    
    @Column(name = "usuario_id")
    private Integer usuarioId;
    
    @OneToMany(mappedBy = "objetivo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegistroProgreso> registrosProgreso = new ArrayList<>();

    // Enum para el estado del objetivo
    public enum EstadoObjetivo {
        PENDIENTE, EN_PROGRESO, COMPLETADO, PAUSADO, CANCELADO
    }

    // Constructores
    public Objetivo() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.estado = EstadoObjetivo.PENDIENTE;
        this.progresoActual = 0;
        this.meta = 100;
    }

    public Objetivo(String titulo, String descripcion, Integer usuarioId) {
        this();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.usuarioId = usuarioId;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Integer getProgresoActual() {
        return progresoActual;
    }

    public void setProgresoActual(Integer progresoActual) {
        this.progresoActual = Math.max(0, Math.min(100, progresoActual)); // Clamp entre 0 y 100
    }
    
    public Integer getMeta() {
        return meta;
    }

    public void setMeta(Integer meta) {
        this.meta = meta;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
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
        return estado == EstadoObjetivo.COMPLETADO || progresoActual >= 100;
    }

    public void actualizarProgreso(int nuevoProgreso) {
        setProgresoActual(nuevoProgreso);
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
                ", progresoActual=" + progresoActual +
                '}';
    }
}
