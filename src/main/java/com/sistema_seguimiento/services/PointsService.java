package com.sistema_seguimiento.services;

import com.sistema_seguimiento.dao.UsuarioDAOJPA;
import java.util.logging.Logger;

/**
 * 🟢 FASE VERDE - Servicio para cálculo de puntos (Código Mínimo)
 * 
 * Implementación mínima para hacer pasar los tests
 */
public class PointsService {
    
    private static final Logger LOGGER = Logger.getLogger(PointsService.class.getName());
    
    // Constantes de puntos por estado
    private static final int PUNTOS_CUMPLIDO = 10;
    private static final int PUNTOS_PARCIAL = 5;
    private static final int PUNTOS_NO_CUMPLIDO = 0;
    
    private UsuarioDAOJPA usuarioDAO;
    
    public PointsService() {
        this.usuarioDAO = new UsuarioDAOJPA();
    }
    
    /**
     * 🟢 Calcula los puntos según el estado del hábito (CÓDIGO MÍNIMO)
     * 
     * @param estado Estado del hábito (CUMPLIDO, NO_CUMPLIDO, PARCIAL)
     * @return Puntos correspondientes al estado
     */
    public int calculatePoints(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return 0;
        }
        
        return switch (estado.toUpperCase().trim()) {
            case "CUMPLIDO" -> PUNTOS_CUMPLIDO;
            case "PARCIAL" -> PUNTOS_PARCIAL;
            case "NO_CUMPLIDO" -> PUNTOS_NO_CUMPLIDO;
            default -> 0;
        };
    }
    
    /**
     * 🟢 Agrega puntos a un usuario según el estado del hábito
     * 
     * @param usuarioId ID del usuario
     * @param estado Estado del hábito
     */
    public void addPointsToUser(Integer usuarioId, String estado) {
        if (usuarioId == null) {
            LOGGER.warning("UsuarioId es null, no se pueden agregar puntos");
            return;
        }
        
        int puntos = calculatePoints(estado);
        
        if (puntos > 0) {
            boolean resultado = usuarioDAO.addPoints(usuarioId, puntos);
            if (resultado) {
                LOGGER.info("Agregados " + puntos + " puntos al usuario " + usuarioId);
            } else {
                LOGGER.warning("No se pudieron agregar puntos al usuario " + usuarioId);
            }
        } else {
            LOGGER.info("Estado " + estado + " otorga 0 puntos, no se actualiza BD");
        }
    }
}
