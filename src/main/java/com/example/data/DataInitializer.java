package com.example.data;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DataInitializer implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("✅ Sistema iniciado - Sin datos de prueba");
        
        // DESHABILITADO: Creación automática de hábitos de prueba
        // Los usuarios deben crear sus propios hábitos desde la interfaz
        
        /*
        try {
            // Crear algunos hábitos de ejemplo
            HabitoDAO habitoDAO = new HabitoDAO();
            
            // Verificar si ya existen hábitos
            if (habitoDAO.findByUsuarioId("demo").isEmpty()) {
                // Crear hábitos de ejemplo
                Habito ejercicio = new Habito("Hacer ejercicio", 
                    "30 minutos de ejercicio cardiovascular", 
                    Habito.FrecuenciaHabito.DIARIO, 1, "demo");
                
                Habito lectura = new Habito("Leer libros", 
                    "Leer al menos 20 páginas de un libro", 
                    Habito.FrecuenciaHabito.DIARIO, 1, "demo");
                
                Habito agua = new Habito("Beber agua", 
                    "Mantener una buena hidratación", 
                    Habito.FrecuenciaHabito.DIARIO, 8, "demo");
                
                habitoDAO.save(ejercicio);
                habitoDAO.save(lectura);
                habitoDAO.save(agua);
                
                System.out.println("Datos de ejemplo creados exitosamente");
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar datos: " + e.getMessage());
            e.printStackTrace();
        }
        */
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup si es necesario
    }
}
