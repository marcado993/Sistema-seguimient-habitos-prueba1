package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una tarea (hábito) colocada en el Kanban
 * Guarda la posición, estado y franja horaria de cada hábito en el calendario
 */
@Entity
@Table(name = "tarea_kanban")
public class TareaKanban {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;
    
    @Column(name = "habito_id", nullable = false)
    private Long habitoId;
    
    @Column(name = "time_slot", nullable = false)
    private String timeSlot; // morning, afternoon, evening, night
    
    @Column(nullable = false)
    private String status; // todo, progress, done
    
    @Column(nullable = false)
    private String time; // Hora específica: "09:00"
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public TareaKanban() {
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public TareaKanban(String usuarioId, Long habitoId, String timeSlot, String status, String time) {
        this.usuarioId = usuarioId;
        this.habitoId = habitoId;
        this.timeSlot = timeSlot;
        this.status = status;
        this.time = time;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Long getHabitoId() {
        return habitoId;
    }
    
    public void setHabitoId(Long habitoId) {
        this.habitoId = habitoId;
    }
    
    public String getTimeSlot() {
        return timeSlot;
    }
    
    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    @Override
    public String toString() {
        return "TareaKanban{" +
                "id=" + id +
                ", usuarioId='" + usuarioId + '\'' +
                ", habitoId=" + habitoId +
                ", timeSlot='" + timeSlot + '\'' +
                ", status='" + status + '\'' +
                ", time='" + time + '\'' +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}
