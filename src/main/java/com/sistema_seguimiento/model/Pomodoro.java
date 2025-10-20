package com.sistema_seguimiento.model;

import java.time.LocalDateTime;

/**
 * Modelo Pomodoro - Timer de técnica Pomodoro
 * 
 * La técnica Pomodoro consiste en:
 * - 25 minutos de trabajo concentrado
 * - 5 minutos de descanso corto
 * - Después de 4 pomodoros, descanso largo de 15-30 minutos
 * 
 * Estados: TRABAJO, DESCANSO_CORTO, DESCANSO_LARGO, PAUSADO, COMPLETADO
 */
public class Pomodoro {
    
    // Enum para estados del Pomodoro
    public enum EstadoPomodoro {
        TRABAJO,           // Período de trabajo (25 min)
        DESCANSO_CORTO,    // Descanso corto (5 min)
        DESCANSO_LARGO,    // Descanso largo (15-30 min)
        PAUSADO,           // Timer pausado
        COMPLETADO         // Sesión completada
    }
    
    // Constantes de tiempo (en minutos)
    public static final int DURACION_TRABAJO = 25;
    public static final int DURACION_DESCANSO_CORTO = 5;
    public static final int DURACION_DESCANSO_LARGO = 15;
    public static final int POMODOROS_ANTES_DESCANSO_LARGO = 4;
    
    // Atributos
    private Long id;
    private EstadoPomodoro estadoActual;
    private int tiempoRestanteMinutos;
    private int pomodorosCompletados;
    private LocalDateTime inicioSesion;
    private LocalDateTime ultimaActualizacion;
    private String usuarioId;
    
    // Constructores
    public Pomodoro() {
        this.estadoActual = EstadoPomodoro.TRABAJO;
        this.tiempoRestanteMinutos = DURACION_TRABAJO;
        this.pomodorosCompletados = 0;
        this.inicioSesion = LocalDateTime.now();
        this.ultimaActualizacion = LocalDateTime.now();
    }
    
    public Pomodoro(String usuarioId) {
        this();
        this.usuarioId = usuarioId;
    }
    
    /**
     * Simula el paso del tiempo y realiza transiciones de estado
     * 
     * @param minutosTranscurridos Minutos que han pasado
     * @return true si hubo cambio de estado, false si no
     */
    public boolean pasarTiempo(int minutosTranscurridos) {
        if (estadoActual == EstadoPomodoro.PAUSADO || estadoActual == EstadoPomodoro.COMPLETADO) {
            return false;
        }
        
        tiempoRestanteMinutos -= minutosTranscurridos;
        ultimaActualizacion = LocalDateTime.now();
        
        // Si el tiempo llegó a cero o menos, hacer transición
        if (tiempoRestanteMinutos <= 0) {
            return realizarTransicion();
        }
        
        return false;
    }
    
    /**
     * Realiza la transición al siguiente estado según la lógica Pomodoro
     * 
     * @return true si hubo transición exitosa
     * 
     * � FASE VERDE - Lógica implementada para pasar los tests
     */
    private boolean realizarTransicion() {
        EstadoPomodoro estadoAnterior = estadoActual;
        
        switch (estadoActual) {
            case TRABAJO:
                pomodorosCompletados++;
                
                // Después de 4 pomodoros, descanso largo
                if (pomodorosCompletados % POMODOROS_ANTES_DESCANSO_LARGO == 0) {
                    estadoActual = EstadoPomodoro.DESCANSO_LARGO;
                    tiempoRestanteMinutos = DURACION_DESCANSO_LARGO;
                } else {
                    estadoActual = EstadoPomodoro.DESCANSO_CORTO;
                    tiempoRestanteMinutos = DURACION_DESCANSO_CORTO;
                }
                break;
                
            case DESCANSO_CORTO:
            case DESCANSO_LARGO:
                estadoActual = EstadoPomodoro.TRABAJO;
                tiempoRestanteMinutos = DURACION_TRABAJO;
                break;
                
            default:
                return false;
        }
        
        return estadoActual != estadoAnterior;
    }
    
