package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "seguimiento_habitos")
public class SeguimientoHabito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habito_id", nullable = false)
    private Habito habito;
    
    @Column(name = "fecha")
    private LocalDate fecha;
    
    @Column(name = "completado")
    private Integer completado = 0; // Número de veces completado en el día
    
    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    // Constructores
    public SeguimientoHabito() {
        this.fechaRegistro = LocalDateTime.now();
        this.fecha = LocalDate.now();
    }

    public SeguimientoHabito(Habito habito, LocalDate fecha, Integer completado, String notas) {
        this();
        this.habito = habito;
        this.fecha = fecha;
        this.completado = completado;
        this.notas = notas;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Habito getHabito() {
        return habito;
    }

    public void setHabito(Habito habito) {
        this.habito = habito;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getCompletado() {
        return completado;
    }

    public void setCompletado(Integer completado) {
        this.completado = Math.max(0, completado); // No puede ser negativo
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // Métodos de utilidad
    public boolean cumplioMeta() {
        return habito != null && completado >= habito.getMetaDiaria();
    }

    public double getPorcentajeMeta() {
        if (habito == null || habito.getMetaDiaria() == 0) {
            return 0.0;
        }
        return Math.min(100.0, (completado * 100.0) / habito.getMetaDiaria());
    }

    public void incrementarCompletado() {
        this.completado++;
    }

    public void decrementarCompletado() {
        this.completado = Math.max(0, this.completado - 1);
    }

    @Override
    public String toString() {
        return "SeguimientoHabito{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", completado=" + completado +
                ", cumplioMeta=" + cumplioMeta() +
                '}';
    }
}
