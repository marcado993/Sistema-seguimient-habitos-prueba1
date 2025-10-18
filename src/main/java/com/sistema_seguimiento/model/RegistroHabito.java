package com.sistema_seguimiento.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registros_habito")
public class RegistroHabito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habito_id", nullable = false)
    private Habito habito;
    
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    
    @Column(name = "completado")
    private Integer completado; // Número de veces completado ese día
    
    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;
    
    @Column(name = "estado_animo")
    private String estadoAnimo;
    
    // Constructores
    public RegistroHabito() {
        this.fecha = LocalDate.now();
        this.completado = 0;
    }

    public RegistroHabito(Habito habito, LocalDate fecha, Integer completado, String observacion) {
        this.habito = habito;
        this.fecha = fecha;
        this.completado = completado;
        this.observacion = observacion;
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
        this.completado = completado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstadoAnimo() {
        return estadoAnimo;
    }

    public void setEstadoAnimo(String estadoAnimo) {
        this.estadoAnimo = estadoAnimo;
    }

    @Override
    public String toString() {
        return "RegistroHabito{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", completado=" + completado +
                ", observacion='" + observacion + '\'' +
                '}';
    }
}
