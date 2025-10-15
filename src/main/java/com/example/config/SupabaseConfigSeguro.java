package com.example.config;

/**
 * Configuración SEGURA de Supabase
 * Usa variables de entorno para datos sensibles
 */
public class SupabaseConfigSeguro {
    
    // ============================================
    // CONFIGURACIÓN PÚBLICA (Segura para exponer)
    // ============================================
    
    /**
     * URL de Supabase - SEGURO exponer
     */
    public static final String SUPABASE_URL = "https://pofkyvcgltumwpxvuzpp.supabase.co";
    
    /**
     * Anon Key - SEGURO exponer (diseñada para frontend público)
     * Tiene permisos limitados por Row Level Security
     */
    public static final String SUPABASE_ANON_KEY = 
        System.getenv("SUPABASE_ANON_KEY") != null 
            ? System.getenv("SUPABASE_ANON_KEY")
            : "tu-anon-key-aqui"; // Default para desarrollo
    
    // ============================================
    // CONFIGURACIÓN PRIVADA (Usar variables de entorno)
    // ============================================
    
    /**
     * Service Key - PELIGROSO exponer
     * Solo usar en backend, NUNCA en frontend
     */
    public static final String SUPABASE_SERVICE_KEY = System.getenv("SUPABASE_SERVICE_KEY");
    
    /**
     * Host de la base de datos
     */
    public static final String DB_HOST = 
        System.getenv("SUPABASE_DB_HOST") != null
            ? System.getenv("SUPABASE_DB_HOST")
            : "db.pofkyvcgltumwpxvuzpp.supabase.co";
    
    public static final String DB_PORT = "5432";
    public static final String DB_NAME = "postgres";
    public static final String DB_USER = "postgres";
    
    /**
     * Password - NUNCA expongas esto
     */
    public static final String DB_PASSWORD = System.getenv("SUPABASE_DB_PASSWORD");
    
    // ============================================
    // STRINGS DE CONEXIÓN
    // ============================================
    
    public static String getJdbcUrl() {
        return String.format(
            "jdbc:postgresql://%s:%s/%s?sslmode=require",
            DB_HOST, DB_PORT, DB_NAME
        );
    }
    
    /**
     * Validar configuración
     */
    public static boolean isConfigured() {
        boolean configured = DB_PASSWORD != null && !DB_PASSWORD.isEmpty();
        
        if (!configured) {
            System.err.println("⚠️ CONFIGURACIÓN FALTANTE:");
            System.err.println("   Establece las variables de entorno:");
            System.err.println("   - SUPABASE_DB_PASSWORD (REQUERIDO)");
            System.err.println("   - SUPABASE_SERVICE_KEY (Opcional)");
            System.err.println("");
            System.err.println("   En PowerShell:");
            System.err.println("   $env:SUPABASE_DB_PASSWORD = \"tu-password\"");
            System.err.println("   $env:SUPABASE_SERVICE_KEY = \"tu-service-key\"");
        }
        
        return configured;
    }
    
    /**
     * Mostrar configuración (sin passwords)
     */
    public static String getConfigInfo() {
        return String.format("""
            ╔═══════════════════════════════════════════════════╗
            ║       CONFIGURACIÓN SUPABASE (SEGURA)             ║
            ╠═══════════════════════════════════════════════════╣
            ║ URL:          %-35s ║
            ║ DB Host:      %-35s ║
            ║ DB Password:  %-35s ║
            ║ Service Key:  %-35s ║
            ║ Configurado:  %-35s ║
            ╚═══════════════════════════════════════════════════╝
            """,
            SUPABASE_URL,
            DB_HOST,
            DB_PASSWORD != null ? "✓ Configurada" : "✗ Faltante",
            SUPABASE_SERVICE_KEY != null ? "✓ Configurada" : "✗ Faltante",
            isConfigured() ? "✓ SÍ" : "✗ NO"
        );
    }
}
