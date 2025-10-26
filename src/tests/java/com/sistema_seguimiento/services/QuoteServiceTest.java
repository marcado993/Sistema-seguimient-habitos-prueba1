package com.sistema_seguimiento.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para QuoteService - TDD
 * Incluye pruebas para verificar el comportamiento del servicio de frases diarias
 */
public class QuoteServiceTest {

    /**
     * PRUEBA 1/8 - FASE VERDE
     * Verifica que getDailyQuote() devuelve una frase no nula y no vacía
     */
    @Test
    public void given_usuarioLogeadoPrimeraVezDelDia_when_estaPantallaprincipal_then_ventanaEmergenteMuestraFraseDiaria() {
        // Given: El usuario se ha logeado por primera vez en el día
        QuoteService quoteService = new QuoteService();
        
        // When: El servicio obtiene la frase diaria para mostrar en la pantalla principal
        String dailyQuote = quoteService.getDailyQuote();
        
        // Then: Debe retornar una frase motivacional no nula y no vacía para mostrar en ventana emergente
        assertNotNull(dailyQuote, "La frase diaria no debe ser nula");
        assertFalse(dailyQuote.isEmpty(), "La frase diaria no debe estar vacía");
        assertTrue(dailyQuote.length() > 0, "La frase diaria debe tener contenido");
    }
    
    /**
     * PRUEBA 2/8 - FASE ROJA (Escenario 3.3)
     * Verifica que getDailyQuote() devuelve frases diferentes en días diferentes
     * 
     * NOTA: Esta prueba fallará porque la implementación actual usa LocalDate.now()
     * que no se puede simular sin inyección de dependencias o un método que acepte
     * una fecha como parámetro.
     */
    @Test
    public void given_cambioDeDay_when_solicitaFraseDiaria_then_devuelveFraseDiferente() {
        // Given: Un servicio de frases diarias
        QuoteService quoteService = new QuoteService();
        
        // When: Se solicita la frase para dos días diferentes
        // Día 1 (usando el día actual simulado como día 1)
        String fraseDay1 = quoteService.getDailyQuoteForDay(1);
        
        // Día 2 (simulando el día siguiente)
        String fraseDay2 = quoteService.getDailyQuoteForDay(2);
        
        // Then: Las frases deben ser diferentes
        assertNotNull(fraseDay1, "La frase del día 1 no debe ser nula");
        assertNotNull(fraseDay2, "La frase del día 2 no debe ser nula");
        assertNotEquals(fraseDay1, fraseDay2, 
            "Las frases de días diferentes deben ser distintas");
        
        // Mensaje de prueba exitosa
        System.out.println("[TEST PASSED] Prueba 2/8 - Cambio de dia: Las frases de dias diferentes son distintas");
        System.out.println("  Frase dia 1: " + fraseDay1);
        System.out.println("  Frase dia 2: " + fraseDay2);
    }
}
