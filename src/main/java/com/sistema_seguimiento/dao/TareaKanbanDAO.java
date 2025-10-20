package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.TareaKanban;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;

public class TareaKanbanDAO {

    public TareaKanban save(TareaKanban tarea) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            if (tarea.getId() == null){
                em.persist(tarea);
            }else {
                tarea = em.merge(tarea);
            }
            tx.commit();
            return tarea;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al guardar la tarea Kanban",e);
        }finally {
            em.close();
        }

    }

    public void delete(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Optional<TareaKanban> tareaOpt = findById(id);
            if (tareaOpt.isPresent()) {
                em.remove(em.merge(tareaOpt.get())); // Merge para re-adjuntar la entidad antes de remover
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al eliminar la tarea Kanban", e);
        } finally {
            em.close();
        }
    }

    public Optional<TareaKanban> findById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TareaKanban tarea = em.find(TareaKanban.class, id);
            return Optional.ofNullable(tarea);
        }finally {
            em.close();
        }
    }
}
