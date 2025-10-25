package com.sistema_seguimiento.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Clase de prueba para verificar la conexión a Supabase
 * 
 * Ejecuta esta clase como main para diagnosticar problemas de conexión
 */
public class TestSupabaseConnection {

    // Credenciales de prueba para Supabase (del persistence.xml de tests)
    private static final String DB_HOST = "aws-1-us-east-2.pooler.supabase.com";
    private static final String DB_PORT = "6543";
    private static final String DB_PORT_DIRECTO = "6543";
    private static final String DB_NAME = "postgres";
    private static final String DB_USER = "postgres.pofkyvcgltumwpxvuzpp";
    private static final String DB_PASSWORD = "G7v$kP9tL#x2Rq!M";

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║   TEST DE CONEXIÓN A SUPABASE                     ║");
        System.out.println("╚═══════════════════════════════════════════════════╝\n");

        // Mostrar configuración
        System.out.println("📋 CONFIGURACIÓN:");
        System.out.println("   Host:     " + DB_HOST);
        System.out.println("   Puerto:   " + DB_PORT);
        System.out.println("   Base de Datos: " + DB_NAME);
        System.out.println("   Usuario:  " + DB_USER);
        System.out.println();

        // Intentar conexión
        testConnection();
    }

    private static void testConnection() {
        System.out.println("🔄 Intentando conectar a Supabase...\n");

        // Intentar primero con el puerto del pooler (6543)
        if (!testConnectionWithPort(DB_PORT, true)) {
            System.out.println("\n⚠️  El puerto 6543 no funciona. Intentando puerto directo 5432...\n");
            testConnectionWithPort(DB_PORT_DIRECTO, false);
        }
    }

    private static boolean testConnectionWithPort(String port, boolean isPrimero) {
        try {
            // Cargar el driver
            if (isPrimero) {
                System.out.println("1️⃣  Cargando driver PostgreSQL...");
                Class.forName("org.postgresql.Driver");
                System.out.println("   ✅ Driver cargado correctamente\n");
            }

            // Obtener URL
            String url = String.format(
                "jdbc:postgresql://%s:%s/%s?sslmode=require&connectTimeout=10",
                DB_HOST, port, DB_NAME
            );
            System.out.println((isPrimero ? "2️⃣" : "3️⃣") + "  Intentando puerto " + port + ":");
            System.out.println("   URL: " + url + "\n");

            long startTime = System.currentTimeMillis();

            Connection conn = DriverManager.getConnection(
                url,
                DB_USER,
                DB_PASSWORD
            );

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("   ✅ ¡CONEXIÓN EXITOSA EN PUERTO " + port + "!\n");
            System.out.println("   Tiempo de conexión: " + duration + "ms\n");

            // Ejecutar query de prueba
            System.out.println((isPrimero ? "3️⃣" : "4️⃣") + "  Ejecutando query de prueba...");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NOW()");

            if (rs.next()) {
                System.out.println("   ✅ Query ejecutada correctamente");
                System.out.println("   Timestamp del servidor: " + rs.getTimestamp(1) + "\n");
            }

            // Obtener información de la base de datos
            System.out.println((isPrimero ? "4️⃣" : "5️⃣") + "  Información de la base de datos:");
            System.out.println("   - Catálogo: " + conn.getCatalog());
            System.out.println("   - Schema: " + conn.getSchema());
            System.out.println("   - URL: " + conn.getMetaData().getURL() + "\n");

            // Listar tablas
            System.out.println((isPrimero ? "5️⃣" : "6️⃣") + "  Tablas disponibles:");
            ResultSet tables = conn.getMetaData().getTables(
                null, "public", "%", new String[]{"TABLE"}
            );

            boolean hasTablas = false;
            while (tables.next()) {
                hasTablas = true;
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("   - " + tableName);
            }

            if (!hasTablas) {
                System.out.println("   (No hay tablas en el schema 'public')");
            }

            System.out.println();

            // Cerrar conexión
            stmt.close();
            conn.close();

            System.out.println("╔═══════════════════════════════════════════════════╗");
            System.out.println("║  ✅ TODO FUNCIONA CORRECTAMENTE                   ║");
            System.out.println("║  Usa el puerto " + port + " en tu configuración   ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            
            return true;

        } catch (ClassNotFoundException e) {
            System.err.println("❌ ERROR: No se puede cargar el driver PostgreSQL");
            System.err.println("   Asegúrate de tener postgresql en tu pom.xml");
            return false;

        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("timed out")) {
                System.err.println("❌ ERROR: TIMEOUT EN PUERTO " + port);
                System.err.println("   La base de datos no responde");
                return false;

            } else if (e.getMessage() != null && e.getMessage().contains("authentication failed")) {
                System.err.println("❌ ERROR DE AUTENTICACIÓN EN PUERTO " + port);
                System.err.println("   Credenciales incorrectas");
                System.err.println("   Usuario: " + DB_USER);
                return false;

            } else {
                System.err.println("❌ ERROR EN PUERTO " + port + ": " + e.getMessage());
                return false;
            }
        }
    }
}
