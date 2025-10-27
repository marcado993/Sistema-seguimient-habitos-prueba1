package com.sistema_seguimiento.services;

import com.sistema_seguimiento.dao.UsuarioDAOJPA;
import java.util.logging.Logger;

/**
 * Servicio para cálculo de puntos del sistema de gamificación
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
     * ANTES: Método monolítico con múltiples responsabilidades
     * 
     * public void addPointsToUser(Integer usuarioId, String estado) {
     *     if (usuarioId == null) return;
     *     int puntos = calculatePoints(estado);
     *     if (puntos > 0) {
     *         usuarioDAO.addPoints(usuarioId, puntos);
     *     }
     * }
     */
    
    /**
     * DESPUÉS: Separado en 3 responsabilidades claras
     */
    public void addPointsToUser(Integer usuarioId, String estado) {
        if (usuarioId == null) return;
        
        int puntos = validateAndCalculatePoints(estado);
        updateUserPoints(usuarioId, puntos);
    }

    private int validateAndCalculatePoints(String estado) {
        return calculatePoints(estado);
    }

    private void updateUserPoints(Integer usuarioId, int puntos) {
        if (puntos <= 0) return;
        
        boolean resultado = usuarioDAO.addPoints(usuarioId, puntos);
        if (resultado) {
            LOGGER.info("+" + puntos + " puntos a usuario " + usuarioId);
        }
    }
}
