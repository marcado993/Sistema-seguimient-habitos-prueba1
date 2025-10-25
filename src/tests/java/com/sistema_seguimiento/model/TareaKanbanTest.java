package com.sistema_seguimiento.model;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.Assert.*;

public class TareaKanbanTest {

    @Test
    public void given_habitoAsociado_when_creaTareaNueva_then_estadoInicialPendiente(){

        TareaKanban nuevaTarea = new TareaKanban("Diseñar la vista del kanban");

        assertNotNull(nuevaTarea);
        assertEquals(TareaKanban.EstadoKanban.POR_HACER, nuevaTarea.getEstado());
    }

    private static Stream<Arguments> proovedorDeMovimientosKanban(){
        return Stream.of(
//          Movimientos VÁLIDOS
                Arguments.of(TareaKanban.EstadoKanban.POR_HACER, TareaKanban.EstadoKanban.EN_PROGRESO, true),
                Arguments.of(TareaKanban.EstadoKanban.EN_PROGRESO, TareaKanban.EstadoKanban.COMPLETADO, true),
                Arguments.of(TareaKanban.EstadoKanban.EN_PROGRESO, TareaKanban.EstadoKanban.POR_HACER, true),

//          Movimientos INVÁLIDOS
                Arguments.of(TareaKanban.EstadoKanban.POR_HACER, TareaKanban.EstadoKanban.COMPLETADO, false),
                Arguments.of(TareaKanban.EstadoKanban.COMPLETADO, TareaKanban.EstadoKanban.EN_PROGRESO, false)
        );
    }


    @ParameterizedTest(name = "Mover de {0} a {1} debería ser {2}")
    @MethodSource("proovedorDeMovimientosKanban")
    public void given_estadoActual_when_seMueveLaTarea_then_nuevoEstadoValido(TareaKanban.EstadoKanban estadoInicial, TareaKanban.EstadoKanban estadoNuevo, boolean movimientoEsperado){
        TareaKanban tarea = new TareaKanban("Mi tarea");
        tarea.setEstado(estadoInicial);

        boolean resultadoMovimiento = tarea.mover(estadoNuevo);

        assertEquals(movimientoEsperado, resultadoMovimiento);

        if (movimientoEsperado){
            assertEquals(estadoNuevo, tarea.getEstado());
        } else {
            assertEquals(estadoInicial, tarea.getEstado());
        }


    }
}