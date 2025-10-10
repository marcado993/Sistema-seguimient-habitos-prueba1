package com.example.dao;

import com.example.model.TareaKanban;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * DAO para gestionar las tareas en el Kanban
 * Permite guardar/cargar/actualizar posiciones y estados de hábitos en el calendario
 */
public class TareaKanbanDAO {
    
    /**
     * Obtiene todas las tareas del Kanban de un usuario
     */
    public List<TareaKanban> findByUsuarioId(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<TareaKanban> query = em.createQuery(
                "SELECT t FROM TareaKanban t WHERE t.usuarioId = :usuarioId ORDER BY t.fechaActualizacion DESC", 
                TareaKanban.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Guarda o actualiza una tarea en el Kanban
     */
    public TareaKanban save(TareaKanban tarea) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            if (tarea.getId() == null) {
                em.persist(tarea);
            } else {
                tarea = em.merge(tarea);
            }
            
            em.getTransaction().commit();
            return tarea;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca una tarea específica de un usuario por hábito
     */
    public TareaKanban findByUsuarioAndHabito(String usuarioId, Long habitoId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<TareaKanban> query = em.createQuery(
                "SELECT t FROM TareaKanban t WHERE t.usuarioId = :usuarioId AND t.habitoId = :habitoId", 
                TareaKanban.class
            );
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("habitoId", habitoId);
            
            List<TareaKanban> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }
    
    /**
     * Elimina una tarea del Kanban
     */
    public void delete(Long tareaId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            TareaKanban tarea = em.find(TareaKanban.class, tareaId);
            if (tarea != null) {
                em.remove(tarea);
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Elimina todas las tareas de un usuario
     */
    public void deleteAllByUsuario(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            em.createQuery("DELETE FROM TareaKanban t WHERE t.usuarioId = :usuarioId")
              .setParameter("usuarioId", usuarioId)
              .executeUpdate();
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
