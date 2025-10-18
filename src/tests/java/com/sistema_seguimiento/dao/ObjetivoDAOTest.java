package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.Objetivo;
import com.sistema_seguimiento.model.Objetivo.EstadoObjetivo;
import com.sistema_seguimiento.model.RegistroProgreso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test TDD para ObjetivoDAO
 * Ciclo TDD: ROJO → VERDE → REFACTOR
 * 
 * Foco: Modificar Objetivo (Actualización de Progreso)
 * Método bajo prueba: actualizarProgreso(Long objetivoId, int nuevoProgreso, String observaciones)
 */
public class ObjetivoDAOTest {
    
    private static EntityManagerFactory emf;
    private EntityManager em;
    private ObjetivoDAO objetivoDAO;
    
    @BeforeClass
    public static void setUpClass() {
        // Inicializar EntityManagerFactory usando persistence-unit de test (H2 en memoria)
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
    }
    
    @AfterClass
    public static void tearDownClass() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
    
    @Before
    public void setUp() {
        em = emf.createEntityManager();
        objetivoDAO = new ObjetivoDAO();
        
        // Limpiar BD antes de cada test
        em.getTransaction().begin();
        em.createQuery("DELETE FROM RegistroProgreso").executeUpdate();
        em.createQuery("DELETE FROM Objetivo").executeUpdate();
        em.getTransaction().commit();
    }
    
    @After
    public void tearDown() {
        if (em != null && em.isOpen()) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
    
    /**
     * 🔴 FASE ROJA - Test que debe fallar inicialmente
     * 
     * Verifica que actualizarProgreso():
     * 1. Crea un nuevo RegistroProgreso con progreso anterior y nuevo
     * 2. Persiste el RegistroProgreso en la BD
     * 3. Actualiza el progreso del Objetivo
     * 4. El Objetivo actualizado se persiste en la BD
     */
    @Test
    public void testActualizarProgresoCreaRegistro() {
        // ARRANGE - Preparar datos de prueba
        System.out.println("\n🔴 FASE ROJA: Ejecutando testActualizarProgresoCreaRegistro");
        
        // Crear y persistir un objetivo inicial
        Objetivo objetivoInicial = new Objetivo();
        objetivoInicial.setTitulo("Aprender TDD");
        objetivoInicial.setDescripcion("Implementar pruebas unitarias con JUnit");
        objetivoInicial.setEstado(EstadoObjetivo.ACTIVO);
        objetivoInicial.setProgreso(30); // Progreso inicial 30%
        objetivoInicial.setUsuarioId("user123");
        objetivoInicial.setFechaCreacion(LocalDateTime.now());
        objetivoInicial.setFechaLimite(LocalDateTime.now().plusDays(30));
        
        em.getTransaction().begin();
        em.persist(objetivoInicial);
        em.getTransaction().commit();
        
        Long objetivoId = objetivoInicial.getId();
        assertNotNull("El objetivo debe tener un ID después de persistir", objetivoId);
        System.out.println("✅ Objetivo creado con ID: " + objetivoId + " y progreso inicial: 30%");
        
        // ACT - Ejecutar el método bajo prueba
        int nuevoProgreso = 65; // Actualizar a 65%
        String observaciones = "Se completó la fase de implementación de tests";
        
        System.out.println("🔧 Actualizando progreso de 30% → 65%");
        objetivoDAO.actualizarProgreso(objetivoId, nuevoProgreso, observaciones);
        
        // ASSERT - Verificar resultados
        em.clear(); // Limpiar caché de EntityManager
        
        // 1. Verificar que el objetivo se actualizó
        Objetivo objetivoActualizado = em.find(Objetivo.class, objetivoId);
        assertNotNull("El objetivo debe existir en la BD", objetivoActualizado);
        assertEquals("El progreso del objetivo debe ser 65%", 
                     Integer.valueOf(65), objetivoActualizado.getProgreso());
        System.out.println("✅ Objetivo actualizado: progreso = " + objetivoActualizado.getProgreso() + "%");
        
        // 2. Verificar que se creó un RegistroProgreso
        List<RegistroProgreso> registros = em.createQuery(
            "SELECT r FROM RegistroProgreso r WHERE r.objetivo.id = :objetivoId ORDER BY r.fechaRegistro DESC",
            RegistroProgreso.class)
            .setParameter("objetivoId", objetivoId)
            .getResultList();
        
        assertFalse("Debe existir al menos un registro de progreso", registros.isEmpty());
        assertEquals("Debe existir exactamente 1 registro de progreso", 1, registros.size());
        
        // 3. Verificar datos del RegistroProgreso
        RegistroProgreso registro = registros.get(0);
        assertNotNull("El registro debe tener un ID", registro.getId());
        assertEquals("El progreso anterior debe ser 30%", 
                     Integer.valueOf(30), registro.getProgresoAnterior());
        assertEquals("El progreso nuevo debe ser 65%", 
                     Integer.valueOf(65), registro.getProgresoNuevo());
        assertEquals("Las observaciones deben coincidir", 
                     observaciones, registro.getObservaciones());
        assertNotNull("La fecha de registro no debe ser null", registro.getFechaRegistro());
        
        System.out.println("✅ RegistroProgreso creado:");
        System.out.println("   - ID: " + registro.getId());
        System.out.println("   - Progreso anterior: " + registro.getProgresoAnterior() + "%");
        System.out.println("   - Progreso nuevo: " + registro.getProgresoNuevo() + "%");
        System.out.println("   - Observaciones: " + registro.getObservaciones());
        System.out.println("   - Fecha: " + registro.getFechaRegistro());
        
        System.out.println("✅ Test completado exitosamente!");
    }
    
    /**
     * Test adicional: verificar que actualizarProgreso maneja objetivo inexistente
     */
    @Test
    public void testActualizarProgresoObjetivoInexistente() {
        System.out.println("\n🧪 Test: Actualizar progreso de objetivo inexistente");
        
        // ACT & ASSERT - Intentar actualizar un objetivo que no existe
        Long objetivoIdInexistente = 99999L;
        
        try {
            objetivoDAO.actualizarProgreso(objetivoIdInexistente, 50, "No debería crear nada");
            
            // Verificar que NO se creó ningún registro
            em.clear();
            List<RegistroProgreso> registros = em.createQuery(
                "SELECT r FROM RegistroProgreso r", RegistroProgreso.class)
                .getResultList();
            
            assertTrue("No debe haber registros para objetivos inexistentes", 
                      registros.isEmpty());
            System.out.println("✅ No se crearon registros para objetivo inexistente");
            
        } catch (Exception e) {
            // Si lanza excepción también es válido
            System.out.println("✅ Excepción esperada: " + e.getMessage());
        }
    }
}
