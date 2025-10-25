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
 * en estado TRABAJO con 25 minutos restantes.
 * 
 * Ciclo TDD: [ROJO] -> [VERDE] -> [REFACTOR]
 * 
 * NOTE: Updated to use current EstadoPomodoro enum and getTiempoRestanteMinutos() method
 */
public class PomodoroIteracion1Test {

    @Before
    public void setUp() {
        // Setup for tests
    }

    /**
     * Requisito: Al inicializar un nuevo pomodoro, el estado debe ser 
     * 'TRABAJO' y el tiempo restante debe ser 25 minutos.
     * 
     * Narrativa BDD:
     * DADO un nuevo temporizador Pomodoro
     * CUANDO se inicializa
     * ENTONCES el estado debe ser TRABAJO y el tiempo debe ser 25 minutos
     */
    @Test
    public void give_nuevoPomodoro_when_inicializar_then_estadoTrabajoY25Minutos() {
        // Arrange & Act
        Pomodoro temp = new Pomodoro();

        // Assert
        assertEquals("El estado inicial debe ser TRABAJO", 
                     Pomodoro.EstadoPomodoro.TRABAJO, 
                     temp.getEstadoActual());
        assertEquals("El tiempo restante inicial debe ser 25 minutos", 
                     25, 
                     temp.getTiempoRestanteMinutos());
    }
}
