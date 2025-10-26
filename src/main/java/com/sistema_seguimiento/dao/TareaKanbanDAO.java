package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.TareaKanban;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class TareaKanbanDAO {
    
    private static final Logger LOGGER = Logger.getLogger(TareaKanbanDAO.class.getName());

    /**
     * Guarda una nueva tarea Kanban en la base de datos
     * @param tarea La tarea a guardar
     * @return La tarea guardada con su ID generado
     */
    public TareaKanban save(TareaKanban tarea) {
        if (tarea.getId() != null) {
            throw new IllegalArgumentException("Para guardar una nueva tarea, el ID debe ser null. Use update() para modificar.");
        }
        executeWithTransaction(em -> {
            em.persist(tarea);
            LOGGER.info("Tarea Kanban guardada con ID: " + tarea.getId());
        });
        return tarea;
    }
    
    /**
     * Actualiza una tarea Kanban existente
     * @param tarea La tarea a actualizar
     * @return La tarea actualizada
     */
    public TareaKanban update(TareaKanban tarea) {
        if (tarea.getId() == null) {
            throw new IllegalArgumentException("Para actualizar una tarea, debe tener un ID. Use save() para crear nueva.");
        }
        executeWithTransaction(em -> {
            em.merge(tarea);
            LOGGER.info("Tarea Kanban actualizada con ID: " + tarea.getId());
        });
        return tarea;
    }

    private void executeWithTransaction(java.util.function.Consumer<EntityManager> operation) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            operation.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            LOGGER.severe("Error en la transacción del TareaKanbanDAO: " + e.getMessage());
            throw new RuntimeException("Error en la transacción del TareaKanbanDAO", e);
        } finally {
            em.close();
        }
    }

    /**
     * Elimina una tarea Kanban por su ID
     * @param id El ID de la tarea a eliminar
     */
    public void delete(Integer id) {
        executeWithTransaction(em -> {
            Optional<TareaKanban> tareaOpt = findById(id);
            if (tareaOpt.isPresent()) {
                em.remove(em.merge(tareaOpt.get()));
                LOGGER.info("Tarea Kanban eliminada con ID: " + id);
            } else {
                LOGGER.warning("No se encontró tarea Kanban con ID: " + id);
            }
        });
    }

    /**
     * Busca una tarea Kanban por su ID
     * @param id El ID de la tarea
     * @return Optional con la tarea si existe
     */
    public Optional<TareaKanban> findById(Integer id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TareaKanban tarea = em.find(TareaKanban.class, id);
            return Optional.ofNullable(tarea);
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todas las tareas Kanban
     * @return Lista de todas las tareas
     */
    public List<TareaKanban> findAll() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<TareaKanban> query = em.createQuery(
                "SELECT t FROM TareaKanban t ORDER BY t.id", TareaKanban.class);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.severe("Error al obtener todas las tareas: " + e.getMessage());
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    /**
     * Verifica si existe una tarea con el ID especificado
     * @param id El ID a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    /**
     * Obtiene tareas por estado
     * @param estado El estado de las tareas (TODO, IN_PROGRESS, DONE)
     * @return Lista de tareas con el estado especificado
     */
    public List<TareaKanban> findByEstado(String estado) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<TareaKanban> query = em.createQuery(
                "SELECT t FROM TareaKanban t WHERE t.estado = :estado ORDER BY t.id", 
                TareaKanban.class);
            query.setParameter("estado", estado);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.severe("Error al buscar tareas por estado: " + e.getMessage());
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta el número total de tareas
     * @return El número de tareas en la base de datos
     */
    public long count() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(t) FROM TareaKanban t", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            LOGGER.severe("Error al contar tareas: " + e.getMessage());
            return 0L;
        } finally {
            em.close();
        }
    }
}
