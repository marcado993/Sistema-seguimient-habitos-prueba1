package com.example.service;

import com.example.dao.HabitoDAO;
import com.example.model.Habito;
import com.example.model.RegistroProgreso;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de Hábitos - Capa de lógica de negocio
 * Separa la lógica de negocio de los controladores
 */
public class HabitoService {
    
    private HabitoDAO habitoDAO;
    
    public HabitoService() {
        this.habitoDAO = new HabitoDAO();
    }
    
    public HabitoService(HabitoDAO habitoDAO) {
        this.habitoDAO = habitoDAO;
    }
    
    /**
     * Obtener todos los hábitos activos de un usuario
     */
    public List<Habito> obtenerHabitosActivos(String usuarioId) {
        return habitoDAO.findHabitosActivos(usuarioId);
    }
    
    /**
     * Obtener hábito por ID
     */
    public Habito obtenerHabitoPorId(Long id) {
        return habitoDAO.findById(id).orElse(null);
    }
    
    /**
     * Crear un nuevo hábito
     */
    public Habito crearHabito(Habito habito) {
        return habitoDAO.save(habito);
    }
    
    /**
     * Registrar cumplimiento de hábito
     */
    public boolean registrarCumplimiento(Long habitoId, LocalDate fecha, boolean cumplido) {
        Habito habito = obtenerHabitoPorId(habitoId);
        
        if (habito == null) {
            return false;
        }
        
        // Lógica para registrar el cumplimiento
        return true;
    }
    
    /**
     * Calcular racha actual de un hábito
     */
    public int calcularRachaActual(Long habitoId) {
        Habito habito = obtenerHabitoPorId(habitoId);
        
        if (habito != null) {
            return habito.calcularRachaActual();
        }
        
        return 0;
    }
    
    /**
     * Obtener estadísticas de un hábito
     */
    public double obtenerPorcentajeCompletado(Long habitoId, LocalDate desde, LocalDate hasta) {
        Habito habito = obtenerHabitoPorId(habitoId);
        
        if (habito != null) {
            return habito.getPorcentajeCompletado(desde, hasta);
        }
        
        return 0.0;
    }
    
    /**
     * Eliminar un hábito (marcarlo como inactivo)
     */
    public boolean eliminarHabito(Long habitoId) {
        Habito habito = obtenerHabitoPorId(habitoId);
        
        if (habito != null) {
            habito.setActivo(false);
            habitoDAO.save(habito);
            return true;
        }
        
        return false;
    }
}
