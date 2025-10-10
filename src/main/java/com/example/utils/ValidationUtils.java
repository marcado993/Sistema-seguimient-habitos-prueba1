package com.example.utils;

import java.util.regex.Pattern;

/**
 * Clase de utilidades para validación
 * Implementa validaciones preventivas antes de operaciones riesgosas
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    /**
     * Validar si un email es válido
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validar datos de un hábito
     */
    public static boolean isValidHabitoData(String nombre, String frecuencia) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        
        if (nombre.length() > Constants.MAX_NOMBRE_LENGTH) {
            return false;
        }
        
        if (frecuencia == null || frecuencia.trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Validar datos de un objetivo
     */
    public static boolean isValidObjetivoData(String titulo, String descripcion) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }
        
        if (titulo.length() > Constants.MAX_NOMBRE_LENGTH) {
            return false;
        }
        
        if (descripcion != null && descripcion.length() > Constants.MAX_DESCRIPCION_LENGTH) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Validar que un número esté en un rango
     */
    public static boolean isInRange(Integer valor, int min, int max) {
        if (valor == null) {
            return false;
        }
        return valor >= min && valor <= max;
    }
    
    /**
     * Validar que un progreso sea válido (0-100)
     */
    public static boolean isValidProgreso(Integer progreso) {
        return isInRange(progreso, 0, 100);
    }
    
    /**
     * Validar que una meta diaria sea válida
     */
    public static boolean isValidMetaDiaria(Integer meta) {
        return isInRange(meta, 1, Constants.MAX_META_DIARIA);
    }
    
    /**
     * Validar que una cadena no esté vacía
     */
    public static boolean isNotEmpty(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }
    
    /**
     * Validar que un ID sea válido
     */
    public static boolean isValidId(Long id) {
        return id != null && id > 0;
    }
    
    /**
     * Limpiar y validar input de usuario
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim();
    }
    
    // Constructor privado
    private ValidationUtils() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
    }
}