    /**
     * Pausa el timer
     */
    public void pausar() {
        if (estadoActual != EstadoPomodoro.COMPLETADO) {
            estadoActual = EstadoPomodoro.PAUSADO;
            ultimaActualizacion = LocalDateTime.now();
        }
    }
    
    /**
     * Reanuda el timer desde pausa
     */
    public void reanudar() {
        if (estadoActual == EstadoPomodoro.PAUSADO) {
            // Volver al estado anterior (se podría guardar en un campo)
            // Por ahora, siempre vuelve a TRABAJO
            estadoActual = EstadoPomodoro.TRABAJO;
            ultimaActualizacion = LocalDateTime.now();
        }
    }
    
    /**
     * Reinicia el Pomodoro al estado inicial
     */
    public void reiniciar() {
        estadoActual = EstadoPomodoro.TRABAJO;
        tiempoRestanteMinutos = DURACION_TRABAJO;
        pomodorosCompletados = 0;
        inicioSesion = LocalDateTime.now();
        ultimaActualizacion = LocalDateTime.now();
    }
    
    /**
     * Completa la sesión de Pomodoro
     */
    public void completar() {
        estadoActual = EstadoPomodoro.COMPLETADO;
        ultimaActualizacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public EstadoPomodoro getEstadoActual() {
        return estadoActual;
    }
    
    public void setEstadoActual(EstadoPomodoro estadoActual) {
        this.estadoActual = estadoActual;
    }
    
    public int getTiempoRestanteMinutos() {
        return tiempoRestanteMinutos;
    }
    
    public void setTiempoRestanteMinutos(int tiempoRestanteMinutos) {
        this.tiempoRestanteMinutos = tiempoRestanteMinutos;
    }
    
    public int getPomodorosCompletados() {
        return pomodorosCompletados;
    }
    
    public void setPomodorosCompletados(int pomodorosCompletados) {
        this.pomodorosCompletados = pomodorosCompletados;
    }
    
    public LocalDateTime getInicioSesion() {
        return inicioSesion;
    }
    
    public void setInicioSesion(LocalDateTime inicioSesion) {
        this.inicioSesion = inicioSesion;
    }
    
    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }
    
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
    
    public String getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    // Métodos de utilidad
    public boolean estaActivo() {
        return estadoActual != EstadoPomodoro.PAUSADO && 
               estadoActual != EstadoPomodoro.COMPLETADO;
    }
    
    public boolean estaEnTrabajo() {
        return estadoActual == EstadoPomodoro.TRABAJO;
    }
    
    public boolean estaEnDescanso() {
        return estadoActual == EstadoPomodoro.DESCANSO_CORTO || 
               estadoActual == EstadoPomodoro.DESCANSO_LARGO;
    }
    
    public int getPorcentajeProgreso() {
        int duracionTotal = getDuracionEstadoActual();
        if (duracionTotal == 0) return 0;
        
        int tiempoTranscurrido = duracionTotal - tiempoRestanteMinutos;
        return (tiempoTranscurrido * 100) / duracionTotal;
    }
    
    private int getDuracionEstadoActual() {
        switch (estadoActual) {
            case TRABAJO:
                return DURACION_TRABAJO;
            case DESCANSO_CORTO:
                return DURACION_DESCANSO_CORTO;
            case DESCANSO_LARGO:
                return DURACION_DESCANSO_LARGO;
            default:
                return 0;
        }
    }
    
    @Override
    public String toString() {
        return "Pomodoro{" +
                "estado=" + estadoActual +
                ", tiempoRestante=" + tiempoRestanteMinutos + " min" +
                ", pomodorosCompletados=" + pomodorosCompletados +
                ", progreso=" + getPorcentajeProgreso() + "%" +
                '}';
    }
}
