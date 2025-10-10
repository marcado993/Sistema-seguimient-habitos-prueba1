package com.example.service;

import com.example.dao.ObjetivoDAO;
import com.example.model.Objetivo;
import com.example.model.RegistroProgreso;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de Objetivos - Capa de lógica de negocio
 * Maneja la lógica relacionada con objetivos
 */
public class ObjetivoService {
    
    private ObjetivoDAO objetivoDAO;
    
    public ObjetivoService() {
        this.objetivoDAO = new ObjetivoDAO();
    }
    
    public ObjetivoService(ObjetivoDAO objetivoDAO) {
        this.objetivoDAO = objetivoDAO;
    }
    
    /**
     * Obtener todos los objetivos de un usuario
     */
    public List<Objetivo> obtenerObjetivosPorUsuario(String usuarioId) {
        return objetivoDAO.findByUsuarioId(usuarioId);
    }
    
    /**
     * Obtener objetivos activos de un usuario
     */
    public List<Objetivo> obtenerObjetivosActivos(String usuarioId) {
        return objetivoDAO.findObjetivosActivos(usuarioId);
    }
    
    /**
     * Obtener objetivo por ID
     */
    public Objetivo obtenerObjetivoPorId(Long id) {
        return objetivoDAO.findById(id).orElse(null);
    }
    
    /**
     * Crear un nuevo objetivo
     */
    public Objetivo crearObjetivo(String titulo, String descripcion, String usuarioId, LocalDateTime fechaLimite) {
        Objetivo objetivo = new Objetivo();
        objetivo.setTitulo(titulo);
        objetivo.setDescripcion(descripcion);
        objetivo.setUsuarioId(usuarioId);
        objetivo.setFechaLimite(fechaLimite);
        objetivo.setEstado(Objetivo.EstadoObjetivo.ACTIVO);
        
        return objetivoDAO.save(objetivo);
    }
    
    /**
     * Actualizar progreso de un objetivo
     */
    public boolean actualizarProgreso(Long objetivoId, Integer nuevoProgreso, String observaciones) {
        Objetivo objetivo = obtenerObjetivoPorId(objetivoId);
        
        if (objetivo == null) {
            return false;
        }
        
        Integer progresoAnterior = objetivo.getProgreso();
        objetivo.setProgreso(nuevoProgreso);
        
        // Crear registro de progreso
        RegistroProgreso registro = new RegistroProgreso(objetivo, progresoAnterior, nuevoProgreso, observaciones);
        
        // Si alcanza el 100%, marcar como completado
        if (nuevoProgreso >= 100) {
            objetivo.setEstado(Objetivo.EstadoObjetivo.COMPLETADO);
        }
        
        objetivoDAO.save(objetivo);
        return true;
    }
    
    /**
     * Cambiar estado de un objetivo
     */
    public boolean cambiarEstado(Long objetivoId, Objetivo.EstadoObjetivo nuevoEstado) {
        Objetivo objetivo = obtenerObjetivoPorId(objetivoId);
        
        if (objetivo == null) {
            return false;
        }
        
        objetivo.setEstado(nuevoEstado);
        objetivoDAO.save(objetivo);
        return true;
    }
    
    /**
     * Validar si un objetivo está activo
     */
    public boolean esObjetivoActivo(Long objetivoId) {
        Objetivo objetivo = obtenerObjetivoPorId(objetivoId);
        return objetivo != null && objetivo.getEstado() == Objetivo.EstadoObjetivo.ACTIVO;
    }
    
    /**
     * Obtener porcentaje de completado
     */
    public Integer obtenerPorcentajeCompletado(Long objetivoId) {
        Objetivo objetivo = obtenerObjetivoPorId(objetivoId);
        return objetivo != null ? objetivo.getProgreso() : 0;
    }
}
