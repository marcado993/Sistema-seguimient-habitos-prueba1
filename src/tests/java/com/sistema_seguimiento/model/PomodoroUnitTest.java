package com.sistema_seguimiento.model;

import com.sistema_seguimiento.model.Pomodoro.EstadoPomodoro;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests Unitarios (Normales) para Pomodoro
 * 
 * Esta clase contiene tests unitarios sin parametrizar
 * que prueban funcionalidades específicas del modelo Pomodoro.
 * 
 * Separada de PomodoroTest.java que usa @RunWith(Parameterized.class)
 */
public class PomodoroUnitTest {
    
    /**
     * 🔴 FASE ROJA - Test de inicialización
     * 
     * Dado que se crea un nuevo Pomodoro,
     * Cuando se inicializa,
     * Entonces su estado debe ser TRABAJO y el tiempo debe empezar en 25 minutos
     */
    @Test
    public void cuandoSeIniciaUnPomodoro_entoncesSuEstadoEsCorriendoYElTiempoEmpiezaAContar() {
        System.out.println("\n🧪 Test: Inicialización de Pomodoro");
        
        // ========== ARRANGE & ACT ==========
        // Crear un nuevo Pomodoro (se inicializa automáticamente en el constructor)
        Pomodoro pomodoro = new Pomodoro("user123");
        
        System.out.println("📌 Pomodoro creado");
        
        // ========== ASSERT ==========
        // Verificar que el estado inicial es TRABAJO
        assertEquals("El estado inicial debe ser TRABAJO", 
                    EstadoPomodoro.TRABAJO, 
                    pomodoro.getEstadoActual());
        
        System.out.println("✅ Estado: " + pomodoro.getEstadoActual());
        
        // Verificar que el tiempo restante es 25 minutos
        assertEquals("El tiempo inicial debe ser 25 minutos", 
                    25, 
                    pomodoro.getTiempoRestanteMinutos());
        
        System.out.println("✅ Tiempo restante: " + pomodoro.getTiempoRestanteMinutos() + " min");
        
        // Verificar que los pomodoros completados empiezan en 0
        assertEquals("Los pomodoros completados deben empezar en 0",
                    0,
                    pomodoro.getPomodorosCompletados());
        
        System.out.println("✅ Pomodoros completados: " + pomodoro.getPomodorosCompletados());
        
        // Verificar que el timer está activo
        assertTrue("El timer debe estar activo inicialmente",
                  pomodoro.estaActivo());
        
        System.out.println("✅ Timer activo: " + pomodoro.estaActivo());
        
        // Verificar que está en estado de trabajo
        assertTrue("Debe estar en estado de trabajo",
                  pomodoro.estaEnTrabajo());
        
        System.out.println("✅ En trabajo: " + pomodoro.estaEnTrabajo());
        
        // Verificar que NO está en descanso
        assertFalse("No debe estar en descanso inicialmente",
                   pomodoro.estaEnDescanso());
        
        System.out.println("✅ En descanso: " + pomodoro.estaEnDescanso());
        
        // Verificar que tiene un ID de usuario
        assertNotNull("Debe tener un usuario ID",
                     pomodoro.getUsuarioId());
        
        assertEquals("El usuario ID debe ser 'user123'",
                    "user123",
                    pomodoro.getUsuarioId());
        
        System.out.println("✅ Usuario ID: " + pomodoro.getUsuarioId());
        
        // Verificar que la fecha de inicio no es null
        assertNotNull("Debe tener fecha de inicio de sesión",
                     pomodoro.getInicioSesion());
        
        System.out.println("✅ Inicio sesión: " + pomodoro.getInicioSesion());
        
        // Verificar que la fecha de última actualización no es null
        assertNotNull("Debe tener fecha de última actualización",
                     pomodoro.getUltimaActualizacion());
        
        System.out.println("✅ Última actualización: " + pomodoro.getUltimaActualizacion());
        
        System.out.println("✅ Test pasado: Inicialización correcta del Pomodoro");
    }
}
