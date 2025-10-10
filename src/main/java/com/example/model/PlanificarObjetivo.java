package com.example.model;

import java.util.Date;

/**
 * Clase PlanificarObjetivo según el diagrama de clases
 */
public class PlanificarObjetivo {
    
    private String estrategia;
    private String frecuencia;
    private Integer tiempoNecesario;
    private Date fechaCreacion;
    
    // Constructores
    public PlanificarObjetivo() {
        this.fechaCreacion = new Date();
    }
    
    public PlanificarObjetivo(String estrategia, String frecuencia, Integer tiempoNecesario) {
        this();
        this.estrategia = estrategia;
        this.frecuencia = frecuencia;
        this.tiempoNecesario = tiempoNecesario;
    }
    
    // Métodos de negocio
    public RegistroProgreso agregarRegistro(RegistroProgreso registro) {
        // Lógica para agregar registro
        return registro;
    }
    
    public int calcularRachaActual() {
        // Lógica para calcular racha actual
        return 0;
    }
    
    public void organizarPlan() {
        // Lógica para organizar el plan
    }
    
    // Getters y Setters
    public String getEstrategia() {
        return estrategia;
    }
    
    public void setEstrategia(String estrategia) {
        this.estrategia = estrategia;
    }
    
    public String getFrecuencia() {
        return frecuencia;
    }
    
    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }
    
    public Integer getTiempoNecesario() {
        return tiempoNecesario;
    }
    
    public void setTiempoNecesario(Integer tiempoNecesario) {
        this.tiempoNecesario = tiempoNecesario;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    @Override
    public String toString() {
        return "PlanificarObjetivo{" +
                "estrategia='" + estrategia + '\'' +
                ", frecuencia='" + frecuencia + '\'' +
                ", tiempoNecesario=" + tiempoNecesario +
                '}';
    }
}
