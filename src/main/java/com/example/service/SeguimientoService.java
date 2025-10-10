package com.example.service;

import com.example.dao.HabitoDAO;
import com.example.model.Habito;
import com.example.model.SeguimientoHabito;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de Seguimiento - Maneja el seguimiento de hábitos
 */
public class SeguimientoService {
    
    private HabitoDAO habitoDAO;
    
    public SeguimientoService() {
        this.habitoDAO = new HabitoDAO();
    }
    
    public SeguimientoService(HabitoDAO habitoDAO) {
        this.habitoDAO = habitoDAO;
    }
    
    /**
     * Registrar seguimiento diario de un hábito
     */
    public boolean registrarSeguimiento(Long habitoId, LocalDate fecha, int completado, String observaciones) {
        Habito habito = habitoDAO.findById(habitoId).orElse(null);
        
        if (habito == null) {
            return false;
        }
        
        // Crear nuevo seguimiento
        SeguimientoHabito seguimiento = new SeguimientoHabito();
        seguimiento.setHabito(habito);
        seguimiento.setFecha(fecha);
        seguimiento.setCompletado(completado);
        seguimiento.setNotas(observaciones);
        
        // Guardar el hábito con el nuevo seguimiento
        habito.getSeguimientos().add(seguimiento);
        habitoDAO.save(habito);
        
        return true;
    }
    
    /**
     * Obtener seguimientos de un hábito en un rango de fechas
     */
    public List<SeguimientoHabito> obtenerSeguimientos(Long habitoId, LocalDate desde, LocalDate hasta) {
        Habito habito = habitoDAO.findById(habitoId).orElse(null);
        
        if (habito == null) {
            return List.of();
        }
        
        return habito.getSeguimientos().stream()
                .filter(s -> !s.getFecha().isBefore(desde) && !s.getFecha().isAfter(hasta))
                .toList();
    }
    
    /**
     * Verificar si un hábito fue completado en una fecha específica
     */
    public boolean fueCompletado(Long habitoId, LocalDate fecha) {
        Habito habito = habitoDAO.findById(habitoId).orElse(null);
        
        if (habito == null) {
            return false;
        }
        
        return habito.tieneMetaDelDia(fecha);
    }
    
    /**
     * Obtener racha actual de un hábito
     */
    public int obtenerRachaActual(Long habitoId) {
        Habito habito = habitoDAO.findById(habitoId).orElse(null);
        
        if (habito == null) {
            return 0;
        }
        
        return habito.getDiasConsecutivos();
    }
}
