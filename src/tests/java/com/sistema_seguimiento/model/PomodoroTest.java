package com.sistema_seguimiento.model;

import com.sistema_seguimiento.model.Pomodoro.EstadoPomodoro;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Test TDD Parametrizado para Pomodoro (Simplificado)
 * 
 * Foco: Lógica de transiciones básicas del timer Pomodoro
 * Método bajo prueba: pasarTiempo(int minutosTranscurridos)
 * 
 * Ciclo TDD: ROJO → VERDE → REFACTOR
 * 
 * Test Parametrizado: Prueba las 2 transiciones principales:
 * 1. TRABAJO → DESCANSO
 * 2. DESCANSO → TRABAJO
 */
@RunWith(Parameterized.class)
public class PomodoroTest {
    
    // Parámetros del test
    private final EstadoPomodoro estadoInicial;
    private final int pomodorosCompletadosInicial;
    private final int tiempoRestanteInicial;
    private final int minutosTranscurridos;
    private final EstadoPomodoro estadoEsperado;
    private final int tiempoRestanteEsperado;
    private final String descripcionEscenario;
    
    /**
     * Constructor que recibe los parámetros para cada test
     */
    public PomodoroTest(
            String descripcionEscenario,
            EstadoPomodoro estadoInicial,
            int pomodorosCompletadosInicial,
            int tiempoRestanteInicial,
            int minutosTranscurridos,
            EstadoPomodoro estadoEsperado,
            int tiempoRestanteEsperado) {
        
        this.descripcionEscenario = descripcionEscenario;
        this.estadoInicial = estadoInicial;
        this.pomodorosCompletadosInicial = pomodorosCompletadosInicial;
        this.tiempoRestanteInicial = tiempoRestanteInicial;
        this.minutosTranscurridos = minutosTranscurridos;
        this.estadoEsperado = estadoEsperado;
        this.tiempoRestanteEsperado = tiempoRestanteEsperado;
    }
    
    /**
     * Datos de prueba parametrizados
     * 
     * Formato: {descripción, estadoInicial, pomodorosCompletados, tiempoRestante, 
     *           minutosTranscurridos, estadoEsperado, tiempoRestanteEsperado}
     */
    @Parameters(name = "{index}: {0}")
    public static Collection<Object[]> datosParaTest() {
        return Arrays.asList(new Object[][] {
            // Escenario 1: Transición de TRABAJO a DESCANSO
            {
                "TRABAJO → DESCANSO_CORTO",
                EstadoPomodoro.TRABAJO,        // Estado inicial
                0,                             // Pomodoros completados inicial
                25,                            // Tiempo restante inicial (25 min)
                25,                            // Minutos transcurridos (completa el trabajo)
                EstadoPomodoro.DESCANSO_CORTO, // Estado esperado
                5                              // Tiempo restante esperado (5 min descanso)
            },
            
            // Escenario 2: Transición de DESCANSO a TRABAJO
            {
                "DESCANSO_CORTO → TRABAJO",
                EstadoPomodoro.DESCANSO_CORTO, // Estado inicial
                1,                             // Ya completó 1 pomodoro
                5,                             // Tiempo restante inicial (5 min)
                5,                             // Minutos transcurridos (completa el descanso)
                EstadoPomodoro.TRABAJO,        // Estado esperado
                25                             // Tiempo restante esperado (25 min trabajo)
            }
        });
    }
    
    /**
     * 🔴 FASE ROJA - Test parametrizado principal
     * 
     * Dado un estado inicial del Pomodoro,
     * Cuando pasa cierto tiempo,
     * Entonces el estado cambia correctamente según la lógica Pomodoro
     */
    @Test
    public void dadoUnEstado_cuandoPasaElTiempo_entoncesCambiaDeEstadoCorrectamente() {
        // ========== ARRANGE ==========
        System.out.println("\n🧪 Ejecutando: " + descripcionEscenario);
        
        Pomodoro pomodoro = new Pomodoro("user123");
        
        // Configurar estado inicial
        pomodoro.setEstadoActual(estadoInicial);
        pomodoro.setPomodorosCompletados(pomodorosCompletadosInicial);
        pomodoro.setTiempoRestanteMinutos(tiempoRestanteInicial);
        
        System.out.println("📌 Estado inicial:");
        System.out.println("   - Estado: " + estadoInicial);
        System.out.println("   - Pomodoros completados: " + pomodorosCompletadosInicial);
        System.out.println("   - Tiempo restante: " + tiempoRestanteInicial + " min");
        System.out.println("⏱️ Tiempo a transcurrir: " + minutosTranscurridos + " min");
        
        // ========== ACT ==========
        boolean huboCambioEstado = pomodoro.pasarTiempo(minutosTranscurridos);
        
        // ========== ASSERT ==========
        EstadoPomodoro estadoFinal = pomodoro.getEstadoActual();
        int tiempoRestanteFinal = pomodoro.getTiempoRestanteMinutos();
        
        System.out.println("✅ Estado final:");
        System.out.println("   - Estado: " + estadoFinal);
        System.out.println("   - Tiempo restante: " + tiempoRestanteFinal + " min");
        System.out.println("   - ¿Hubo cambio de estado?: " + huboCambioEstado);
        
        // Verificar estado esperado
        assertEquals("El estado debe ser " + estadoEsperado, 
                     estadoEsperado, estadoFinal);
        
        // Verificar tiempo restante esperado (con tolerancia para casos de exceso)
        assertTrue("El tiempo restante debe ser aproximadamente " + tiempoRestanteEsperado + 
                   " (actual: " + tiempoRestanteFinal + ")",
                   tiempoRestanteFinal <= tiempoRestanteEsperado && 
                   tiempoRestanteFinal >= (tiempoRestanteEsperado - minutosTranscurridos));
        
        // Verificar lógica de cambio de estado
        boolean deberiaHaberCambio = (estadoInicial != estadoEsperado);
        assertEquals("El flag de cambio de estado debe ser " + deberiaHaberCambio,
                    deberiaHaberCambio, huboCambioEstado);
        
        System.out.println("✅ Test pasado: " + descripcionEscenario);
    }
    
    /**
     * Test adicional: Verificar que estados PAUSADO y COMPLETADO no cambian con el tiempo
     */
}
