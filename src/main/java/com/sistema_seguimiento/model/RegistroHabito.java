package com.sistema_seguimiento.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registro_habito")
public class RegistroHabito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habito_id", nullable = false)
    private Habito habito;
    
    @Column(name = "fecha_registro", nullable = false)  // ✅ Corregido: fecha_registro
    private LocalDate fecha;
    
    @Column(name = "completado")
    private Boolean completado; // ✅ Corregido: Boolean en lugar de Integer
    
    @Column(name = "veces_realizado")  // ✅ Corregido: veces_realizado
    private Integer vecesRealizado;
    
    @Column(name = "notas", columnDefinition = "TEXT")  // ✅ Corregido: notas en lugar de observacion
    private String notas;
    
    @Column(name = "estado_animo")  // ✅ NUEVO: Estado de ánimo al registrar cumplimiento
    private String estadoAnimo;
    
    @Column(name = "fecha_creacion")
    private java.time.LocalDateTime fechaCreacion;
    
    // Constructores
    public RegistroHabito() {
        this.fecha = LocalDate.now();
        this.completado = false;  // ✅ Boolean false
        this.vecesRealizado = 0;
        this.estadoAnimo = "neutral";  // ✅ Default
    }

    public RegistroHabito(Habito habito, LocalDate fecha, Boolean completado, String notas) {
        this.habito = habito;
        this.fecha = fecha;
        this.completado = completado;
        this.notas = notas;
        this.vecesRealizado = 0;
        this.estadoAnimo = "neutral";  // ✅ Default
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Boolean getCompletado() {  // ✅ Cambiado a Boolean
        return completado;
    }

    public void setCompletado(Boolean completado) {  // ✅ Cambiado a Boolean
        this.completado = completado;
    }
    
    public Integer getVecesRealizado() {  // ✅ Nuevo getter
        return vecesRealizado;
    }
    
    public void setVecesRealizado(Integer vecesRealizado) {  // ✅ Nuevo setter
        this.vecesRealizado = vecesRealizado;
    }

    public String getNotas() {  // ✅ Cambiado de getObservacion a getNotas
        return notas;
    }

    public void setNotas(String notas) {  // ✅ Cambiado de setObservacion a setNotas
        this.notas = notas;
    }
    
    public java.time.LocalDateTime getFechaCreacion() {  // ✅ Nuevo getter
        return fechaCreacion;
    }
    
    public void setFechaCreacion(java.time.LocalDateTime fechaCreacion) {  // ✅ Nuevo setter
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getEstadoAnimo() {  // ✅ NUEVO
        return estadoAnimo;
    }
    
    public void setEstadoAnimo(String estadoAnimo) {  // ✅ NUEVO
        this.estadoAnimo = estadoAnimo;
    }

    @Override
    public String toString() {
        return "RegistroHabito{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", completado=" + completado +
                ", vecesRealizado=" + vecesRealizado +
                ", notas='" + notas + '\'' +
                '}';
    }
}
