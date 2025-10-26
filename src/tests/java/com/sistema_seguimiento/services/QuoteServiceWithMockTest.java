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
     * UNA PRUEBA CON MOCK (1/2) - TDD - FASE ROJA
     * Verifica que QuoteService inicializa las frases llamando a QuoteDAO.getQuotes()
     * 
     * NOTA: Esta prueba DEBE FALLAR porque QuoteService no tiene constructor que acepte QuoteDAO
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
        
        System.out.println("[TEST FAILED] Prueba con Mock 1/2 - FASE ROJA");
        System.out.println("  Error: El constructor QuoteService(QuoteDAO) no existe");
        System.out.println("  Solucion: Implementar constructor que acepte QuoteDAO como parametro");
        System.out.println("  Solucion: Modificar QuoteService para usar QuoteDAO.getQuotes()");
        
        // Fallar explícitamente la prueba indicando que falta la implementación
        fail("FASE ROJA: QuoteService no tiene constructor QuoteService(QuoteDAO). " +
             "Se requiere implementar la inyeccion de dependencias para QuoteDAO.");
        
        // El siguiente código no se ejecutará hasta que se implemente el constructor
        // QuoteService service = new QuoteService(quoteDAO);
        // String dailyQuote = service.getDailyQuote();
        // verify(quoteDAO, times(1)).getQuotes();
        // assertNotNull(dailyQuote, "La frase diaria no debe ser nula");
        // assertTrue(mockQuotes.contains(dailyQuote), "La frase debe ser una de las proporcionadas por el DAO");
    }
}
