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
 * CONCENTRACION al estado DESCANSO_CORTO con 300 segundos (5 minutos).
 * 
 * Ciclo TDD: [ROJO] -> [VERDE] -> [REFACTOR]
 */
public class PomodoroIteracion2Test {

    private Pomodoro pomodoro;

    @Before
    public void setUp() {
        pomodoro = new Pomodoro();
    }

    /**
     * [ROJO] Requisito: Si el Pomodoro está en estado 'CONCENTRACION' y se invoca 
     * el método finalizarSesion(), el estado debe cambiar a 'DESCANSO_CORTO' y el 
     * tiempo restante debe ser 300 segundos (5 minutos).
     * 
     * Narrativa BDD:
     * DADO un Pomodoro en estado CONCENTRACION
     * CUANDO se invoca el método finalizarSesion()
     * ENTONCES el estado debe cambiar a DESCANSO_CORTO y el tiempo debe ser 300 segundos
     */
    @Test
    public void give_pomodoroEnConcentracion_when_finalizarSesion_then_transicionaADescansoCortoY300Segundos() {
        // Arrange
       assertEquals("El Pomodoro debe estar en estado CONCENTRACION inicial",
               Pomodoro.EstadoTemporizador.CONCENTRACION,
                     pomodoro.getEstado());
        assertEquals("El tiempo restante debe ser 1500 segundos",
                     Integer.valueOf(1500),
                     pomodoro.getTiempoRestanteSegundos());

        // Act
        pomodoro.finalizarSesion();

        // Assert
        assertEquals("Después de finalizar, el estado debe ser DESCANSO_CORTO",
                     Pomodoro.EstadoTemporizador.DESCANSO_CORTO,
                     pomodoro.getEstado());
        assertEquals("Después de finalizar, el tiempo restante debe ser 300 segundos (5 minutos)",
                     Integer.valueOf(300),
                     pomodoro.getTiempoRestanteSegundos());
    }

    /**
     * [BONUS] Test adicional: Transición de Descanso Corto a Concentración.
     * 
     * Narrativa BDD:
     * DADO un Pomodoro que ha completado una sesión de CONCENTRACION
     * CUANDO se encuentra en estado DESCANSO_CORTO y se invoca finalizarSesion()
     * ENTONCES debe retornar a CONCENTRACION con 1500 segundos
     */
    @Test
    public void give_pomodoroEnDescansoCorto_when_finalizarSesion_then_transicionaAConcentracionY1500Segundos() {
        // Arrange: llevar al Pomodoro a estado DESCANSO_CORTO
        pomodoro.finalizarSesion();
        assertEquals("El Pomodoro debe estar en DESCANSO_CORTO",
                     Pomodoro.EstadoTemporizador.DESCANSO_CORTO,
                     pomodoro.getEstado());

        // Act
        pomodoro.finalizarSesion();

        // Assert
        assertEquals("Después de finalizar descanso corto, debe volver a CONCENTRACION",
                     Pomodoro.EstadoTemporizador.CONCENTRACION,
                     pomodoro.getEstado());
        assertEquals("El tiempo restante debe ser 1500 segundos nuevamente",
                     Integer.valueOf(1500),
                     pomodoro.getTiempoRestanteSegundos());
    }

    /**
     * [BONUS] Test adicional: Múltiples ciclos completos.
     * 
     * Narrativa BDD:
     * DADO un Pomodoro configurado
     * CUANDO se ejecutan múltiples ciclos de trabajo y descanso
     * ENTONCES cada ciclo debe transicionar correctamente y mantener los tiempos adecuados
     */
    @Test
    public void give_pomodoro_when_multipleCiclos_then_ciclosCorrectos() {
        // Ciclo 1: Concentración
        assertEquals(Pomodoro.EstadoTemporizador.CONCENTRACION, pomodoro.getEstado());
        assertEquals(Integer.valueOf(1500), pomodoro.getTiempoRestanteSegundos());

        // Ciclo 1: Descanso Corto
        pomodoro.finalizarSesion();
        assertEquals(Pomodoro.EstadoTemporizador.DESCANSO_CORTO, pomodoro.getEstado());
        assertEquals(Integer.valueOf(300), pomodoro.getTiempoRestanteSegundos());

        // Ciclo 2: Concentración nuevamente
        pomodoro.finalizarSesion();
        assertEquals(Pomodoro.EstadoTemporizador.CONCENTRACION, pomodoro.getEstado());
        assertEquals(Integer.valueOf(1500), pomodoro.getTiempoRestanteSegundos());

        // Ciclo 2: Descanso Corto nuevamente
        pomodoro.finalizarSesion();
        assertEquals(Pomodoro.EstadoTemporizador.DESCANSO_CORTO, pomodoro.getEstado());
        assertEquals(Integer.valueOf(300), pomodoro.getTiempoRestanteSegundos());
    }
}
