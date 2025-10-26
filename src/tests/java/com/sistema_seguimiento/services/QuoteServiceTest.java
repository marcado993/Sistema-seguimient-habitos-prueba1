package com.sistema_seguimiento.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba unitaria para QuoteService - Fase ROJA (TDD)
 * Esta prueba fallará hasta que implementemos el servicio
 */
public class QuoteServiceTest {

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
}

