package com.sistema_seguimiento.services;

import com.sistema_seguimiento.dao.QuoteDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * FASE ROJA - Prueba con Mock (1/2) para QuoteService
 * 
 * Verifica que QuoteService llame correctamente a QuoteDAO.getQuotes()
 * para obtener las frases motivacionales
 */
public class QuoteServiceWithMockTest {
    
    private QuoteDAO quoteDAO;
    
    @BeforeEach
    void setup() {
        // Crear mock del QuoteDAO
        quoteDAO = mock(QuoteDAO.class);
        
        System.out.println("[TEST SETUP] Preparando prueba con Mock 1/2");
        System.out.println("  Mock de QuoteDAO creado exitosamente");
    }
    
    /**
     * UNA PRUEBA CON MOCK (1/2) - TDD - FASE VERDE
     * Verifica que QuoteService inicializa las frases llamando a QuoteDAO.getQuotes()
     */
    @Test
    @DisplayName("QuoteService debe llamar a QuoteDAO.getQuotes() durante la inicializacion")
    void given_quoteServiceConDAO_when_inicializa_then_llamaGetQuotes() {
        System.out.println("[TEST RUNNING] Prueba con Mock 1/2 - Verificando llamada a QuoteDAO.getQuotes()");
        
        // Arrange
        List<String> mockQuotes = Arrays.asList(
            "Frase de prueba 1",
            "Frase de prueba 2",
            "Frase de prueba 3"
        );
        
        // Configurar el mock para devolver las frases de prueba
        when(quoteDAO.getQuotes()).thenReturn(mockQuotes);
        
        // Act
        // Crear una nueva instancia que debería llamar a getQuotes() en su constructor
        QuoteService service = new QuoteService(quoteDAO);
        
        // Obtener una frase para verificar que se están usando las del DAO
        String dailyQuote = service.getDailyQuote();
        
        // Assert
        // Verificar que getQuotes() fue llamado exactamente 1 vez
        verify(quoteDAO, times(1)).getQuotes();
        
        // Verificar que la frase obtenida es una de las del mock
        assertNotNull(dailyQuote, "La frase diaria no debe ser nula");
        assertTrue(mockQuotes.contains(dailyQuote), 
            "La frase debe ser una de las proporcionadas por el DAO");
        
        // Mensaje de prueba exitosa
        System.out.println("[TEST PASSED] Prueba con Mock 1/2 - FASE VERDE completada exitosamente");
        System.out.println("  QuoteService.getQuotes() fue llamado 1 vez");
        System.out.println("  Frase obtenida: " + dailyQuote);
        System.out.println("  La frase proviene correctamente del QuoteDAO mockeado");
    }
}
