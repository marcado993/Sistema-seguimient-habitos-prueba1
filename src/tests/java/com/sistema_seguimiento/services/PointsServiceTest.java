package com.sistema_seguimiento.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 🔴 FASE ROJA - Prueba Parametrizada (1/2) para PointsService
 * 
 * ESTE TEST DEBE FALLAR porque PointsService NO EXISTE aún
 * 
 * TDD: UNA SOLA PRUEBA
 * Escenarios de prueba:
 * 1.1: CUMPLIDO -> 10 puntos
 * 1.2: NO_CUMPLIDO -> 0 puntos
 * 1.3: PARCIAL -> 5 puntos
 */
public class PointsServiceTest {
    
    /**
     * 🔴 UNA SOLA PRUEBA PARAMETRIZADA - TDD Puro
     * DEBE FALLAR: PointsService no existe
     */
    @ParameterizedTest(name = "Estado {0} debe dar {1} puntos")
    @CsvSource({
        "CUMPLIDO, 10",      // Escenario 1.1
        "NO_CUMPLIDO, 0",    // Escenario 1.2
        "PARCIAL, 5"         // Escenario 1.3
    })
    void testCalculatePoints_DeberiaRetornarPuntosCorrectosPorEstado(
            String estado, 
            int puntosEsperados) {
        
        // Arrange
        PointsService service = new PointsService();
        
        // Act
        int puntosObtenidos = service.calculatePoints(estado);
        
        // Assert
        assertEquals(puntosEsperados, puntosObtenidos,
            "El estado " + estado + " debería dar " + puntosEsperados + " puntos");
    }
}
