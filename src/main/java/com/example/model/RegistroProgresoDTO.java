package com.example.model;

import java.time.LocalDate;

/**
 * DTO para transferencia de datos de RegistroProgreso
 * Basado en el diagrama de clases
 */
public class RegistroProgresoDTO {
    
    private LocalDate fecha;
    private String estado;
    private String observacion;
    private String estadoAnimo;
    private boolean cumplido;
    
    // Constructores
    public RegistroProgresoDTO() {}
    
    public RegistroProgresoDTO(LocalDate fecha, String estado, String observacion, String estadoAnimo, boolean cumplido) {
        this.fecha = fecha;
        this.estado = estado;
        this.observacion = observacion;
        this.estadoAnimo = estadoAnimo;
        this.cumplido = cumplido;
    }
    
    // Getters y Setters
    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
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
    
    public boolean isCumplido() {
        return cumplido;
    }
    
    public void setCumplido(boolean cumplido) {
        this.cumplido = cumplido;
    }
    
    @Override
    public String toString() {
        return "RegistroProgresoDTO{" +
                "fecha=" + fecha +
                ", estado='" + estado + '\'' +
                ", cumplido=" + cumplido +
                '}';
    }
}
