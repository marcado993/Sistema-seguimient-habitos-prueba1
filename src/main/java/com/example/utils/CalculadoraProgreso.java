package com.example.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Calculadora de progreso para hábitos y objetivos
 * Centraliza cálculos complejos relacionados con el progreso
 */
public class CalculadoraProgreso {
    
    /**
     * Calcular porcentaje de completado entre dos fechas
     */
    public static double calcularPorcentaje(int diasCompletados, int diasTotales) {
        if (diasTotales <= 0) {
            return 0.0;
        }
        return (diasCompletados * 100.0) / diasTotales;
    }
    
    /**
     * Calcular racha actual de días consecutivos
     */
    public static int calcularRachaDiaria(List<LocalDate> fechasCompletadas) {
        if (fechasCompletadas == null || fechasCompletadas.isEmpty()) {
            return 0;
        }
        
        fechasCompletadas.sort(LocalDate::compareTo);
        
        int rachaActual = 1;
        LocalDate fechaActual = LocalDate.now();
        
        // Verificar si hoy o ayer está completado
        boolean hayCompletadoReciente = fechasCompletadas.contains(fechaActual) || 
                                       fechasCompletadas.contains(fechaActual.minusDays(1));
        
        if (!hayCompletadoReciente) {
            return 0;
        }
        
        // Contar días consecutivos desde hoy hacia atrás
        LocalDate fechaComparacion = fechaActual;
        for (int i = fechasCompletadas.size() - 1; i >= 0; i--) {
            LocalDate fecha = fechasCompletadas.get(i);
            
            if (fecha.equals(fechaComparacion) || fecha.equals(fechaComparacion.minusDays(1))) {
                rachaActual++;
                fechaComparacion = fecha;
            } else {
                break;
            }
        }
        
        return rachaActual;
    }
    
    /**
     * Calcular días transcurridos entre dos fechas
     */
    public static long calcularDiasTranscurridos(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(desde, hasta);
    }
    
    /**
     * Calcular promedio de completado por semana
     */
    public static double calcularPromedioSemanal(int diasCompletados, LocalDate fechaInicio) {
        if (fechaInicio == null) {
            return 0.0;
        }
        
        long diasTotales = calcularDiasTranscurridos(fechaInicio, LocalDate.now());
        if (diasTotales < 7) {
            return calcularPorcentaje(diasCompletados, (int) diasTotales);
        }
        
        double semanas = diasTotales / 7.0;
        return diasCompletados / semanas;
    }
    
    /**
     * Determinar si un objetivo está en riesgo basándose en el progreso y fecha límite
     */
    public static boolean estaEnRiesgo(int progresoActual, LocalDate fechaInicio, LocalDate fechaLimite) {
        if (fechaLimite == null || fechaInicio == null) {
            return false;
        }
        
        LocalDate hoy = LocalDate.now();
        
        if (hoy.isAfter(fechaLimite)) {
            return true;
        }
        
        long diasTotales = calcularDiasTranscurridos(fechaInicio, fechaLimite);
        long diasTranscurridos = calcularDiasTranscurridos(fechaInicio, hoy);
        
        if (diasTotales <= 0) {
            return false;
        }
        
        double progresoEsperado = (diasTranscurridos * 100.0) / diasTotales;
        
        // Si el progreso actual está 20% por debajo del esperado, está en riesgo
        return progresoActual < (progresoEsperado - 20);
    }
    
    // Constructor privado
    private CalculadoraProgreso() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
    }
}
