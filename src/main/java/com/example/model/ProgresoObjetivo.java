package com.example.model;

/**
 * Clase ProgresoObjetivo según el diagrama de clases
 */
public class ProgresoObjetivo {
    
    private Float porcentaje;
    private String estado;
    
    // Constructores
    public ProgresoObjetivo() {}
    
    public ProgresoObjetivo(Float porcentaje, String estado) {
        this.porcentaje = porcentaje;
        this.estado = estado;
    }
    
    // Métodos de negocio
    public void consultarProgreso() {
        // Lógica para consultar progreso
    }
    
    // Getters y Setters
    public Float getPorcentaje() {
        return porcentaje;
    }
    
    public void setPorcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "ProgresoObjetivo{" +
                "porcentaje=" + porcentaje +
                ", estado='" + estado + '\'' +
                '}';
    }
}
