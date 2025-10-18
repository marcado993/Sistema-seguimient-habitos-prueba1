package com.sistema_seguimiento.config;

/**
 * Configuración de conexión a Supabase
 * 
 * INSTRUCCIONES PARA CONFIGURAR:
 * 1. Ve a https://supabase.com
 * 2. Crea un proyecto o usa uno existente
 * 3. Ve a Settings > Database
 * 4. Copia el "Connection String" en formato JDBC
 * 5. Reemplaza los valores aquí con tus credenciales
 */
public class SupabaseConfig {
    
    // ============================================
    // CONFIGURACIÓN DE SUPABASE
    // ============================================
    
    /**
     * URL de tu proyecto Supabase
     * Formato: https://tu-proyecto.supabase.co
     */
    public static final String SUPABASE_URL = "https://pofkyvcgltumwpxvuzpp.supabase.co";
    
    /**
     * Clave pública (anon key) de Supabase
     * La encuentras en Settings > API
     * ✅ Esta clave es SEGURA para exponer públicamente
     */
    public static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBvZmt5dmNnbHR1bXdweHZ1enBwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjA1MzQxMTIsImV4cCI6MjA3NjExMDExMn0.u0LZoEFXjT_3tehyn5gQ5bmPbXn3eVewWI_PzuvyOsw";
    
    /**
     * Clave de servicio (service role key) - SOLO PARA BACKEND
     * La encuentras en Settings > API
     * ⚠️ NUNCA expongas esta clave en el frontend
     */
    public static final String SUPABASE_SERVICE_KEY = "tu-supabase-service-key-aqui";
    
    // ============================================
    // CONFIGURACIÓN DE BASE DE DATOS POSTGRESQL
    // ============================================
    
    /**
     * Host de la base de datos
     * Usando AWS Pooler para mejor rendimiento y evitar timeouts
     * Formato: aws-1-us-east-2.pooler.supabase.com
     */
    public static final String DB_HOST = "aws-1-us-east-2.pooler.supabase.com";
    
    /**
     * Puerto de PostgreSQL
     * 5432 = Conexión directa (puede tener problemas de timeout)
     * 6543 = Transaction Pooler (RECOMENDADO para aplicaciones)
     */
    public static final String DB_PORT = "6543"; // Usando Transaction Pooler
    
    /**
     * Nombre de la base de datos (usualmente "postgres")
     */
    public static final String DB_NAME = "postgres";
    
    /**
     * Usuario de la base de datos
     * Para puerto 6543 (Transaction Pooler): postgres.{project-ref}
     * Para puerto 5432 (Directo): postgres
     */
    public static final String DB_USER = "postgres.pofkyvcgltumwpxvuzpp";
    
    /**
     * Contraseña de la base de datos
     * La estableciste al crear el proyecto
     * ⚠️ REEMPLAZA ESTO CON TU CONTRASEÑA REAL
     */
    public static final String DB_PASSWORD = "MkkTXwEozmjmSjk1";
    
    // ============================================
    // STRINGS DE CONEXIÓN
    // ============================================
    
    /**
     * String de conexión JDBC para PostgreSQL/Supabase
     * Formato completo con SSL
     */
    public static String getJdbcUrl() {
        return String.format(
            "jdbc:postgresql://%s:%s/%s?sslmode=require",
            DB_HOST, DB_PORT, DB_NAME
        );
    }
    
    /**
     * String de conexión alternativa con configuraciones adicionales
     */
    public static String getJdbcUrlWithOptions() {
        return String.format(
            "jdbc:postgresql://%s:%s/%s?sslmode=require&user=%s&password=%s&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
            DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
        );
    }
    
    /**
     * Validar si la configuración está completa
     */
    public static boolean isConfigured() {
        return !DB_HOST.contains("xxxxxxxxxxxxx") 
            && !DB_PASSWORD.equals("tu-password-aqui")
            && !SUPABASE_ANON_KEY.equals("tu-supabase-anon-key-aqui");
    }
    
    /**
     * Obtener información de configuración (sin mostrar passwords)
     */
    public static String getConfigInfo() {
        return String.format("""
            ╔═══════════════════════════════════════════════════╗
            ║       CONFIGURACIÓN SUPABASE                      ║
            ╠═══════════════════════════════════════════════════╣
            ║ Supabase URL: %-35s ║
            ║ DB Host:      %-35s ║
            ║ DB Port:      %-35s ║
            ║ DB Name:      %-35s ║
            ║ DB User:      %-35s ║
            ║ Configurado:  %-35s ║
            ╚═══════════════════════════════════════════════════╝
            """,
            SUPABASE_URL,
            DB_HOST,
            DB_PORT,
            DB_NAME,
            DB_USER,
            isConfigured() ? "✓ SÍ" : "✗ NO"
        );
    }
}
