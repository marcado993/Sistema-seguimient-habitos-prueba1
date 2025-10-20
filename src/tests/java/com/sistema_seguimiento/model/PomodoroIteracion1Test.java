package com.sistema_seguimiento.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitarios para Pomodoro - ITERACIÓN 1.
 * 
 * ITERACIÓN 1: Inicialización y Estado
 * 
 * Objetivo: Verificar que el temporizador Pomodoro se inicializa correctamente
 * en estado CONCENTRACION con 1500 segundos (25 minutos).
 * 
 * Ciclo TDD: [ROJO] -> [VERDE] -> [REFACTOR]
 */
public class PomodoroIteracion1Test {

    private Pomodoro pomodoro;

    @Before
    public void setUp() {
        pomodoro = new Pomodoro();
    }

    /**
     * [ROJO] Requisito: Al inicializar un nuevo pomodoro, el estado debe ser 
     * 'CONCENTRACION' y el tiempo restante debe ser 1500 segundos (25 minutos).
     * 
     * Narrativa BDD:
     * DADO un nuevo temporizador Pomodoro
     * CUANDO se inicializa
     * ENTONCES el estado debe ser CONCENTRACION y el tiempo debe ser 1500 segundos
     */
    @Test
    public void give_nuevoPomodoro_when_inicializar_then_estadoConcentracionY1500Segundos() {
        // Arrange (Ya hecho en setUp)

        // Act
        Pomodoro temp = new Pomodoro();

        // Assert
        assertEquals("El estado inicial debe ser CONCENTRACION", 
                     Pomodoro.EstadoTemporizador.CONCENTRACION, 
                     temp.getEstado());
        assertEquals("El tiempo restante inicial debe ser 1500 segundos (25 minutos)", 
                     Integer.valueOf(1500), 
                     temp.getTiempoRestanteSegundos());
    }
}
