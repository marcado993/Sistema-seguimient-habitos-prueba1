package com.sistema_seguimiento.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "habitos")
public class Habito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "frecuencia")
    private FrecuenciaHabito frecuencia;
    
    @Column(name = "meta_diaria")
    private Integer metaDiaria; // Número de veces por día
    
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "usuario_id")
    private String usuarioId;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objetivo_id")
    private Objetivo objetivo;
    
    @OneToMany(mappedBy = "habito", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegistroHabito> registros = new ArrayList<>();

    // Enum para la frecuencia del hábito
    public enum FrecuenciaHabito {
        DIARIO, SEMANAL, MENSUAL
    }

    // Constructores
    public Habito() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaInicio = LocalDate.now();
        this.activo = true;
    }

    public Habito(String nombre, String descripcion, FrecuenciaHabito frecuencia, Integer metaDiaria, String usuarioId) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.frecuencia = frecuencia;
        this.metaDiaria = metaDiaria;
        this.usuarioId = usuarioId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public FrecuenciaHabito getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(FrecuenciaHabito frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Integer getMetaDiaria() {
        return metaDiaria;
    }

    public void setMetaDiaria(Integer metaDiaria) {
        this.metaDiaria = metaDiaria;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<RegistroHabito> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroHabito> registros) {
        this.registros = registros;
    }
    
    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    // Métodos de utilidad
    
    /**
     * Método para agregar un registro según el diagrama
     */
    public void agregarRegistro(RegistroHabito registro) {
        if (registro != null) {
            registro.setHabito(this);
            this.registros.add(registro);
        }
    }
    
    /**
     * Método para calcular racha actual según el diagrama
     */
    public int calcularRachaActual() {
        return getDiasConsecutivos();
    }
    
    public boolean tieneMetaDelDia(LocalDate fecha) {
        if (metaDiaria == null) return false;
        return registros.stream()
                .anyMatch(r -> r.getFecha().equals(fecha) && r.getCompletado() >= metaDiaria);
    }

    public int getDiasConsecutivos() {
        int dias = 0;
        LocalDate fecha = LocalDate.now();
        
        while (tieneMetaDelDia(fecha)) {
            dias++;
            fecha = fecha.minusDays(1);
        }
        
        return dias;
    }

    public double getPorcentajeCompletado(LocalDate desde, LocalDate hasta) {
        if (metaDiaria == null) return 0.0;
        long totalDias = desde.datesUntil(hasta.plusDays(1)).count();
        long diasCompletados = registros.stream()
                .filter(r -> !r.getFecha().isBefore(desde) && !r.getFecha().isAfter(hasta))
                .filter(r -> r.getCompletado() >= metaDiaria)
                .count();
        
        return totalDias > 0 ? (diasCompletados * 100.0) / totalDias : 0.0;
    }

    @Override
    public String toString() {
        return "Habito{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", frecuencia=" + frecuencia +
                ", metaDiaria=" + metaDiaria +
                ", activo=" + activo +
                '}';
    }
}
