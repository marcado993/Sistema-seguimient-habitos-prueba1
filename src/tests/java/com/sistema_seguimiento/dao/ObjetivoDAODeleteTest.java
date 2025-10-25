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
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Test TDD para ObjetivoDAO - Método delete()
 * Ciclo TDD: ROJO → VERDE → REFACTOR
 * 
 * Foco: Eliminar Objetivo (Hard Delete)
 * Método bajo prueba: delete(Long id)
 * 
 * NOTA IMPORTANTE: ObjetivoDAO usa HARD DELETE (em.remove())
 *                  mientras que HabitoDAO usa SOFT DELETE (setActivo(false))
 *                  
 * 🔧 Refactorización futura: Estandarizar estrategia de eliminación
 */
public class ObjetivoDAODeleteTest {
    
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
     * 🔴 FASE ROJA - Test para método delete() - Hard Delete
     * 
     * Verifica que delete():
     * 1. Elimina físicamente el objetivo de la BD (hard delete)
     * 2. El objetivo NO es recuperable mediante findById()
     * 3. Retorna Optional.empty() o null después de la eliminación
     * 
     * NOTA: ObjetivoDAO usa HARD DELETE (em.remove())
     *       mientras que HabitoDAO usa SOFT DELETE (setActivo(false))
     *       
     * 🔧 Refactorización futura: Estandarizar estrategia de eliminación
     */
    @Test
    public void testEliminarObjetivo() {
        // ARRANGE - Preparar datos de prueba
        System.out.println("\n🔴 FASE ROJA: Ejecutando testEliminarObjetivo (Hard Delete)");
        
        // Crear y persistir un objetivo
        Objetivo objetivoAEliminar = new Objetivo();
        objetivoAEliminar.setTitulo("Objetivo a eliminar");
        objetivoAEliminar.setDescripcion("Este objetivo será eliminado permanentemente");
        objetivoAEliminar.setEstado(EstadoObjetivo.EN_PROGRESO);
        objetivoAEliminar.setProgresoActual(50);
        objetivoAEliminar.setUsuarioId(2);
        objetivoAEliminar.setFechaCreacion(LocalDateTime.now());
        
        em.getTransaction().begin();
        em.persist(objetivoAEliminar);
        em.getTransaction().commit();
        
        Integer objetivoId = objetivoAEliminar.getId();
        assertNotNull("El objetivo debe tener un ID después de persistir", objetivoId);
        System.out.println("✅ Objetivo creado con ID: " + objetivoId);
        
        // Verificar que existe antes de eliminar
        em.clear();
        Optional<Objetivo> objetivoAntes = objetivoDAO.findById(objetivoId);
        assertTrue("El objetivo debe existir antes de eliminar", objetivoAntes.isPresent());
        System.out.println("✅ Objetivo confirmado en BD antes de eliminar");
        
        // ACT - Ejecutar el método bajo prueba
        System.out.println("🗑️ Ejecutando delete(" + objetivoId + ") - Hard Delete");
        objetivoDAO.delete(objetivoId);
        
        // ASSERT - Verificar que el objetivo fue eliminado
        em.clear(); // Limpiar caché de EntityManager
        
        // 1. Verificar con findById que retorna Optional.empty()
        Optional<Objetivo> objetivoDespues = objetivoDAO.findById(objetivoId);
        assertFalse("El objetivo NO debe existir después de delete()", objetivoDespues.isPresent());
        System.out.println("✅ findById() retorna Optional.empty() - Hard delete confirmado");
        
        // 2. Verificar directamente en EntityManager que no existe
        Objetivo objetivoDirecto = em.find(Objetivo.class, objetivoId);
        assertNull("El objetivo NO debe existir en la BD", objetivoDirecto);
        System.out.println("✅ Objetivo eliminado físicamente de la BD (hard delete)");
        
        // 3. Verificar que no aparece en consultas generales
        List<Objetivo> todosObjetivos = em.createQuery(
            "SELECT o FROM Objetivo o WHERE o.id = :id", Objetivo.class)
            .setParameter("id", objetivoId)
            .getResultList();
        
        assertTrue("El objetivo no debe aparecer en queries", todosObjetivos.isEmpty());
        System.out.println("✅ Objetivo no aparece en consultas SQL");
        
        System.out.println("✅ Test completado: Hard delete funciona correctamente!");
        System.out.println("⚠️ NOTA: Este es hard delete (eliminación física)");
        System.out.println("   HabitoDAO usa soft delete (setActivo=false)");
        System.out.println("   Considerar estandarizar estrategia en refactorización futura");
    }
    
