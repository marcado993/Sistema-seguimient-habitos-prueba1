package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.TareaKanban;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class TareaKanbanDAOTests {
    private EntityManagerFactory emf;
    private EntityManager em;
    private TareaKanbanDAO tareaKanbanDAO;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        em = emf.createEntityManager();
        tareaKanbanDAO = new TareaKanbanDAO();

        em.getTransaction().begin();
        em.createQuery("DELETE FROM TareaKanban").executeUpdate();
        em.getTransaction().commit();
    }

    @After
    public void tearDown() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }

    @Test
    public void give_tareaKanban_when_cicloVidaCRUD_then_ok() {
//         CREATE
        TareaKanban nuevaTarea = new TareaKanban("Implementar TDD para DAO");
        TareaKanban tareaGuardada = tareaKanbanDAO.save(nuevaTarea);
        assertNotNull("La tarea guardada debe tener un ID.", tareaGuardada.getId());

//          READ
        Optional<TareaKanban> tareaRecuperadaOpt = tareaKanbanDAO.findById(tareaGuardada.getId());
        assertTrue("La tarea debería encontrarse en la BD.", tareaRecuperadaOpt.isPresent());
        assertEquals("Implementar TDD para DAO", tareaRecuperadaOpt.get().getTitulo());

//          UPDATE
        TareaKanban tareaParaActualizar = tareaRecuperadaOpt.get();
        tareaParaActualizar.setTitulo("Implementar y documentar TDD");
        tareaKanbanDAO.save(tareaParaActualizar);

        Optional<TareaKanban> tareaActualizadaOpt = tareaKanbanDAO.findById(tareaGuardada.getId());
        assertTrue(tareaActualizadaOpt.isPresent());
        assertEquals("Implementar y documentar TDD", tareaActualizadaOpt.get().getTitulo());

        // --- DELETE ---
        tareaKanbanDAO.delete(tareaGuardada.getId());
        Optional<TareaKanban> tareaEliminadaOpt = tareaKanbanDAO.findById(tareaGuardada.getId());
        assertFalse("La tarea no debería existir después de ser eliminada.", tareaEliminadaOpt.isPresent());
    }
}