package com.sistema_seguimiento.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarea_kanban")
public class TareaKanban {

    public enum EstadoKanban {
        POR_HACER, EN_PROGRESO, COMPLETADO
    }
    
    public enum PrioridadKanban {
        BAJA, MEDIA, ALTA, URGENTE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "objetivo_id", nullable = false)
    private Integer objetivoId;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoKanban estado = EstadoKanban.POR_HACER;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad")
    private PrioridadKanban prioridad = PrioridadKanban.MEDIA;
    
    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;
    
    @Column(name = "orden")
    private Integer orden = 0;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;

    public TareaKanban(){
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoKanban.POR_HACER;
        this.prioridad = PrioridadKanban.MEDIA;
        this.orden = 0;
    }

    public TareaKanban(String titulo) {
        this();
        this.titulo = titulo;
    }
    
    public TareaKanban(String titulo, Integer objetivoId) {
        this();
        this.titulo = titulo;
        this.objetivoId = objetivoId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getObjetivoId() {
        return objetivoId;
    }

    public void setObjetivoId(Integer objetivoId) {
        this.objetivoId = objetivoId;
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

    public EstadoKanban getEstado() {
        return estado;
    }

    public void setEstado(EstadoKanban estado) {
        this.estado = estado;
    }
    
    public PrioridadKanban getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadKanban prioridad) {
        this.prioridad = prioridad;
    }
    
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
    
    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(LocalDateTime fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }

    public boolean mover(EstadoKanban estadoNuevo) {
        switch (this.estado){
            case POR_HACER :
                if (estadoNuevo == EstadoKanban.EN_PROGRESO){
                    this.estado = estadoNuevo;
                    return true;
                }
                break;
            case EN_PROGRESO:
                if (estadoNuevo == EstadoKanban.POR_HACER || estadoNuevo == EstadoKanban.COMPLETADO){
                    this.estado = estadoNuevo;
                    if (estadoNuevo == EstadoKanban.COMPLETADO) {
                        this.fechaCompletado = LocalDateTime.now();
                    }
                    return true;
                }
                break;
            case COMPLETADO:
                // No se puede mover desde COMPLETADO
                return false;
        }
        return false;
    }
}

