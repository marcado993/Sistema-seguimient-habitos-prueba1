package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 🔴 FASE ROJA - Prueba Unitaria (3/8) para UsuarioDAO.addPoints()
 * 
 * ESTE TEST DEBE FALLAR porque el método addPoints() NO EXISTE en UsuarioDAOJPA
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioDAOAddPointsTest {
    
    private static EntityManagerFactory emf;
    private EntityManager em;
    private UsuarioDAOJPA usuarioDAO;
    private Usuario usuarioTest;
    
    @BeforeAll
    static void setupClass() {
        emf = Persistence.createEntityManagerFactory("TestPU");
    }
    
    @BeforeEach
    void setup() {
        em = emf.createEntityManager();
        usuarioDAO = new UsuarioDAOJPA();
        
        // Crear usuario de prueba
        em.getTransaction().begin();
        usuarioTest = new Usuario();
        usuarioTest.setUsername("test_points_" + System.currentTimeMillis());
        usuarioTest.setEmail("test_points_" + System.currentTimeMillis() + "@test.com");
        usuarioTest.setPassword("password123");
        usuarioTest.setPuntos(0);
        em.persist(usuarioTest);
        em.getTransaction().commit();
        em.clear();
    }
    
    @AfterEach
    void tearDown() {
        if (em != null && em.isOpen()) {
            if (usuarioTest != null && usuarioTest.getId() != null) {
                em.getTransaction().begin();
                Usuario u = em.find(Usuario.class, usuarioTest.getId());
                if (u != null) {
                    em.remove(u);
                }
                em.getTransaction().commit();
            }
            em.close();
        }
    }
    
    @AfterAll
    static void tearDownClass() {
        if (emf != null) {
            emf.close();
        }
    }
    
    /**
     * 🔴 UNA SOLA PRUEBA UNITARIA (3/8) - TDD Puro
     * DEBE FALLAR: addPoints() no existe en UsuarioDAOJPA
     */
    @Test
    @DisplayName("addPoints() debe agregar puntos correctamente a un usuario")
    void testAddPoints_UsuarioExistente_DebeAgregarPuntos() {
        // Arrange
        Integer usuarioId = usuarioTest.getId();
        int puntosAAgregar = 10;
        int puntosEsperados = 10;
        
        // Act
        boolean resultado = usuarioDAO.addPoints(usuarioId, puntosAAgregar);
        
        // Assert
        assertTrue(resultado, "addPoints() debería retornar true");
        
        // Limpiar la caché de primer nivel y refrescar desde BD
        em.clear();
        em.getEntityManagerFactory().getCache().evictAll();
        
        // Crear nuevo EntityManager para forzar lectura desde BD
        EntityManager emFresh = emf.createEntityManager();
        Usuario usuarioActualizado = emFresh.find(Usuario.class, usuarioId);
        emFresh.close();
        
        assertNotNull(usuarioActualizado, "El usuario debe existir");
        assertEquals(puntosEsperados, usuarioActualizado.getPuntos(),
            "Los puntos deben ser " + puntosEsperados);
    }
}
