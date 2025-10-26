package com.sistema_seguimiento.services;

import java.util.logging.Logger;

/**
 * 🟢 FASE VERDE - Servicio para cálculo de puntos (Código mínimo)
 * 
 * @version 1.0
 * @since 2.0
 */
public class PointsService {
    
    private static final Logger LOGGER = Logger.getLogger(PointsService.class.getName());
    
    /**
     * 🟢 Código MÍNIMO para pasar el test parametrizado
     * 
     * @param estado Estado del hábito (CUMPLIDO, NO_CUMPLIDO, PARCIAL)
     * @return Puntos correspondientes
     */
    public int calculatePoints(String estado) {
        if (estado == null) {
            return 0;
        }
        
        return switch (estado.toUpperCase().trim()) {
            case "CUMPLIDO" -> 10;
            case "PARCIAL" -> 5;
            case "NO_CUMPLIDO" -> 0;
            default -> 0;
        };
    }
    
    /**
     * 🟢 Método para agregar puntos a un usuario (usado en test de mock)
     * 
     * @param usuarioId ID del usuario
     * @param estado Estado del hábito
     */
    public void addPointsToUser(Integer usuarioId, String estado) {
        int puntos = calculatePoints(estado);
        LOGGER.info("Usuario " + usuarioId + " ganó " + puntos + " puntos por estado: " + estado);
        // La integración real con DAO se hará después
    }
}
