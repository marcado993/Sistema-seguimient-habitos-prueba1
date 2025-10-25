package com.sistema_seguimiento.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_progreso")
public class RegistroProgreso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objetivo_id", nullable = false)
    private Objetivo objetivo;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    
    @Column(name = "progreso_anterior")
    private Integer progresoAnterior;
    
    @Column(name = "progreso_nuevo")
    private Integer progresoNuevo;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    // Constructores
    public RegistroProgreso() {
        this.fechaRegistro = LocalDateTime.now();
    }

    public RegistroProgreso(Objetivo objetivo, Integer progresoAnterior, Integer progresoNuevo, String observaciones) {
        this();
        this.objetivo = objetivo;
        this.progresoAnterior = progresoAnterior;
        this.progresoNuevo = progresoNuevo;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getProgresoAnterior() {
        return progresoAnterior;
    }

    public void setProgresoAnterior(Integer progresoAnterior) {
        this.progresoAnterior = progresoAnterior;
    }

    public Integer getProgresoNuevo() {
        return progresoNuevo;
    }

    public void setProgresoNuevo(Integer progresoNuevo) {
        this.progresoNuevo = progresoNuevo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // Métodos de utilidad
    public Integer getDiferenciaProgreso() {
        return progresoNuevo - progresoAnterior;
    }

    @Override
    public String toString() {
        return "RegistroProgreso{" +
                "id=" + id +
                ", progresoAnterior=" + progresoAnterior +
                ", progresoNuevo=" + progresoNuevo +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
