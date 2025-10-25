package com.sistema_seguimiento.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitarios para Pomodoro - ITERACIÓN 2.
 * 
 * ITERACIÓN 2: Transición a Descanso Corto
 * 
 * Objetivo: Verificar que el Pomodoro transiciona correctamente del estado
 * TRABAJO al estado DESCANSO_CORTO con 5 minutos restantes.
 * 
 * Ciclo TDD: [ROJO] -> [VERDE] -> [REFACTOR]
 * 
 * NOTE: Updated to use current EstadoPomodoro enum and pasarTiempo() method
 */
public class PomodoroIteracion2Test {

    private Pomodoro pomodoro;

    @Before
    public void setUp() {
        pomodoro = new Pomodoro();
    }

    /**
     * Requisito: Si el Pomodoro está en estado 'TRABAJO' y se invoca 
     * el método pasarTiempo() para completar 25 minutos, el estado debe cambiar 
     * a 'DESCANSO_CORTO' y el tiempo restante debe ser 5 minutos.
     * 
     * Narrativa BDD:
     * DADO un Pomodoro en estado TRABAJO
     * CUANDO se invoca el método pasarTiempo() para completar los 25 minutos
     * ENTONCES el estado debe cambiar a DESCANSO_CORTO y el tiempo debe ser 5 minutos
     */
    @Test
    public void give_pomodoroEnTrabajo_when_pasarTiempo25Minutos_then_transicionaADescansoCorto() {
        // Arrange
        assertEquals("El Pomodoro debe estar en estado TRABAJO inicial",
                Pomodoro.EstadoPomodoro.TRABAJO,
                pomodoro.getEstadoActual());
        assertEquals("El tiempo restante debe ser 25 minutos",
                25,
                pomodoro.getTiempoRestanteMinutos());

        // Act - Simular que pasaron 25 minutos (completar sesión de trabajo)
        pomodoro.pasarTiempo(25);

        // Assert
        assertEquals("Después de completar trabajo, el estado debe ser DESCANSO_CORTO",
                Pomodoro.EstadoPomodoro.DESCANSO_CORTO,
                pomodoro.getEstadoActual());
        assertEquals("Después de completar, el tiempo restante debe ser 5 minutos (descanso corto)",
                5,
                pomodoro.getTiempoRestanteMinutos());
    }

    /**
     * Test adicional: Transición de Descanso Corto a Trabajo.
     * 
     * Narrativa BDD:
     * DADO un Pomodoro que ha completado una sesión de TRABAJO
     * CUANDO se encuentra en estado DESCANSO_CORTO y se invoca pasarTiempo(5)
     * ENTONCES debe retornar a TRABAJO con 25 minutos
     */
    @Test
    public void give_pomodoroEnDescansoCorto_when_pasarTiempo5Minutos_then_transicionaATrabajoY25Minutos() {
        // Arrange: llevar al Pomodoro a estado DESCANSO_CORTO
        pomodoro.pasarTiempo(25);
        assertEquals("El Pomodoro debe estar en DESCANSO_CORTO",
                Pomodoro.EstadoPomodoro.DESCANSO_CORTO,
                pomodoro.getEstadoActual());

        // Act - Completar el descanso corto (5 minutos)
        pomodoro.pasarTiempo(5);

        // Assert
        assertEquals("Después de completar descanso corto, debe volver a TRABAJO",
                Pomodoro.EstadoPomodoro.TRABAJO,
                pomodoro.getEstadoActual());
        assertEquals("El tiempo restante debe ser 25 minutos nuevamente",
                25,
                pomodoro.getTiempoRestanteMinutos());
    }

    /**
     * Test adicional: Múltiples ciclos completos.
     * 
     * Narrativa BDD:
     * DADO un Pomodoro configurado
     * CUANDO se ejecutan múltiples ciclos de trabajo y descanso
     * ENTONCES cada ciclo debe transicionar correctamente y mantener los tiempos adecuados
     */
    @Test
    public void give_pomodoro_when_multipleCiclos_then_ciclosCorrectos() {
        // Ciclo 1: Trabajo
        assertEquals(Pomodoro.EstadoPomodoro.TRABAJO, pomodoro.getEstadoActual());
        assertEquals(25, pomodoro.getTiempoRestanteMinutos());

        // Ciclo 1: Transición a Descanso Corto
        pomodoro.pasarTiempo(25);
        assertEquals(Pomodoro.EstadoPomodoro.DESCANSO_CORTO, pomodoro.getEstadoActual());
        assertEquals(5, pomodoro.getTiempoRestanteMinutos());

        // Ciclo 2: Transición de vuelta a Trabajo
        pomodoro.pasarTiempo(5);
        assertEquals(Pomodoro.EstadoPomodoro.TRABAJO, pomodoro.getEstadoActual());
        assertEquals(25, pomodoro.getTiempoRestanteMinutos());

        // Ciclo 2: Transición a Descanso Corto nuevamente
        pomodoro.pasarTiempo(25);
        assertEquals(Pomodoro.EstadoPomodoro.DESCANSO_CORTO, pomodoro.getEstadoActual());
        assertEquals(5, pomodoro.getTiempoRestanteMinutos());
    }
}
