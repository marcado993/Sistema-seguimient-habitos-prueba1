package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.Habito;
import com.sistema_seguimiento.model.Objetivo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class HabitoDAOTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private HabitoDAO habitoDAO;
    private ObjetivoDAO objetivoDAO;

    @Before
    public void setUp(){
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        em = emf.createEntityManager();
        habitoDAO = new HabitoDAO();
        objetivoDAO = new ObjetivoDAO();
//        Limpiamos la BD antes de cada test para asegurar el aislamiento
        em.getTransaction().begin();
        em.createQuery("DELETE FROM RegistroHabito").executeUpdate();
        em.createQuery("DELETE FROM Habito").executeUpdate();
        em.createQuery("DELETE FROM Objetivo").executeUpdate();
        em.getTransaction().commit();
    }


    @Test
    public void given_Objetivo_when_Habito_then_CRUD(){
        Objetivo objetivoPadre = new Objetivo();
        objetivoPadre.setTitulo("Obj para Hábitos");
        objetivoPadre.setUsuarioId("test_user");
        Objetivo objetivoGuardado = objetivoDAO.save(objetivoPadre);


//        CREATE
        Habito nuevoHabito = new Habito("Leer 30 minutos", "Leer un libro de desarrollo personal", Habito.FrecuenciaHabito.DIARIO, 10, "test_user");
        nuevoHabito.setObjetivo(objetivoGuardado);

        Habito habitoGuardaro = habitoDAO.save(nuevoHabito);
        assertNotNull("El hábito guardado debe tener un ID", habitoGuardaro.getId());

//        READ
        Optional<Habito> habitoRecuperadoOpt = habitoDAO.findById(habitoGuardaro.getId());
        assertTrue("El hábito debería encontrarse en la BD", habitoRecuperadoOpt.isPresent());
        Habito habitoRecuperado = habitoRecuperadoOpt.get();
        assertEquals("Leer 30 minutos", habitoRecuperado.getNombre());
        assertNotNull("El hábito recuperado debe tener un objetivo asociado", habitoRecuperado.getObjetivo());
        assertEquals("El ID del ob asociado no coincide", objetivoGuardado.getId(), habitoRecuperado.getObjetivo().getId());

//        UPDATE
        habitoRecuperado.setNombre("Leer 15 páginas al día");
        habitoDAO.save(habitoRecuperado);

        Optional<Habito> habitoActualizadoOpt = habitoDAO.findById(habitoGuardaro.getId());
        assertEquals("Leer 15 páginas al día", habitoActualizadoOpt.get().getNombre());
        System.out.println("VERIFICACIÓN DE UPDATE: Correcta!");

        // DELETE
        habitoDAO.delete(habitoGuardaro.getId());

        Optional<Habito> habitoEliminadoOpt = habitoDAO.findById(habitoGuardaro.getId());
        assertTrue("El hábito aún debe existir en la DB (soft delete)", habitoEliminadoOpt.isPresent());
        assertFalse("El estado 'activo' del hábito debe ser false después de eliminar", habitoEliminadoOpt.get().getActivo());
        System.out.println("VERIFICACIÓN DE DELETE (SOFT): Correcta!");
    }

    @After
    public void tearDown(){
        em.close();
        emf.close();
    }
}