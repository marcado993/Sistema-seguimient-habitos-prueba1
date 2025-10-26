package com.sistema_seguimiento.config;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Configuración SEGURA de Supabase usando variables de entorno.
 * Esta clase maneja la configuración de conexión a Supabase de forma segura,
 * evitando exponer credenciales sensibles en el código fuente.
 * 
 * @author Sistema Seguimiento Team
 * @version 2.0
 */
public class SupabaseConfigSeguro {
    
    private static final Logger LOGGER = Logger.getLogger(SupabaseConfigSeguro.class.getName());
    
    // ============================================
    // CONFIGURACIÓN PÚBLICA (Segura para exponer)
    // ============================================
    
    /** URL base de Supabase - Seguro exponer públicamente */
    public static final String SUPABASE_URL = "https://pofkyvcgltumwpxvuzpp.supabase.co";
    
    /** Clave anónima de Supabase - Diseñada para uso público con RLS */
    private static final String SUPABASE_ANON_KEY = 
        getEnvOrDefault("SUPABASE_ANON_KEY", "tu-anon-key-aqui");
    
    // ============================================
    // CONFIGURACIÓN PRIVADA (Variables de entorno)
    // ============================================
    
    /** Clave de servicio - NUNCA exponer en frontend */
    private static final String SUPABASE_SERVICE_KEY = System.getenv("SUPABASE_SERVICE_KEY");
    
    /** Host de la base de datos PostgreSQL */
    private static final String DB_HOST = 
        getEnvOrDefault("SUPABASE_DB_HOST", "db.pofkyvcgltumwpxvuzpp.supabase.co");
    
    /** Puerto de PostgreSQL */
    private static final String DB_PORT = 
        getEnvOrDefault("SUPABASE_DB_PORT", "5432");
    
    /** Nombre de la base de datos */
    private static final String DB_NAME = 
        getEnvOrDefault("SUPABASE_DB_NAME", "postgres");
    
    /** Usuario de la base de datos */
    private static final String DB_USER = 
        getEnvOrDefault("SUPABASE_DB_USER", "postgres");
    
    /** Contraseña de la base de datos - Crítico */
    private static final String DB_PASSWORD = System.getenv("SUPABASE_DB_PASSWORD");
    
    // ============================================
    // GETTERS PÚBLICOS
    // ============================================
    
    /**
     * Obtiene la URL de Supabase
     * @return URL base de Supabase
     */
    public static String getSupabaseUrl() {
        return SUPABASE_URL;
    }
    
    /**
     * Obtiene la clave anónima de forma segura
     * @return Optional con la clave anónima
     */
    public static Optional<String> getAnonKey() {
        return Optional.ofNullable(SUPABASE_ANON_KEY)
                       .filter(key -> !key.equals("tu-anon-key-aqui"));
    }
    
    /**
     * Obtiene la clave de servicio de forma segura
     * @return Optional con la clave de servicio
     */
    public static Optional<String> getServiceKey() {
        return Optional.ofNullable(SUPABASE_SERVICE_KEY);
    }
    
    /**
     * Obtiene el host de la base de datos
     * @return Host de PostgreSQL
     */
    public static String getDbHost() {
        return DB_HOST;
    }
    
    /**
     * Obtiene la contraseña de la base de datos de forma segura
     * @return Optional con la contraseña
     */
    public static Optional<String> getDbPassword() {
        return Optional.ofNullable(DB_PASSWORD);
    }
    
    // ============================================
    // STRINGS DE CONEXIÓN Y UTILIDADES
    // ============================================
    