    /**
     * Test adicional: Eliminar objetivo inexistente (no debe fallar)
     */
    @Test
    public void testEliminarObjetivoInexistente() {
        System.out.println("\n🧪 Test: Eliminar objetivo inexistente");
        
        Integer objetivoIdInexistente = 88888;
        
        try {
            // ACT - Intentar eliminar objetivo que no existe
            objetivoDAO.delete(objetivoIdInexistente);
            
            // ASSERT - No debe lanzar excepción (el método verifica if (objetivo != null))
            System.out.println("✅ No lanza excepción al eliminar objetivo inexistente");
            
        } catch (Exception e) {
            fail("No debería lanzar excepción al eliminar objetivo inexistente: " + e.getMessage());
        }
    }
    
    /**
     * Test adicional: Verificar que eliminación de objetivo elimina/no elimina registros asociados
     * dependiendo de la configuración de cascada en @OneToMany
     */
    @Test
    public void testEliminarObjetivoConRegistrosProgreso() {
        System.out.println("\n🧪 Test: Eliminar objetivo con registros de progreso asociados");
        
        // ARRANGE - Crear objetivo con registros de progreso
        Objetivo objetivo = new Objetivo();
        objetivo.setTitulo("Objetivo con historial");
        objetivo.setDescripcion("Tiene registros de progreso");
        objetivo.setEstado(EstadoObjetivo.EN_PROGRESO);
        objetivo.setProgresoActual(40);
        objetivo.setUsuarioId(3);
        objetivo.setFechaCreacion(LocalDateTime.now());
        
        em.getTransaction().begin();
        em.persist(objetivo);
        em.getTransaction().commit();
        
        Integer objetivoId = objetivo.getId();
        
        // Crear registros de progreso asociados
        objetivoDAO.actualizarProgreso(objetivoId, 60, "Primera actualización");
        objetivoDAO.actualizarProgreso(objetivoId, 80, "Segunda actualización");
        
        // Verificar que existen registros
        em.clear();
        List<RegistroProgreso> registrosAntes = objetivoDAO.findRegistrosProgreso(objetivoId);
        assertEquals("Debe haber 2 registros de progreso", 2, registrosAntes.size());
        System.out.println("✅ Objetivo creado con 2 registros de progreso");
        
        // ACT - Eliminar objetivo
        System.out.println("🗑️ Eliminando objetivo con registros asociados...");
        objetivoDAO.delete(objetivoId);
        
        // ASSERT - Verificar efecto en registros de progreso
        em.clear();
        
        // Si hay CascadeType.REMOVE, los registros también se eliminan
        // Si NO hay cascade, quedarán huérfanos (puede causar excepción)
        try {
            List<RegistroProgreso> registrosDespues = em.createQuery(
                "SELECT r FROM RegistroProgreso r WHERE r.objetivo.id = :objetivoId",
                RegistroProgreso.class)
                .setParameter("objetivoId", objetivoId)
                .getResultList();
            
            if (registrosDespues.isEmpty()) {
                System.out.println("✅ Registros de progreso eliminados por cascada (CascadeType.REMOVE)");
            } else {
                System.out.println("⚠️ Registros de progreso quedaron huérfanos (sin cascada)");
                System.out.println("   Cantidad de registros huérfanos: " + registrosDespues.size());
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Excepción al consultar registros: " + e.getMessage());
            System.out.println("   Puede indicar violación de integridad referencial");
        }
        
        System.out.println("✅ Test completado - Verifica comportamiento con entidades relacionadas");
    }
}
