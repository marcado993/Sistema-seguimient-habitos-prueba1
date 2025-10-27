package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*; // Asegúrate de usar JUnit 5

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PetDAOTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private PetDAO petDAO;
    private UsuarioDAOJPA usuarioDAO; // Necesario para crear usuarios de prueba
    private Usuario usuarioPrueba;

    @BeforeAll
    static void setupClass() {
        try {
            emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        } catch (Exception e) {
            System.err.println("Error fatal inicializando EMF: " + e.getMessage());
            e.printStackTrace();
            throw e; // Fallar rápido si EMF no se crea
        }
    }

    @AfterAll
    static void tearDownClass() {
        if (emf != null && emf.isOpen()) { // Verificar que no sea null y esté abierto
            emf.close();
        }
    }

    @BeforeEach
    void setup() {
        em = null; // Reiniciar em
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager(); // Crear EM para este test
            petDAO = new PetDAO();
            usuarioDAO = new UsuarioDAOJPA();

            // Crear usuario de prueba DENTRO de una transacción
            tx = em.getTransaction();
            tx.begin();

            usuarioPrueba = new Usuario("Test Pet User " + System.nanoTime(), "petuser" + System.nanoTime() + "@test.com", "pass123");
            em.persist(usuarioPrueba);
            tx.commit();
            System.out.println("Usuario de prueba creado con ID: " + usuarioPrueba.getId());
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try {
                    tx.rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("Error haciendo rollback en setup: " + rollbackEx.getMessage());
                }
            }
            // Asegurar cierre de EM si se abrió
            if (em != null && em.isOpen()) {
                em.close();
            }
            // Relanzar excepción para que el test falle claramente en setup
            throw new RuntimeException("Fallo en @BeforeEach setup: " + e.getMessage(), e);
        }
        // Nota: NO cerramos 'em' aquí, se cerrará en tearDown
    }

    @AfterEach
    void tearDown() {
        EntityTransaction tx = null;
        if (em != null && em.isOpen()) {
            try {
                tx = em.getTransaction();
                tx.begin();
                // Limpiar datos creados por el test
                // Usar parámetros es más seguro
                if (usuarioPrueba != null && usuarioPrueba.getId() != null) {
                    // Luego Usuario
                    // Usamos merge para re-adjuntar la entidad si fue desasociada
                    // y luego remove, o find y luego remove
                    Usuario u = em.find(Usuario.class, usuarioPrueba.getId());
                    if (u != null) {
                        em.remove(u);
                    }
                    System.out.println("Limpiando usuario de prueba ID: " + usuarioPrueba.getId());
                }
                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    try {
                        tx.rollback();
                        System.err.println("Rollback realizado en teardown.");
                    } catch (Exception rollbackEx) {
                        System.err.println("Error haciendo rollback en teardown: " + rollbackEx.getMessage());
                    }
                }
                System.err.println("Error durante @AfterEach tearDown: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Asegurar SIEMPRE el cierre del EntityManager
                if (em != null && em.isOpen()) {
                    em.close();
                }
            }
        }
        usuarioPrueba = null;
    }

    @Test
    @Order(3)
    @DisplayName("getCurrentUserPet debe devolver la mascota activa más reciente")
    void getCurrentUserPet() {
        Long usuarioId = Long.valueOf(usuarioPrueba.getId());
        UserPet mascota1 = petDAO.saveNewPet(usuarioId, PetType.HUEVO); // Se desactivará
        try { Thread.sleep(10); } catch (InterruptedException e) {}
        UserPet mascota2 = petDAO.saveNewPet(usuarioId, PetType.BEBE); // Esta debe ser la activa

        Optional<UserPet> mascotaActivaOpt = petDAO.getCurrentUserPet(usuarioId);

        // Assert
        assertTrue(mascotaActivaOpt.isPresent(), "Debería encontrarse una mascota activa");
        UserPet mascotaActiva = mascotaActivaOpt.get();
        assertEquals(mascota2.getId(), mascotaActiva.getId(), "Debe devolver la última mascota creada (mascota2)");
        assertTrue(mascotaActiva.isActive(), "La mascota devuelta debe estar activa");
        assertEquals(PetType.BEBE, mascotaActiva.getState(), "El estado debe ser el de la última mascota");

        // Verificar que la mascota1 está inactiva (si tienes un método findById)
        // Optional<UserPet> mascota1Recuperada = petDAO.findById(mascota1.getId());
        // assertTrue(mascota1Recuperada.isPresent());
        // assertFalse(mascota1Recuperada.get().isActive());
    }

    @Test
    @Order(4)
    @DisplayName("getCurrentUserPet debe devolver Optional.empty si no hay mascota activa")
    void getCurrentUserPet_NoActiva() {
        Long usuarioId = Long.valueOf(usuarioPrueba.getId());

        Optional<UserPet> mascotaActivaOpt = petDAO.getCurrentUserPet(usuarioId);

        assertFalse(mascotaActivaOpt.isPresent(), "No debería encontrar mascota activa");
    }

    @Test
    @Order(1)
    @DisplayName("saveNewPet debe crear una mascota activa (Huevo) y desactivar la anterior")
    void saveNewPet() {
        Long usuarioId = Long.valueOf(usuarioPrueba.getId()); // Obtén el ID real
        PetType tipoMascota = PetType.HUEVO;

        // (Opcional) Crear una mascota previa para probar la desactivación
        // Necesitarás crear primero la plantilla Pet si no existe
        // Pet petPlantillaVieja = petDAO.findPetByType(PetType.ALGUN_TIPO).orElse(null);
        // if(petPlantillaVieja == null) { /* Crear y guardar plantilla */ }
        // UserPet mascotaVieja = new UserPet(usuarioPrueba, petPlantillaVieja, PetType.ALGUN_TIPO);
        // petDAO.saveUserPet(mascotaVieja); // Guardar mascota vieja

        // !! ESTA LÍNEA DEBERÍA FALLAR INICIALMENTE (ROJO) !!
        UserPet nuevaMascota = petDAO.saveNewPet(usuarioId, tipoMascota);

        assertNotNull(nuevaMascota, "La nueva mascota no debería ser null");
        assertNotNull(nuevaMascota.getId(), "La nueva mascota debe tener un ID");
        assertEquals(usuarioId.intValue(), nuevaMascota.getUsuario().getId().intValue(), "El ID de usuario debe coincidir");
        assertEquals(tipoMascota, nuevaMascota.getState(), "El tipo/estado de la mascota debe ser HUEVO");
        assertTrue(nuevaMascota.isActive(), "La nueva mascota debe estar activa");
        assertNotNull(nuevaMascota.getPet(), "Debe estar asociada a una plantilla Pet");
        assertEquals(tipoMascota, nuevaMascota.getPet().getType(), "La plantilla Pet debe ser del tipo correcto");

        // (Opcional) Verificar que la mascota vieja (si se creó) está inactiva
        // Optional<UserPet> viejaRecuperada = petDAO.findById(mascotaVieja.getId()); // Asumiendo que tienes un findById en PetDAO
        // assertTrue(viejaRecuperada.isPresent());
        // assertFalse(viejaRecuperada.get().isActive(), "La mascota vieja debe estar inactiva");

        // Verificar que solo hay una mascota activa para el usuario
        em.clear(); // Limpiar caché
        Optional<UserPet> activaActual = petDAO.getCurrentUserPet(usuarioId);
        assertTrue(activaActual.isPresent(), "Debería haber una mascota activa");
        assertEquals(nuevaMascota.getId(), activaActual.get().getId(), "La mascota activa debe ser la recién creada");
    }

    @Test
    @Order(2)
    @DisplayName("updatePetState debe cambiar el estado de la mascota activa del usuario")
    void updatePetState() {
        Long usuarioId = Long.valueOf(usuarioPrueba.getId());
        UserPet mascotaInicial = petDAO.saveNewPet(usuarioId, PetType.HUEVO);
        PetType nuevoEstado = PetType.BEBE;

        // !! ESTA LÍNEA DEBERÍA FALLAR INICIALMENTE (ROJO) !!
        boolean resultado = petDAO.updatePetState(usuarioId, nuevoEstado);

        assertTrue(resultado, "La actualización debería ser exitosa");

        em.clear(); // Limpiar caché para leer desde la BD
        Optional<UserPet> mascotaActualizadaOpt = petDAO.getCurrentUserPet(usuarioId);
        assertTrue(mascotaActualizadaOpt.isPresent(), "Debe existir una mascota activa");
        assertEquals(nuevoEstado, mascotaActualizadaOpt.get().getState(), "El estado debe haberse actualizado a BEBE");
        assertEquals(mascotaInicial.getId(), mascotaActualizadaOpt.get().getId(), "Debe ser la misma mascota (mismo ID)");
    }


}