    /**
     * Método auxiliar para obtener variables de entorno con valor por defecto
     * @param envVar Nombre de la variable de entorno
     * @param defaultValue Valor por defecto si no está definida
     * @return Valor de la variable o el valor por defecto
     */
    private static String getEnvOrDefault(String envVar, String defaultValue) {
        String value = System.getenv(envVar);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
    
    /**
     * Genera la URL de conexión JDBC a PostgreSQL
     * @return URL JDBC completa con SSL habilitado
     */
    public static String getJdbcUrl() {
        return String.format(
            "jdbc:postgresql://%s:%s/%s?sslmode=require",
            DB_HOST, DB_PORT, DB_NAME
        );
    }
    
    /**
     * Obtiene el usuario de la base de datos
     * @return Usuario de PostgreSQL
     */
    public static String getDbUser() {
        return DB_USER;
    }
    
    /**
     * Valida que toda la configuración requerida esté presente
     * @return true si la configuración es válida, false en caso contrario
     */
    public static boolean isConfigured() {
        boolean hasPassword = DB_PASSWORD != null && !DB_PASSWORD.isEmpty();
        
        if (!hasPassword) {
            LOGGER.severe("⚠️ CONFIGURACIÓN INCOMPLETA: Falta SUPABASE_DB_PASSWORD");
            logConfigurationInstructions();
        } else {
            LOGGER.info("✓ Configuración de Supabase validada correctamente");
        }
        
        return hasPassword;
    }
    
    /**
     * Valida configuración completa incluyendo claves de API
     * @return true si todas las configuraciones están presentes
     */
    public static boolean isFullyConfigured() {
        boolean hasPassword = DB_PASSWORD != null && !DB_PASSWORD.isEmpty();
        boolean hasAnonKey = getAnonKey().isPresent();
        boolean hasServiceKey = getServiceKey().isPresent();
        
        if (!hasPassword || !hasAnonKey || !hasServiceKey) {
            LOGGER.warning("⚠️ Configuración incompleta detectada");
            logConfigurationInstructions();
            return false;
        }
        
        LOGGER.info("✓ Configuración completa de Supabase validada");
        return true;
    }
    
    /**
     * Registra instrucciones para configurar las variables de entorno
     */
    private static void logConfigurationInstructions() {
        LOGGER.info("""
            
            ════════════════════════════════════════════════════════
            INSTRUCCIONES DE CONFIGURACIÓN - Variables de Entorno
            ════════════════════════════════════════════════════════
            
            Establece las siguientes variables de entorno:
            
            REQUERIDO:
              • SUPABASE_DB_PASSWORD    - Contraseña de PostgreSQL
            
            OPCIONAL:
              • SUPABASE_ANON_KEY       - Clave anónima pública
              • SUPABASE_SERVICE_KEY    - Clave de servicio (backend)
              • SUPABASE_DB_HOST        - Host personalizado
              • SUPABASE_DB_PORT        - Puerto personalizado
              • SUPABASE_DB_NAME        - Nombre de BD personalizado
              • SUPABASE_DB_USER        - Usuario personalizado
            
            En Windows PowerShell:
              $env:SUPABASE_DB_PASSWORD = "tu-password"
              $env:SUPABASE_ANON_KEY = "tu-anon-key"
            
            En Linux/Mac:
              export SUPABASE_DB_PASSWORD="tu-password"
              export SUPABASE_ANON_KEY="tu-anon-key"
            
            ════════════════════════════════════════════════════════
            """);
    }
    
    /**
     * Muestra información de configuración de forma segura (sin exponer credenciales)
     * @return String formateado con información de configuración
     */
    public static String getConfigInfo() {
        return String.format("""
            ╔═══════════════════════════════════════════════════════════╗
            ║       CONFIGURACIÓN SUPABASE (SEGURA v2.0)                ║
            ╠═══════════════════════════════════════════════════════════╣
            ║ URL Supabase:     %-39s ║
            ║ DB Host:          %-39s ║
            ║ DB Port:          %-39s ║
            ║ DB Name:          %-39s ║
            ║ DB User:          %-39s ║
            ║ DB Password:      %-39s ║
            ║ Anon Key:         %-39s ║
            ║ Service Key:      %-39s ║
            ║ Configurado:      %-39s ║
            ╚═══════════════════════════════════════════════════════════╝
            """,
            SUPABASE_URL,
            DB_HOST,
            DB_PORT,
            DB_NAME,
            DB_USER,
            getDbPassword().isPresent() ? "✓ Configurada" : "✗ Faltante",
            getAnonKey().isPresent() ? "✓ Configurada" : "✗ Faltante",
            getServiceKey().isPresent() ? "✓ Configurada" : "✗ Faltante",
            isConfigured() ? "✓ SÍ" : "✗ NO"
        );
    }
    
    /**
     * Imprime la información de configuración en la consola
     */
    public static void printConfigInfo() {
        System.out.println(getConfigInfo());
    }
    
    /**
     * Valida y lanza excepción si la configuración no es válida
     * @throws IllegalStateException si la configuración requerida no está presente
     */
    public static void validateOrThrow() {
        if (!isConfigured()) {
            throw new IllegalStateException(
                "Configuración de Supabase incompleta. " +
                "Establece la variable de entorno SUPABASE_DB_PASSWORD"
            );
        }
    }
}
