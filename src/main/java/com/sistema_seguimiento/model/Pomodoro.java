package com.sistema_seguimiento.model;

import jakarta.persistence.*;

/**
 * Pomodoro - Entidad que gestiona la lógica de un temporizador Pomodoro.
 * Controla transiciones entre estados: Concentración, Descanso Corto, Descanso Largo.
 * 
 * Duración de sesiones:
 * - Concentración: 25 minutos (1500 segundos)
 * - Descanso Corto: 5 minutos (300 segundos)
 * - Descanso Largo: 15 minutos (900 segundos)
 */
@Entity
@Table(name = "pomodoro")
public class Pomodoro {

    public enum EstadoTemporizador {
        CONCENTRACION,
        DESCANSO_CORTO,
        DESCANSO_LARGO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoTemporizador estado;

    private Integer tiempoRestanteSegundos;

    // Constantes de duraciones (en segundos)
    @SuppressWarnings("unused")
    private static final int DURACION_CONCENTRACION = 1500;      // 25 minutos
    @SuppressWarnings("unused")
    private static final int DURACION_DESCANSO_CORTO = 300;       // 5 minutos
    @SuppressWarnings("unused")
    private static final int DURACION_DESCANSO_LARGO = 900;       // 15 minutos (para futura iteración 3)

    /**
     * Constructor por defecto. Inicializa el temporizador en estado CONCENTRACION
     * con 25 minutos (1500 segundos).
     */
    public Pomodoro() {
        this.estado = EstadoTemporizador.CONCENTRACION;
        this.tiempoRestanteSegundos = DURACION_CONCENTRACION;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoTemporizador getEstado() {
        return estado;
    }

    public void setEstado(EstadoTemporizador estado) {
        this.estado = estado;
    }

    public Integer getTiempoRestanteSegundos() {
        return tiempoRestanteSegundos;
    }

    public void setTiempoRestanteSegundos(Integer tiempoRestanteSegundos) {
        this.tiempoRestanteSegundos = tiempoRestanteSegundos;
    }

    /**
     * Finaliza la sesión actual y transiciona al siguiente estado.
     * - CONCENTRACION -> DESCANSO_CORTO (5 minutos)
     * - DESCANSO_CORTO -> CONCENTRACION (25 minutos)
     * - DESCANSO_LARGO -> CONCENTRACION (25 minutos)
     */
public void finalizarSesion() {
    transicionarAlSiguienteEstado();
}

private void transicionarAlSiguienteEstado() {
    switch (this.estado) {
        case CONCENTRACION:
            this.estado = EstadoTemporizador.DESCANSO_CORTO;
            this.tiempoRestanteSegundos = DURACION_DESCANSO_CORTO;
            break;
        case DESCANSO_CORTO:
        case DESCANSO_LARGO:
            this.estado = EstadoTemporizador.CONCENTRACION;
            this.tiempoRestanteSegundos = DURACION_CONCENTRACION;
            break;
    }
}
}
