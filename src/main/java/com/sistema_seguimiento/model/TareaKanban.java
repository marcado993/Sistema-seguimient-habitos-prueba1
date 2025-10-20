package com.sistema_seguimiento.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tareas_kanban")
public class TareaKanban {

    public enum EstadoKanban {
        PENDIENTE,
        EN_PROGRESO,
        COMPLETADO
    }
    private String titulo;

    @Enumerated(EnumType.STRING)
    private EstadoKanban estado;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TareaKanban(){

    }

    public TareaKanban(String titulo) {
        this.titulo = titulo;
        this.estado = EstadoKanban.PENDIENTE;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setEstado(EstadoKanban estado) {
        this.estado = estado;
    }

    public EstadoKanban getEstado() {
        return estado;
    }

    public boolean mover(EstadoKanban estadoNuevo) {
        switch (this.estado){
            case PENDIENTE :
                if (estadoNuevo == EstadoKanban.EN_PROGRESO){
                    this.estado = estadoNuevo;
                    return true;
                }
                break;
            case EN_PROGRESO:
                if (estadoNuevo == EstadoKanban.PENDIENTE || estadoNuevo == EstadoKanban.COMPLETADO){
                    this.estado = estadoNuevo;
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
